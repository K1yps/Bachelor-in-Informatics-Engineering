package FileServerProtocol.FileServerChunk;

import FileServerProtocol.Exceptions.InvalidChunkException;
import FileServerProtocol.Structs.FSCType;
import FileServerProtocol.Structs.FileMetaData;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class FSGet extends FSChunk {
    private final FileMetaData meta;

    public FSGet(String file, long size, long modifiedDate) {
        super(FSCType.GET);
        meta = new FileMetaData(file, size, modifiedDate);
    }

    public FSGet(FileMetaData fileMeta) {
        super(FSCType.GET);
        meta = fileMeta;
    }

    public static FSGet read(DataInputStream input) throws InvalidChunkException {
        return new FSGet(FileMetaData.deserialize(input));
    }

    public FileMetaData getMeta() {
        return meta;
    }

    @Override
    protected void write(DataOutputStream input) throws IOException {
        super.write(input);
        try {
            meta.serialize(input);
        } catch (IOException ignored) {
        }
    }

    @Override
    public String toString() {
        return "FSGet{" +
                meta +
                '}';
    }

    @Override
    public String toString(boolean extend) {
        if (extend)
            return this.toString();
        else
            return "FSGet{ File: " + meta.file + '}';
    }
}
