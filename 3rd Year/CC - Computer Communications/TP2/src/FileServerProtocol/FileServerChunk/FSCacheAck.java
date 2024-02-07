package FileServerProtocol.FileServerChunk;

import FileServerProtocol.Structs.FSCType;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class FSCacheAck extends FSChunk implements FSAcknowledgement {

    int id;

    protected FSCacheAck(int id) {
        super(FSCType.Cache_Acknowledgement);
        this.id = id;
    }

    public static FSCacheAck read(DataInputStream in) throws IOException {
        return new FSCacheAck(in.readInt());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    protected void write(DataOutputStream input) throws IOException {
        super.write(input);
        input.writeInt(id);
    }

    @Override
    public String toString() {
        if (father == null)
            return "Cache_ACK{ id: " + id + " - emission}";
        else return type.toString() + "id: " + id + " - reception";
    }

    @Override
    public String toString(boolean extend) {
        if (extend)
            return this.toString();
        return "Cache_ACK{ id: " + id + "}";
    }
}
