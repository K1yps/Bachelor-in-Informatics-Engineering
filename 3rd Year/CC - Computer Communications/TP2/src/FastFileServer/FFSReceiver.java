package FastFileServer;

import FileServerProtocol.FileServerChunk.FSAcknowledgement;
import FileServerProtocol.FileServerChunk.FSChunk;
import FileServerProtocol.Structs.FSParser;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.function.Consumer;

public class FFSReceiver implements Runnable {
    private int MTU; // Default
    DatagramSocket socket;
    Consumer<FSChunk> acknowledger;
    Consumer<FSChunk> receiver;

    public FFSReceiver(DatagramSocket socket, int MTU, Consumer<FSChunk> acknowledger, Consumer<FSChunk> receiver) {
        this.socket = socket;
        this.MTU = MTU;
        this.acknowledger = acknowledger;
        this.receiver = receiver;
    }

    public int getMTU() {
        return MTU;
    }

    public void setMTU(int MTU) {
        this.MTU = MTU;
    }

    @Override
    public void run() {
        while (!socket.isClosed())
            try {
                byte[] data = new byte[MTU];
                DatagramPacket p = new DatagramPacket(data, MTU);
                socket.receive(p);
                FSChunk request = FSParser.parse(p);

                if (request instanceof FSAcknowledgement)
                    acknowledger.accept(request);
                else
                    receiver.accept(request);
            } catch (IOException ignored) {
            }
    }
}
