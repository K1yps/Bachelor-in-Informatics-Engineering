package FileServerProtocol.Structs;

import java.net.InetAddress;
import java.util.Objects;

public class ServerAddress {
    public final InetAddress address;
    public final int port;

    public ServerAddress(InetAddress address, int port) {
        this.address = address;
        this.port = port;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServerAddress that = (ServerAddress) o;
        return port == that.port &&
                address.equals(that.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address, port);
    }

    @Override
    public String toString() {
        return address + ":" + port;
    }
}
