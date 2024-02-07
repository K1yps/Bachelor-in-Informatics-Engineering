package FileServerGateway.Workers;

import FileServerGateway.FSGProperties;
import FileServerGateway.HTTPExchanger;
import FileServerProtocol.FileServerChunk.FSChunk;
import FileServerProtocol.FileServerChunk.FSData;
import FileServerProtocol.FileServerChunk.FSGet;
import FileServerProtocol.Structs.FileMetaData;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

public class FileRequest implements Runnable {
    Lock lock = new ReentrantLock();
    Condition cond = lock.newCondition();
    int status = 0, count = 0, last = Integer.MAX_VALUE;

    Consumer<FSChunk> sender;

    Map<Integer, FSData> chunks = new TreeMap<>();

    FileMetaData target;
    HttpExchange output;


    public FileRequest(HttpExchange exchange, FileMetaData file, Consumer<FSChunk> sender) {
        this.output = exchange;
        this.target = file;
        this.sender = sender;
    }


    public void receiveOK(FSChunk chunk) {
        try {
            lock.lock();
            FSData res = (FSData) chunk;
            if (count <= res.getOffset()) {
                if (res.isLast()) {
                    status = 1;
                    last = res.getOffset() + 1;
                }
                chunks.putIfAbsent(res.getOffset(), res);
            } //else throw it away

        } finally {
            cond.signal();
            lock.unlock();
        }

    }

    public void interrupt() {
        chunks.clear();
    }

    private void setHeaders() {
        Headers h = output.getResponseHeaders();
        HTTPExchanger.contextType(h, target.getExtension());
        HTTPExchanger.date(h);
        HTTPExchanger.lastModified(h, target.modifiedTime);
        HTTPExchanger.setContentLength(h, target.size);
        HTTPExchanger.global(h);
    }


    @Override
    public void run() {
        if (status != 0) return;
        sender.accept(new FSGet(target));
        try {
            lock.lock();

            if (FSGProperties.GetTimeout > 0)
                while (status == 0 && chunks.isEmpty()) {
                    if (cond.awaitNanos(FSGProperties.GetTimeout) < 0)
                        status = -2;
                }

            this.setHeaders();
            output.sendResponseHeaders(200, target.size);
            OutputStream out = output.getResponseBody();
            while (count < last) {
                FSData data = chunks.remove(count);
                if (data == null)
                    cond.await();
                else {
                    out.write(data.getData());
                    count++;
                }
            }
            out.close();
            chunks.clear();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            status = -3;
            System.out.println("I/O error while sending file: " + target.file + "  Error message: " + e.getMessage());
        } finally {
            lock.unlock();
        }
    }

    public void cancel() {
        try {
            lock.lock();
            this.status = -1;
        } finally {
            cond.signal();
            lock.unlock();
        }
    }

    public boolean failed() {
        try {
            lock.lock();
            return status < 0;
        } finally {
            lock.unlock();
        }
    }
}
