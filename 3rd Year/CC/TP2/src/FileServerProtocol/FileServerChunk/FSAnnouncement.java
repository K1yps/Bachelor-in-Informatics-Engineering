package FileServerProtocol.FileServerChunk;

import FileServerProtocol.Structs.FSCType;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class FSAnnouncement extends FSChunk {
    int file_count;

    public FSAnnouncement(int file_count) {
        super(FSCType.Announcement);
        this.file_count = file_count;
    }

    public static FSAnnouncement read(DataInputStream input) throws IOException {
        int count = input.readInt();
        if (count < 0)
            count = 0;

        return new FSAnnouncement(count);
    }

    @Override
    protected void write(DataOutputStream input) throws IOException {
        super.write(input);
        new DataOutputStream(input).writeInt(file_count);

    }

    @Override
    public String toString() {
        return "FSAnnouncement{" +
                "file_count=" + file_count +
                '}';
    }

    @Override
    public String toString(boolean extend) {
        return this.toString();
    }
}
