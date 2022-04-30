package FileServerProtocol.FileServerChunk;

import FileServerProtocol.Structs.FSCType;
import FileServerProtocol.Structs.ServerAddress;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class FSFail extends FSChunk {

    String description;

    public FSFail(FSCType type) {
        super(type);
    }

    public FSFail(FSCType type, String description) {
        super(type);
        this.description = description;
    }

    public FSFail(FSCType type, ServerAddress father, String description) {
        super(type, father);
        this.description = description;
    }

    public static FSFail read(FSCType type, DataInputStream input) {
        FSFail res = new FSFail(type);

        try {
            res.description = input.readUTF();
        } catch (IOException e) {
            res.description = "";
        }
        return res;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    protected void write(DataOutputStream input) throws IOException {
        super.write(input);
        if (description != null && !description.isEmpty()) {
            try {
                input.writeUTF(description);
            } catch (IOException ignored) {
            }
        }
    }

    @Override
    public String toString() {
        return "FSFail{" +
                "Type= " + this.type +
                ",description='" + description + '\'' +
                '}';
    }

    @Override
    public String toString(boolean extend) {
        if (extend)
            return this.toString();
        else
            return "FSFail{" + "Type= " + this.type + '}';
    }
}
