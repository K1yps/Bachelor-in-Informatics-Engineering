package FileServerProtocol.FileServerChunk;

import FileServerProtocol.Structs.FSCType;
import FileServerProtocol.Structs.ServerAddress;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;

import static FileServerProtocol.Structs.FSCType.Ping_Request;
import static FileServerProtocol.Structs.FSCType.Ping_Response;

public class FSPing extends FSChunk implements FSAcknowledgement {


    public FSPing() {
        super(Ping_Request);
    }

    protected FSPing(FSCType type) {
        super(type);
    }

    public FSPing(ServerAddress father) {
        super(Ping_Request, father);
    }

    public static FSPing read(FSCType type) {
        return new FSPing(type);
    }

    public DatagramPacket respond() throws IOException {
        this.type = Ping_Response;
        return this.build();
    }

    public FSPing prepare() {
        this.type = Ping_Response;
        return this;
    }

    @Override
    protected void write(DataOutputStream input) throws IOException {
        super.write(input);
    }

    @Override
    public boolean isAcknowledgeable() {
        return true;
    }

    @Override
    public FSChunk acknowledge() {
        return new FSPing(Ping_Response);
    }

    @Override
    public String toString() {
        if (father == null)
            return "FSPing: " + type.toString() + " emission";
        else return "FSPing: " + type.toString() + " from " + father.toString();
    }

    @Override
    public String toString(boolean extend) {
        if (extend)
            return this.toString();
        else
            return "FSPing: " + type.toString();
    }
}
