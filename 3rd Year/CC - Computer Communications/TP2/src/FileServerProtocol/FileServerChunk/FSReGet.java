package FileServerProtocol.FileServerChunk;

import FileServerProtocol.Structs.FileMetaData;
import FileServerProtocol.Structs.ServerAddress;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static FileServerProtocol.Structs.FSCType.ReGET;

public class FSReGet extends FSChunk {
    List<Integer> frames = new ArrayList<>();
    int last = -1;
    private FileMetaData meta;

    protected FSReGet() {
        super(ReGET);
    }

    public FSReGet(FileMetaData meta) {
        super(ReGET);
        this.meta = meta;
    }

    public FSReGet(ServerAddress father, FileMetaData meta) {
        super(ReGET, father);
        this.meta = meta;
    }

    public static FSReGet read(DataInputStream input) throws IOException {
        FSReGet res = new FSReGet();
        res.meta = FileMetaData.deserialize(input);

        res.last = input.readInt();
        int m;
        while (true)
            try {
                res.frames.add(input.readInt());
            } catch (EOFException e) {
                break;
            }

        return res;
    }

    public List<Integer> getFrames() {
        return frames;
    }

    public int getLast() {
        return last;
    }

    public void setLast(int last) {
        this.last = last;
    }

    public FileMetaData getMeta() {
        return meta;
    }

    @Override
    protected void write(DataOutputStream input) throws IOException {
        super.write(input);
        meta.serialize(input);

        input.write(last);

        for (Integer i : frames)
            input.write(i);


    }

    @Override
    public String toString() {

        StringBuilder bd = new StringBuilder();
        bd.append("FSReGet (DEPRECATED): \n ");
        for (Integer f : frames)
            bd.append("\t").append(f.toString()).append("\n");


        return bd.append(", last=").append(last).append(", meta=").append(meta).toString();
    }

    @Override
    public String toString(boolean extend) {
        return "FSReGet (DEPRECATED)";
    }
}
