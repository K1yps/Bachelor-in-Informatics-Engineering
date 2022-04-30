package FileServerProtocol.Structs;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

public class FileMetaData {
    private static final int StaticSize = 2 * (Long.SIZE) / 8;
    public final String file;
    public final long size, modifiedTime;

    public FileMetaData(String file, long size, long modifiedTime) {
        this.file = file;
        this.size = size;
        this.modifiedTime = modifiedTime;
    }

    public static FileMetaData deserialize(DataInputStream s) {

        try {
            return new FileMetaData(s.readUTF(), s.readLong(), s.readLong());
        } catch (IOException e) {
            return null;
        }
    }

    public String getFile() {
        return file;
    }

    public void serialize(DataOutputStream stream) throws IOException {
        stream.writeUTF(file);
        stream.writeLong(size);
        stream.writeLong(modifiedTime);
        stream.flush();
    }

    public int serSize() {
        return file.length() + StaticSize;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FileMetaData)) return false;
        FileMetaData that = (FileMetaData) o;
        return size == that.size && modifiedTime == that.modifiedTime && file.equals(that.file);
    }

    @Override
    public int hashCode() {
        return Objects.hash(file, size, modifiedTime);
    }

    @Override
    public String toString() {
        return "File = " + file + ' ' + "Size = " + size + ' ' + "ModifiedTime = " + modifiedTime;
    }

    public String getExtension() {
        return Optional.ofNullable(file)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(file.lastIndexOf(".") + 1)).orElse("");
    }
}
