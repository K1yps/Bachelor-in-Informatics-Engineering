package FileServerProtocol.FileServerChunk;

import FileServerProtocol.Structs.FSCType;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class FSDataAck extends FSChunk implements FSAcknowledgement {
    int offset;


    protected FSDataAck() {
        super(FSCType.Data_Acknowledgement);
    }

    public FSDataAck(int o) {
        super(FSCType.Data_Acknowledgement);
        offset = o;
    }

    public static FSDataAck read(DataInputStream in) throws IOException {
        FSDataAck res = new FSDataAck();
        res.offset = in.readInt();
        return res;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    @Override
    protected void write(DataOutputStream input) throws IOException {
        super.write(input);
        input.writeInt(offset);
    }

    @Override
    public String toString() {
        return "FSDataAck{" +
                "offset=" + offset +
                '}';
    }

    @Override
    public String toString(boolean extend) {
        return this.toString();
    }
}
