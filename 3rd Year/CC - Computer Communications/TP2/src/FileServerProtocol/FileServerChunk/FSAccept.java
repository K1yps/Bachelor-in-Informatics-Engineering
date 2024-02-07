package FileServerProtocol.FileServerChunk;

import FileServerProtocol.Structs.FSCType;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class FSAccept extends FSChunk {

    private final int MaxFrameSize;
    private final long warningTimer;

    public FSAccept(int MaxFrameSize, long warningTimer) {
        super(FSCType.Accepted);
        this.MaxFrameSize = MaxFrameSize;
        this.warningTimer = warningTimer;
    }

    public static FSAccept read(DataInputStream input) throws IOException {
        int size = input.readInt();
        long warningTimer = input.readLong();
        if (size <= 0)
            size = 512; // Default Datagram size to 512

        input.close();

        return new FSAccept(size, warningTimer);
    }

    public long getWarningTimer() {
        return warningTimer;
    }

    @Override
    protected void write(DataOutputStream input) throws IOException {
        super.write(input);
        input.writeInt(MaxFrameSize);
        input.writeLong(warningTimer);
    }

    public int getMaxFrameSize() {
        return MaxFrameSize;
    }

    @Override
    public String toString() {
        return "FSAccept{" +
                "MaxFrameSize=" + MaxFrameSize +
                ", warningTimer=" + warningTimer +
                '}';
    }

    @Override
    public String toString(boolean extend) {
        return this.toString();
    }
}
