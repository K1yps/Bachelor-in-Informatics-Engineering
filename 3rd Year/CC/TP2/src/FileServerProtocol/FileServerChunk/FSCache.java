package FileServerProtocol.FileServerChunk;

import FileServerProtocol.Structs.FSCType;
import FileServerProtocol.Structs.FileMetaData;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.List;

public class FSCache extends FSChunk {

    int id;
    List<FileMetaData> cache;

    public FSCache(List<FileMetaData> cache) {
        super(FSCType.File_Cache);
        this.id = -1;
        this.cache = cache;
    }

    public FSCache(int id, List<FileMetaData> cache) {
        super(FSCType.File_Cache);
        this.cache = cache;
        this.id = id;
    }

    public static FSCache read(DataInputStream input) throws IOException {

        int id = input.readInt();
        List<FileMetaData> arr = new ArrayList<>();

        FileMetaData m;

        while ((m = FileMetaData.deserialize(input)) != null)
            arr.add(m);

        return new FSCache(id, arr);
    }

    public List<FileMetaData> getCache() {
        return cache;
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
        for (FileMetaData f : cache) {
            try {
                f.serialize(input);
            } catch (IOException ignored) {
            }
        }
    }

    public int size() {
        return cache.size();
    }

    public DatagramPacket bite(int size, int id_) throws IOException {
        ByteArrayOutputStream res = new ByteArrayOutputStream();
        DataOutputStream dataStream = new DataOutputStream(res);
        super.write(dataStream);
        if (id_ < 0)
            dataStream.writeInt(this.id);
        else
            dataStream.writeInt(id_);

        while (cache.size() > 0 && res.size() + cache.get(0).serSize() < size) {
            try {
                cache.remove(0).serialize(dataStream);
            } catch (IOException ignored) {
            }
        }

        dataStream.close();
        res.close();
        byte[] arr = res.toByteArray();
        return new DatagramPacket(arr, arr.length);
    }

    public boolean isEmpty() {
        return cache.isEmpty();
    }

    @Override
    public String toString() {
        StringBuilder bd = new StringBuilder();
        bd.append("FSCache:\n");
        for (FileMetaData f : cache)
            bd.append("\t").append(f.toString()).append("\n");
        return bd.toString();
    }

    @Override
    public boolean isAcknowledgeable() {
        return true;
    }

    @Override
    public FSChunk acknowledge() {
        return new FSCacheAck(id);
    }

    @Override
    public String toString(boolean extend) {
        if (extend)
            return this.toString();
        else
            return "FSCache {#FileEntrys = " + cache.size() + " }";
    }
}
