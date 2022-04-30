package FastFileServer;

import FileServerProtocol.FileServerChunk.FSChunk;
import FileServerProtocol.FileServerChunk.FSPing;

import java.util.TimerTask;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

public class Pinger extends TimerTask {

    Lock lock = new ReentrantLock();
    Condition cond = lock.newCondition();
    boolean waitingForPing;
    long timeOut;
    Consumer<FSChunk> sender;
    Runnable FFSKill;

    public Pinger(Consumer<FSChunk> sender, Runnable FFSKill, long TimeOut) {
        this.sender = sender;
        this.timeOut = TimeOut;
        this.FFSKill = FFSKill;
    }

    public void pingReceived() {
        try {
            lock.lock();
            waitingForPing = false;
        } finally {
            cond.signal();
            lock.unlock();
        }
    }

    @Override
    public void run() {
        try {
            lock.lock();
            waitingForPing = true;
            int timeOutCount = 0;
            while (waitingForPing) {
                try {
                    sender.accept(new FSPing());
                    if (cond.awaitNanos(timeOut) < 0)
                        if (timeOutCount++ >= FFSProperties.maxPingAttempts)
                        {
                            System.out.println("Server Connection Lost! Exiting.");
                            FFSKill.run();
                            return;
                        }
                } catch (InterruptedException e) {
                    return;
                }
            }
        } finally {
            lock.unlock();
        }
    }
}
