package FileServerGateway.Workers;

import FileServerProtocol.Structs.FileMetaData;
import FileServerProtocol.Structs.ServerAddress;
import Utils.Pair;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class CacheCleaner implements Runnable {
    Queue<ServerAddress> pedidos = new ArrayDeque<>();
    Queue<Pair<FileMetaData, ServerAddress>> pedidosFile = new ArrayDeque<>();
    Lock lock = new ReentrantLock();
    Condition cond = lock.newCondition();
    BiConsumer<FileMetaData, ServerAddress> removeServerFromFile;
    Consumer<ServerAddress> removeServer;

    public CacheCleaner(BiConsumer<FileMetaData, ServerAddress> removeServerFromFile, Consumer<ServerAddress> removeServer) {
        this.removeServerFromFile = removeServerFromFile;
        this.removeServer = removeServer;
    }

    public void newPedido(FileMetaData f, ServerAddress s) {
        try {
            lock.lock();
            pedidosFile.add(new Pair<>(f, s));
        } finally {
            cond.signal();
            lock.unlock();
        }

    }

    public void newPedido(ServerAddress s) {
        try {
            lock.lock();
            pedidos.add(s);
        } finally {
            cond.signal();
            lock.unlock();
        }

    }


    @Override
    public void run() {
        while (true) {
            try {
                lock.lock();
                while (pedidos.isEmpty() || pedidosFile.isEmpty())
                    cond.await();

                ServerAddress s;
                Pair<FileMetaData, ServerAddress> p;

                while ((s = pedidos.poll()) != null) {
                    removeServer.accept(s);
                }

                while ((p = pedidosFile.poll()) != null) {
                    removeServerFromFile.accept(p.fst, p.snd);
                }
            } catch (InterruptedException e) {
                break;
            } finally {
                lock.unlock();
            }

        }


    }
}
