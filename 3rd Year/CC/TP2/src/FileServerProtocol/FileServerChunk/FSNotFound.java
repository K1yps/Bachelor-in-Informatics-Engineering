package FileServerProtocol.FileServerChunk;

import FileServerProtocol.Structs.FSCType;
import FileServerProtocol.Structs.FileMetaData;

import java.io.DataInputStream;

public class FSNotFound extends FSGet {

    public FSNotFound(String file, long size, long modifiedDate) {
        super(file, size, modifiedDate);
        this.type = FSCType.Not_Found;
    }

    public FSNotFound(FileMetaData fileMeta) {
        super(fileMeta);
        this.type = FSCType.Not_Found;
    }


    public static FSNotFound read(DataInputStream input) {
        FSGet res = FSGet.read(input);
        res.type = FSCType.Not_Found;
        return (FSNotFound) res;
    }


    @Override
    public String toString() {
        return "FSNotFound{" +
                super.toString() +
                '}';
    }

    @Override
    public String toString(boolean extend) {
        return "FSNotFound{" + super.toString(extend) + '}';
    }
}
