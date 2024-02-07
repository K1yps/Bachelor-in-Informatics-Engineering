package FileServerGateway;


import FileServerGateway.Workers.*;
import FileServerProtocol.FileServerChunk.FSAccept;
import FileServerProtocol.FileServerChunk.FSCache;
import FileServerProtocol.FileServerChunk.FSChunk;
import FileServerProtocol.FileServerChunk.FSFail;
import FileServerProtocol.Structs.FSCType;
import FileServerProtocol.Structs.FileMetaData;
import FileServerProtocol.Structs.ServerAddress;
import Utils.BoundedQueue;
import Utils.Pair;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.URI;
import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ServerManager implements Runnable {
    private final DatagramSocket socket;

    ReadWriteLock FClock = new ReentrantReadWriteLock(); //File Cache Lock
    ReadWriteLock SClock = new ReentrantReadWriteLock(); //Server Cache Lock
    ReadWriteLock Slock = new ReentrantReadWriteLock();  //Server Lock
    private final Map<String, FileMetaData> FileCache = new HashMap<>();
    private final Map<FileMetaData, Set<ServerAddress>> ServerCache = new HashMap<>();
    private final Map<ServerAddress, ServerHandler> Servers = new HashMap<>();

    private final BoundedQueue<FSCache> FSCacheBuffer = new BoundedQueue<>();
    CacheCleaner cleaner = new CacheCleaner(this::removeServerFromFile, this::removeServer);
    CyclicClock cyclicClock = new CyclicClock(this::pingAll);

    //Threads
    private final Thread CacheWorker, Dispatcher, CleanerThread;
    private final Timer timer;

    //---- Constructor ------------------------------------------------------------------------------------------------


    public ServerManager(DatagramSocket socket) {
        this.socket = socket;
        //Bounded Buffers
        Dispatcher = new Thread(
                new Dispatcher(socket, this::isLoggedIn, this::receiveGlobal, this::receiveServer, this::processUnLogged));
        Dispatcher.setName("FFServer-Dispatcher");
        CacheWorker = new Thread(new CacheUpdater(FSCacheBuffer, this::addCacheEntries));
        CacheWorker.setName("FSCacheWorker");
        CleanerThread = new Thread(cleaner);
        CleanerThread.setName("FSCacheCleaner");
        timer = new Timer();
    }

    //---- Run --------------------------------------------------------------------------------------------------------


    @Override
    public void run() {
        CacheWorker.start();
        Dispatcher.start();
        CleanerThread.start();
        timer.scheduleAtFixedRate(cyclicClock, 0L, FSGProperties.PingTimer);
    }

    //---- Dispatching Methods ----------------------------------------------------------------------------------------

    private boolean isLoggedIn(ServerAddress address) {
        try {
            Slock.readLock().lock();
            return Servers.containsKey(address);
        } finally {
            Slock.readLock().unlock();
        }

    }

    private void receiveGlobal(FSChunk chunk) {
        if (chunk.getType() == FSCType.File_Cache)
            FSCacheBuffer.add((FSCache) chunk);
    }

    public void receiveServer(FSChunk data) {
        ServerHandler handler = null;
        try {
            Slock.readLock().lock();
            handler = Servers.get(data.getFather());
        } catch (Exception ignored) {
        } finally {
            Slock.readLock().unlock();
        }
        if (handler != null)
            switch (data.getType()) {
                case Ping_Request -> handler.setClock(cyclicClock.getClock() + 1);
                case OK -> handler.receiveOK(data);
                case Announcement -> {
                    try {
                        accept(handler.getAddress());
                    } catch (IOException ignored) {
                    }
                    handler.reset();
                }
                case Not_Found -> handler.receiveNotFound(data);
                default -> {/*discard*/}
            }
    }

    public void processUnLogged(FSChunk data) {
        if (data.getType() == FSCType.Announcement)
            try {
                Slock.writeLock().lock();
                if (Servers.size() <= FSGProperties.MaxServersCount && !FSGProperties.ServerBlackList.contains(data.getFather().address)) {
                    ServerHandler sh = new ServerHandler(socket, data.getFather(), cyclicClock.getClock());
                    Servers.putIfAbsent(data.getFather(), sh);
                    sh.buildWorker().start();
                    accept(data.getFather());
                } else reject(data.getFather());
            } catch (IOException ignored) {
            } finally {
                Slock.writeLock().unlock();
            }
    }

    //---- Cleaning Methods -------------------------------------------------------------------------------------------
    public void removeServerFromFile(FileMetaData f, ServerAddress s) {
        try {
            SClock.writeLock().lock();
            Set<ServerAddress> servers = ServerCache.get(f);
            if (servers != null) {
                servers.remove(s);
                if (servers.isEmpty()) {
                    ServerCache.remove(f);
                }
            }


        } finally {
            SClock.writeLock().unlock();
        }
    }

    public void removeServer(ServerAddress s) {
        try {
            FClock.writeLock().lock();
            SClock.writeLock().lock();
            for (Map.Entry<FileMetaData, Set<ServerAddress>> entry : ServerCache.entrySet()) {
                entry.getValue().remove(s);
                if (entry.getValue().isEmpty()) {
                    ServerCache.remove(entry.getKey());
                    FileCache.remove(entry.getKey().file);
                }
            }
        } finally {
            SClock.writeLock().unlock();
            FClock.writeLock().unlock();
        }
    }

    //---- Cache Management Methods -----------------------------------------------------------------------------------

    private Set<ServerAddress> getAddress(String filename) throws FileNotFoundException {
        try {
            FClock.readLock().lock();
            FileMetaData meta = FileCache.get(filename);
            SClock.readLock().lock();
            FClock.readLock().unlock();
            Set<ServerAddress> res = ServerCache.get(meta);
            SClock.readLock().unlock();
            return res;
        } catch (NullPointerException e) {
            throw new FileNotFoundException("File " + filename + " not found.");
        }
    }

    private Pair<FileMetaData, Set<ServerAddress>> getAddressAndMeta(String filename) throws FileNotFoundException {
        try {
            FClock.readLock().lock();
            FileMetaData meta = FileCache.get(filename);
            SClock.readLock().lock();
            FClock.readLock().unlock();
            Set<ServerAddress> res = ServerCache.get(meta);
            SClock.readLock().unlock();
            if (meta == null || res == null)
                throw new FileNotFoundException("File " + filename + " not found.");
            return new Pair<>(meta, res);
        } catch (NullPointerException e) {
            throw new FileNotFoundException("File " + filename + " not found.");
        }
    }

    public void addCacheEntries(FSCache f) {
        if (!f.isEmpty()) {
            ServerAddress server = f.getFather();
            FClock.writeLock().lock();
            SClock.writeLock().lock();
            for (FileMetaData file : f.getCache()) {
                try {
                    ServerCache.get(file).add(server);
                } catch (NullPointerException e) {
                    FileCache.put(file.file, file);
                    ServerCache.put(file, new HashSet<>(Collections.singleton(server)));
                }
            }
            SClock.writeLock().unlock();
            FClock.writeLock().unlock();
        }
    }

    //---- Server Management ------------------------------------------------------------------------------------------

    public void pingAll(Integer clock) {
        int time = cyclicClock.getClock();
        try {
            Slock.writeLock().lock();
            for (Map.Entry<ServerAddress, ServerHandler> s : Servers.entrySet()) {
                if (s.getValue().isOutDated(time)) {
                    s.getValue().kill();
                    Servers.remove(s.getKey());
                    cleaner.newPedido(s.getKey());
                }
            }
        } finally {
            Slock.writeLock().unlock();
        }
    }

    public void accept(ServerAddress server) throws IOException {
        FSChunk res1 = new FSAccept(FSGProperties.MTU, FSGProperties.PingTimer);
        if (FSGProperties.seeOutgoingFrames)
            System.out.println("Sent Accept: " + res1.toString(FSGProperties.extendFrameOutput));
        DatagramPacket res = res1.build();
        res.setAddress(server.address);
        res.setPort(server.port);
        socket.send(res);
    }

    public void reject(ServerAddress server) throws IOException {
        FSChunk res1 = new FSFail(FSCType.Not_Acceptable, "Server is at max capacity");
        if (FSGProperties.seeOutgoingFrames)
            System.out.println("Sent Not_Acceptable: " + res1.toString(FSGProperties.extendFrameOutput));
        DatagramPacket res = res1.build();
        res.setAddress(server.address);
        res.setPort(server.port);
        socket.send(res);
    }

    //---- HTTP Management --------------------------------------------------------------------------------------------

    public void GET(HttpExchange exchange) throws IOException {
        URI requestURI = exchange.getRequestURI();
        String path = requestURI.getPath();

        path = path.substring(1);

        Pair<FileMetaData, Set<ServerAddress>> servers;

        try {
            servers = getAddressAndMeta(path);
        } catch (FileNotFoundException e) {
            HTTPExchanger.notFound(exchange);
            return;
        }

        Iterator<ServerHandler> iter = servers.snd
                .stream()
                .map(Servers::get)
                .sorted(Comparator.comparingInt(ServerHandler::getQueueSize))
                .iterator();


        while (iter.hasNext()) {
            ServerHandler server = iter.next();
            try {
                server.GET(exchange, servers.fst);
                return;
            } catch (FileNotFoundException e) {
                cleaner.newPedido(servers.fst, server.getAddress());
            }
        }
        HTTPExchanger.notFound(exchange);
    }

    public void HEAD(HttpExchange exchange) throws IOException {
        Headers h = exchange.getResponseHeaders();

        URI requestURI = exchange.getRequestURI();
        String path = requestURI.getPath();
        path = path.substring(1);

        FileMetaData res;
        try {
            FClock.readLock().lock();
            res = FileCache.get(path);
        } finally {
            FClock.readLock().unlock();
        }
        if (res != null) {
            HTTPExchanger.contextType(h, res.getExtension());
            HTTPExchanger.date(h);
            HTTPExchanger.lastModified(h, res.modifiedTime);
            HTTPExchanger.setContentLength(h, res.size);
            HTTPExchanger.global(h);
        } else {
            HTTPExchanger.notFound(exchange);
        }
    }
}
