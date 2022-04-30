package FastFileServer;

import FileServerProtocol.Exceptions.InvalidChunkException;
import FileServerProtocol.FileServerChunk.*;
import FileServerProtocol.Structs.FSCType;
import FileServerProtocol.Structs.FSParser;
import FileServerProtocol.Structs.FileMetaData;
import Utils.BoundedQueue;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

import static java.lang.System.exit;

public class FastFileServer implements Runnable {
    private final DatagramSocket socket;
    private final InetAddress gateway;
    private final int port;
    private final Path root;

    private int MTU = 512; // Default
    private final Map<FileMetaData, Path> cache = new HashMap<>();
    BoundedQueue<FSChunk> input = new BoundedQueue<>();

    Pinger pinger;
    Timer timer = new Timer("Pinger");
    Thread receiverThread;

    FFSReceiver receiver;
    FFSForwarder forwarder;

    //---- Constructors -----------------------------------------------------------------------------------------------

    public FastFileServer(InetAddress gateway, InetAddress local, Path root, int local_port, int server_port) throws IOException {
        this.socket = new DatagramSocket(local_port, local);
        this.port = server_port;
        this.gateway = gateway;
        this.root = root;
        init();
    }

    //---- Initializers -----------------------------------------------------------------------------------------------


    private void init() {
        initPinger();
        initReceiver();
        forwarder = new FFSForwarder(socket, gateway, port, FFSProperties.maxQueueSize, FFSProperties.ackTimeOut);
    }

    private void initPinger() {
        this.pinger = new Pinger(
                s -> {
                    try {
                        forwarder.sendChunk(s);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                },
                () -> exit(-1), // The proper way wasn't working
                FFSProperties.ackTimeOut);
    }

    private void initReceiver() {
        receiver = new FFSReceiver(socket, MTU, this::acknowledge, this::add);
        receiverThread = new Thread(receiver, "FFSReceiver");
    }


    //---- Run --------------------------------------------------------------------------------------------------------

    public void run() {
        int b = 0;

        while (b < FFSProperties.maxConnectionAttempts)
            try {

                socket.setSoTimeout(FFSProperties.stdTimeOut);
                this.announce();
                this.accepted();
                break;
            } catch (SocketTimeoutException e) {
                System.out.println("\tConnection Attempt " + ++b + "/" + FFSProperties.maxConnectionAttempts + " Timed out.");
            } catch (IOException e) {
                System.out.println("Server Error!" + e.getMessage());
                b = Integer.MAX_VALUE;
            }

        if (b < FFSProperties.maxConnectionAttempts)
            try {
                socket.setSoTimeout(0);
                this.updateFiles();
                this.sendCache();

                forwarder.start();
                receiverThread.start();

                while (!socket.isClosed())
                    this.answer();
            } catch (SocketTimeoutException e) {
                System.out.println("Server Timeout, Exiting");
            } catch (IOException e) {
                System.out.println("Server Error!" + e.getMessage());
            }

        timer.cancel();
        timer.purge();
        forwarder.cancel();
        receiverThread.interrupt();
    }

    //---- Setters ----------------------------------------------------------------------------------------------------


    public void setMTU(int MTU) {
        receiver.setMTU(MTU);
        this.MTU = MTU;
    }


    //---- FFS Handlers------------------------------------------------------------------------------------------------

    protected void add(FSChunk chunk){
        input.add(chunk);
        if (FFSProperties.seeIncomingFrames)
            System.out.println(chunk.toString(FFSProperties.extendFrameOutput));
    }

    public void announce() throws IOException {
        DatagramPacket packet = new FSAnnouncement(cache.size()).build();
        packet.setAddress(gateway);
        packet.setPort(port);
        socket.send(packet);
    }

    private void accepted() throws IOException {
        byte[] data = new byte[MTU];
        DatagramPacket p = new DatagramPacket(data, MTU);
        socket.receive(p);
        FSChunk aws = FSParser.parse(p);
        switch (aws.getType()) {
            case Accepted -> accepted((FSAccept) aws);
            case Not_Acceptable -> Not_Acceptable((FSFail) aws);
            default -> throw new InvalidChunkException("Datagram is not a FSAnnounce answer");
        }
    }

    private void reAccept(FSAccept msg) {
        this.setMTU(msg.getMaxFrameSize());
    }

    private void accepted(FSAccept msg) {
        this.setMTU(msg.getMaxFrameSize());
        this.timer.scheduleAtFixedRate(pinger, msg.getWarningTimer(), msg.getWarningTimer());
        System.out.println("Connection established with " + msg.getFather());
    }

    private void Not_Acceptable(FSFail aws) {
        System.out.println("Server Refused Connection");
        System.out.println("Error Message:\n" + aws.getDescription());
    }

    private void answer() {
        try {
            FSChunk request = input.poll();
            switch (request.getType()) {
                case Bye, Gateway_Timeout, Not_Acceptable -> socket.close();
                case GET -> sendFile(((FSGet) request).getMeta());
                case Accepted -> reAccept((FSAccept) request);
                case File_Cache -> {
                    updateFiles();
                    sendCache();
                }
            }
        } catch (IOException | InterruptedException ignored) {
        }
    }

    private void acknowledge(FSChunk ack) {
        if (ack.getType() == FSCType.Ping_Response) {
            pinger.pingReceived();
        } else {
            forwarder.acknowledge(ack);
        }
    }

    public void sendCache() throws IOException {
        FSCache res = new FSCache(new ArrayList<>(cache.keySet()));

        forwarder.clearCache();

        for (int i = 0; res.size() > 0; i++) {
            DatagramPacket bite = res.bite(MTU, i);
            forwarder.pushCacheDatagram(i, bite);
        }
    }

    public void updateFiles() throws IOException {
        Queue<File> queue = new ArrayDeque<>();
        queue.add(root.toFile());

        while (!queue.isEmpty()) {
            File current = queue.remove();

            if (current.isDirectory())
                queue.addAll(Arrays.asList(Objects.requireNonNull(current.listFiles())));

            else if (current.isFile()) {
                BasicFileAttributes attr = Files.readAttributes(current.toPath(), BasicFileAttributes.class);
                cache.putIfAbsent(
                        //KEY
                        new FileMetaData(
                                current.getName(),
                                attr.size(),
                                attr.lastModifiedTime().toMillis()
                        ),
                        //Value
                        current.toPath());
            }
        }
    }

    public void sendFile(FileMetaData request) throws IOException {

        Path file = cache.get(request);
        BufferedInputStream res = null;
        try {
            res = new BufferedInputStream(Files.newInputStream(file));
        } catch (Exception e) {
            file = null;
        }

        if (file == null) {
            forwarder.sendChunk(new FSNotFound(request));
            return;
        }

        forwarder.clearData();

        int frameSize = MTU - FSData.HeaderSize;
        byte[] data = new byte[frameSize];
        int offset = 1, i;

        i = res.read(data);

        FSData current = new FSData(0, data, i);
        while (res.available() > 0) {
            forwarder.pushData(current.getOffset(), current.build());
            i = res.read(data);
            current = new FSData(offset++, data, i);
        }
        current.makeLast();
        forwarder.pushData(current.getOffset(), current.build());
    }

}
