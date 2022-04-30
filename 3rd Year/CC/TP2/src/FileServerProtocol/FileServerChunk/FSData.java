package FileServerProtocol.FileServerChunk;

import FileServerProtocol.Exceptions.BadChecksumException;
import FileServerProtocol.Structs.FSCType;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

public class FSData extends FSChunk implements Comparable<FSData> {
    public static int HeaderSize = FSChunk.HeaderSize + 2 * (Integer.SIZE / 8) + 1;
    private final int offset;
    private final byte[] data;
    private final int size;
    private final Checksum checksum;
    private boolean last; // 1 byte


    public FSData(int offset, byte[] data, boolean last, int size) throws IllegalArgumentException {
        super(FSCType.OK);
        if (offset < 0)
            throw new IllegalArgumentException("The offset must be greater or equal to 0. Given value:" + offset);
        this.offset = offset;
        this.data = data;
        this.last = last;
        this.size = size;
        this.checksum = new CRC32(); //Cyclic Redundancy Check - 32 bits (4 bytes)
        this.checksum.update(data, 0, size);
    }

    public FSData(int offset, byte[] data, int size) throws IllegalArgumentException {
        super(FSCType.OK);
        if (offset < 0)
            throw new IllegalArgumentException("The offset must be greater or equal to 0. Given value:" + offset);
        this.offset = offset;
        this.data = data;
        this.last = false;
        this.size = size;
        this.checksum = new CRC32(); //Cyclic Redundancy Check - 32 bits (4 bytes)
        this.checksum.update(data, 0, size);
    }

    public FSData(int offset, byte[] data, boolean last) throws IllegalArgumentException {
        super(FSCType.OK);
        if (offset < 0)
            throw new IllegalArgumentException("The offset must be greater or equal to 0. Given value:" + offset);
        this.offset = offset;
        this.data = data;
        this.last = last;
        this.size = data.length;
        this.checksum = new CRC32(); //Cyclic Redundancy Check - 32 bits (4 bytes)
        this.checksum.update(data);
    }

    public int getSize() {
        return size;
    }

    public static FSData isFSData(FSChunk in) {
        if (in instanceof FSData)
            return (FSData) in;
        return null;
    }

    public static FSData read(DataInputStream input) throws BadChecksumException, IOException {
        int offsetField = input.readInt();
        boolean last = input.read() == 1;
        int checksum = input.readInt();

        FSData data =
                new FSData(offsetField, input.readAllBytes(), last);
        if (!data.checkChecksum(checksum))
            throw new BadChecksumException("Checksum assertion failed");
        return data;
    }

    public int getOffset() {
        return offset;
    }

    public byte[] getData() {
        return data;
    }

    public boolean isLast() {
        return last;
    }

    public Checksum getChecksum() {
        return checksum;
    }

    @Override
    protected void write(DataOutputStream input) throws IOException {
        super.write(input);
        input.writeInt(offset);
        input.write((last ? 1 : 0));
        input.writeInt((int) checksum.getValue()); //Its a 32 bit value, it's a long because it's unsigned
        input.write(data, 0, size);
    }

    public boolean checkChecksum(int checksum) {
        return ((int) this.checksum.getValue()) == checksum;
    }

    @Override
    public String toString() {
        return "FSData {" +
                "offset=" + offset +
                ", last=" + last +
                ", checksum=" + checksum +
                ", dataSize=" + size +
                '}';
    }

    @Override
    public String toString(boolean extend) {
        return this.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FSData)) return false;
        FSData fsData = (FSData) o;
        return offset == fsData.offset && last == fsData.last && size == fsData.size && Arrays.equals(data, fsData.data) && Objects.equals(checksum, fsData.checksum);
    }

    @Override
    public boolean isAcknowledgeable() {
        return true;
    }

    @Override
    public FSChunk acknowledge() {
        return new FSDataAck(offset);
    }

    @Override
    public int hashCode() {
        return Objects.hash(offset, last, size);
    }


    public void makeLast() {
        this.last = true;
    }

    @Override
    public int compareTo(FSData o) {
        return this.getOffset() - o.getOffset();
    }


}