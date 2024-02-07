package FileServerGateway.Workers;

import FileServerGateway.FSGProperties;
import FileServerProtocol.FileServerChunk.FSChunk;
import FileServerProtocol.FileServerChunk.FSFail;
import FileServerProtocol.Structs.FSCType;
import FileServerProtocol.Structs.FileMetaData;
import FileServerProtocol.Structs.ServerAddress;
import Utils.BoundedQueue;
import com.sun.net.httpserver.HttpExchange;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.NoSuchElementException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ServerHandler implements Runnable {

    Thread worker = null;
    int clock;
    ServerAddress address;
    DatagramSocket out;
    BoundedQueue<FileRequest> queue = new BoundedQueue<>();
    //Ticket System
    Lock ticketLock = new ReentrantLock();
    Condition ticketCond = ticketLock.newCondition();
    int ticket = 0;
    int ticketCount = 1;

    //---- Constructor ------------------------------------------------------------------------------------------------

    public ServerHandler(DatagramSocket out, ServerAddress address, int clock) {
        this.out = out;
        this.address = address;
        this.clock = clock;
    }

    //---- Run and Thread Management ----------------------------------------------------------------------------------

    @Override
    public void run() {
        while (true) {
            try {
                FileRequest current = queue.peek();
                current.run();
                queue.remove();
            } catch (InterruptedException e) {
                break;
            } finally {
                nextTicket();
            }
        }
    }

    public Thread buildWorker() {
        if (this.worker == null) {
            this.worker = new Thread(this);
            worker.setName(address + "'s Handler");
        }
        return worker;
    }

    public void kill() {
        if (this.worker != null)
            worker.interrupt();
        try {
            out.send(new FSFail(FSCType.Bye).build());
        } catch (IOException ignored) {
        }
    }

    //---- Getters -----------------------------------------------------------------------------------------------------


    public ServerAddress getAddress() {
        return address;
    }

    public int getClock() {
        return clock;
    }

    public int getQueueSize() {
        return queue.size();
    }

    public boolean isOutDated(int timeReference) {
        return (timeReference - clock) > FSGProperties.ServerTolerance;
    }

    //---- Setters -----------------------------------------------------------------------------------------------------


    public void setClock(int clock) {
        this.clock = clock;
    }

    //---- Ticket Management ------------------------------------------------------------------------------------------

    private void nextTicket() {
        try {
            ticketLock.lock();
            ticket++;
        } finally {
            ticketCond.signalAll();
            ticketLock.unlock();
        }
    }

    private void waitForTicket(int ticket) throws InterruptedException {
        try {
            ticketLock.lock();
            while (this.ticket < ticket)
                ticketCond.await();
        } finally {
            ticketLock.unlock();
        }
    }

    //---- Request Management -----------------------------------------------------------------------------------------

    public void send(FSChunk chunk) {
        DatagramPacket packet;
        try {
            packet = chunk.build();
            packet.setAddress(address.address);
            packet.setPort(address.port);
            out.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (FSGProperties.seeOutgoingFrames)
            System.out.println("Sent: " + chunk.toString(FSGProperties.extendFrameOutput));
    }

    public void GET(HttpExchange exchange, FileMetaData file) throws FileNotFoundException {
        FileRequest request = new FileRequest(exchange, file, this::send);
        try {
            queue.add(request, ticketLock::lock);
            this.waitForTicket(ticketCount++);
        } catch (InterruptedException e) {
            throw new FileNotFoundException("Something went wrong getting the file");
        } finally {
            ticketLock.unlock();
        }
        if (request.failed())
            throw new FileNotFoundException("Something went wrong getting the file : " + file.file);
    }

    //---- FSChunk Management -----------------------------------------------------------------------------------------


    public void receiveNotFound(FSChunk chunk) {
        try {
            queue.element().cancel();
        } catch (NoSuchElementException | NullPointerException ignored) {
            //Discard
        }
    }

    public void receiveOK(FSChunk chunk) {
        try {
            FileRequest request = queue.element();
            queue.element().receiveOK(chunk);
        } catch (NoSuchElementException ignored) {
            //Discard
        }
    }

    public void reset() {
        queue.forEachAndClear(FileRequest::cancel);
    }
}
