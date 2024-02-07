package FileServerGateway.Workers;

import FileServerGateway.FSGProperties;
import FileServerProtocol.Exceptions.BadChecksumException;
import FileServerProtocol.Exceptions.InvalidChunkException;
import FileServerProtocol.FileServerChunk.FSChunk;
import FileServerProtocol.Structs.FSCType;
import FileServerProtocol.Structs.FSParser;
import FileServerProtocol.Structs.ServerAddress;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Dispatcher implements Runnable {
    private final DatagramSocket socket;
    private final Map<ServerAddress, List<FileRequest>> dataBuilders = new HashMap<>();
    private Predicate<ServerAddress> isLogged;
    private Consumer<FSChunk> globalConsumer;
    private Consumer<FSChunk> serverConsumer;
    private Consumer<FSChunk> unLoggedConsumer;

    public Dispatcher(DatagramSocket socket) {
        this.socket = socket;
    }

    public Dispatcher(DatagramSocket socket,
                      Predicate<ServerAddress> isLogged,
                      Consumer<FSChunk> globalConsumer,
                      Consumer<FSChunk> serverConsumer,
                      Consumer<FSChunk> unLoggedConsumer
    ) {
        this.socket = socket;
        this.isLogged = isLogged;
        this.globalConsumer = globalConsumer;
        this.serverConsumer = serverConsumer;
        this.unLoggedConsumer = unLoggedConsumer;
    }

    public void setIsLogged(Predicate<ServerAddress> isLogged) {
        this.isLogged = isLogged;
    }

    public void setGlobalConsumer(Consumer<FSChunk> globalConsumer) {
        this.globalConsumer = globalConsumer;
    }

    public void setServerConsumer(Consumer<FSChunk> serverConsumer) {
        this.serverConsumer = serverConsumer;
    }

    public void setUnLoggedConsumer(Consumer<FSChunk> unLoggedConsumer) {
        this.unLoggedConsumer = unLoggedConsumer;
    }

    @Override
    public void run() {
        while (!socket.isClosed()) {
            byte[] r = new byte[FSGProperties.MTU];
            DatagramPacket p = new DatagramPacket(r, r.length);
            FSChunk chunk = null;
            try {
                socket.receive(p);
                chunk = FSParser.parse(p);

                if (FSGProperties.seeIncomingFrames)
                    System.out.println("Received : " + chunk.toString(FSGProperties.extendFrameOutput));

                if (isLogged.test(chunk.getFather())) {
                    if (chunk.getType() == FSCType.File_Cache)
                        globalConsumer.accept(chunk);
                    else serverConsumer.accept(chunk);
                } else unLoggedConsumer.accept(chunk);

                if (chunk.isAcknowledgeable()) {
                    DatagramPacket ack = chunk.acknowledge().build();
                    ack.setAddress(p.getAddress());
                    ack.setPort(p.getPort());
                    socket.send(ack);
                    if (FSGProperties.seeOutgoingAcknowledgements)
                        System.out.println("Acknowledgement sent: " + chunk.toString(FSGProperties.extendFrameOutput));
                }

            } catch (InvalidChunkException | BadChecksumException discarded) {
                /*DISCARD*/
                if (FSGProperties.seeIncomingFrames && chunk != null)
                    System.out.println("Frame Discarded :" + chunk.toString(FSGProperties.extendFrameOutput) +
                            " Reason: " + discarded.getMessage()
                    );
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
