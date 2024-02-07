package FileServerProtocol.Structs;

import FileServerProtocol.Exceptions.BadResquestException;
import FileServerProtocol.Exceptions.InvalidChunkException;
import FileServerProtocol.Exceptions.InvalidTypeException;
import FileServerProtocol.FileServerChunk.*;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.DatagramPacket;

public class FSParser {

    public static FSChunk parse(DatagramPacket p) throws InvalidChunkException, BadResquestException, IOException {
        ByteArrayInputStream input = new ByteArrayInputStream(p.getData(), 0, p.getLength());
        DataInputStream dataInput = new DataInputStream(input);

        int t0 = dataInput.readInt();
        FSCType type = FSCType.get(t0);

        if (type == null)
            throw new InvalidTypeException(t0 + " is not a valid code for a FileServerChunk");

        FSChunk res = null;

        switch (type) {
            case GET -> res = FSGet.read(dataInput);
            case Announcement -> res = FSAnnouncement.read(dataInput);
            case File_Cache -> res = FSCache.read(dataInput);
            case OK -> res = FSData.read(dataInput);
            case ReGET -> res = FSReGet.read(dataInput);
            case Accepted -> res = FSAccept.read(dataInput);
            case Bad_Request -> throw new BadResquestException("Something went wrong on the client:" + p.getAddress());
            case Not_Found -> res = FSNotFound.read(dataInput);
            case Bye, Not_Acceptable -> res = FSFail.read(type, dataInput);
            case Ping_Request, Ping_Response -> res = FSPing.read(type);
            case Cache_Acknowledgement -> res = FSCacheAck.read(dataInput);
            case Data_Acknowledgement -> res = FSDataAck.read(dataInput);
        }
        if (res == null)
            throw new InvalidChunkException("Invalid Datagram received");
        res.setFather(new ServerAddress(p.getAddress(), p.getPort()));

        try {
            dataInput.close();
            input.close();
        } catch (IOException ignored) {
        }

        return res;
    }


}
