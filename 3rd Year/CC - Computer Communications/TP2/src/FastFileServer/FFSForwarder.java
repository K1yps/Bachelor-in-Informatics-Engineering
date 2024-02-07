package FastFileServer;

import FileServerProtocol.FileServerChunk.FSCacheAck;
import FileServerProtocol.FileServerChunk.FSChunk;
import FileServerProtocol.FileServerChunk.FSDataAck;
import Utils.Pair;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FFSForwarder implements Runnable {
    private final DatagramSocket socket;
    private final InetAddress gateway;
    private final int port;
    private final Map<Integer, Pair<Boolean, DatagramPacket>> dataBuff = new HashMap<>();
    private final Map<Integer, Pair<Boolean, DatagramPacket>> cacheBuff = new HashMap<>();
    private final Lock lock = new ReentrantLock();
    private final Condition cond = lock.newCondition();
    private final Timer timerOuter = new Timer("FFSForwarder_TimerOuter");
    private final long ackTimeout;
    private final int maxDataSize;
    private int threshold = 1, duplicates = 0;
    private boolean inCongestionAvoidance = false;

    public FFSForwarder(DatagramSocket socket, InetAddress gateway, int port, int maxDataSize, long ackTimeout) {
        this.socket = socket;
        this.gateway = gateway;
        this.port = port;
        this.maxDataSize = maxDataSize;
        this.ackTimeout = ackTimeout;
    }

    public void pushData(int offset, DatagramPacket data) {
        this.bindDatagram(data);
        try {
            lock.lock();
            while (threshold < (dataBuff.size() + cacheBuff.size()))
                cond.await();
            dataBuff.put(offset, new Pair<>(false, data));
            send(data);
        } catch (InterruptedException ignored) {
        } finally {
            lock.unlock();
        }
    }

    public void clearData() {
        try {
            lock.lock();
            dataBuff.clear();
        } finally {
            cond.signalAll();
            lock.unlock();
        }
    }


    public void pushCacheDatagram(int id, DatagramPacket cache) {
        this.bindDatagram(cache);
        try {
            lock.lock();
            while (threshold < (dataBuff.size() + cacheBuff.size()))
                cond.await();
            cacheBuff.put(id, new Pair<>(false, cache));
            send(cache);
        } catch (InterruptedException ignored) {
        } finally {
            lock.unlock();
        }
        if (FFSProperties.seeOutgoingFrames)
            System.out.println("Cache Datagram number " + id + " is being transmited");
    }

    public void clearCache() {
        try {
            lock.lock();
            dataBuff.clear();
        } finally {
            cond.signalAll();
            lock.unlock();
        }
    }


    public int popDuplicates() {
        int res = duplicates;
        duplicates = 0;
        return res;
    }

    public void acknowledge(FSChunk ack) {
        try {
            Pair<Boolean, DatagramPacket> r;

            lock.lock();
            switch (ack.getType()) {
                case Data_Acknowledgement -> r = dataBuff.remove(((FSDataAck) ack).getOffset());
                case Cache_Acknowledgement -> r = cacheBuff.remove(((FSCacheAck) ack).getId());
                default -> { /*Discard*/
                    return;
                }
            }
            if (r == null)//If there wasn't a frame waiting it's a duplicate ACK
                duplicates++;
        } finally {
            cond.signalAll();
            lock.unlock();
        }
    }

    public void resolveThreshold(int timeoutCount, int dupCount) {
        if (timeoutCount >= FFSProperties.timeoutThreshold) {
            this.threshold = 1;
            inCongestionAvoidance = false;
        } else if (dupCount >= FFSProperties.dupThreshold) {
            this.threshold /= 2;
            if (threshold < 1) threshold = 1;
        } else if (inCongestionAvoidance || (inCongestionAvoidance = threshold >= FFSProperties.congestionAvoidanceSwitch))
            threshold++;
        else threshold *= 2;
        if (maxDataSize > 0 && this.threshold > maxDataSize) threshold = maxDataSize;
        if (FFSProperties.seeThresholdResolve)
            System.out.println("Threshold Resolve:  #Timeouts: " + timeoutCount + " #Duplicates: " + dupCount + " Threshold set to: " + threshold);
    }


    public void timeOut() {
        try {
            lock.lock();
            int count = 0, i = 0;
            for (Pair<Boolean, DatagramPacket> p : dataBuff.values())
                if (count++ < threshold)
                    if (p.fst) {
                        send(p.snd);
                        i++;
                    } else p.setFst(true);
                else break;
            for (Pair<Boolean, DatagramPacket> p : cacheBuff.values())
                if (count++ < threshold)
                    if (p.fst) {
                        send(p.snd);
                        i++;
                    } else p.setFst(true);
                else break;
            resolveThreshold(i, popDuplicates());
        } finally {
            lock.unlock();
        }
    }


    public void send(DatagramPacket p) {
        try {
            socket.send(p);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void bindDatagram(DatagramPacket packet) {
        packet.setAddress(gateway);
        packet.setPort(port);
    }

    public void sendChunk(FSChunk chunk) throws IOException {
        DatagramPacket packet = chunk.build();
        this.bindDatagram(packet);
        socket.send(packet);
    }

    public void start() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                timeOut();
            }
        };
        timerOuter.scheduleAtFixedRate(task, ackTimeout, ackTimeout);
    }

    @Override
    public void run() {
        this.start();
    }

    public void cancel() {
        timerOuter.cancel();
    }
}
