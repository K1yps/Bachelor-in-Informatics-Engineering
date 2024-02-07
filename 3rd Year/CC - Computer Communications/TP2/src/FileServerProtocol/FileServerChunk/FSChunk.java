package FileServerProtocol.FileServerChunk;

import FileServerProtocol.Structs.FSCType;
import FileServerProtocol.Structs.ServerAddress;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;

public abstract class FSChunk {
    public static int HeaderSize = (Integer.SIZE) / 8;
    protected FSCType type;
    ServerAddress father; // This is only available if the object was a parsed from a Datagram packet

    protected FSChunk(FSCType type) {
        this.type = type;
        father = null;
    }

    protected FSChunk(FSCType type, ServerAddress father) {
        this.type = type;
        this.father = father;
    }

    public FSCType getType() {
        return type;
    }

    public DatagramPacket build() throws IOException {
        ByteArrayOutputStream st = new ByteArrayOutputStream();
        DataOutputStream dataStream = new DataOutputStream(st);
        this.write(dataStream);
        byte[] data = st.toByteArray();
        dataStream.close();
        st.close();
        return new DatagramPacket(data, data.length);
    }

    protected void write(DataOutputStream input) throws IOException {
        input.writeInt(type.getCode());
    }

    public ServerAddress getFather() {
        return father;
    }

    public void setFather(ServerAddress father) {
        this.father = father;
    }

    public boolean isAcknowledgeable() {
        return false;
    }

    public FSChunk acknowledge() {
        return null;
    }


    @Override
    public String toString() {
        return "FSChunk{" +
                "type=" + type +
                ", father=" + father +
                '}';
    }

    public abstract String toString(boolean extend);

}
