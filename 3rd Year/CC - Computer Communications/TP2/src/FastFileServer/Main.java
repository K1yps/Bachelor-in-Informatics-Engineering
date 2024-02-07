package FastFileServer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.exit;

public class Main {

    public static InetAddress gateway, local;
    public static Path path;
    public static int local_port = 80, server_port = 80;

    static {
        try {
            local = InetAddress.getLocalHost();
            path = Path.of(".");
        } catch (UnknownHostException ignored) {
        }
    }

    public static void main(String[] args) {

        List<String> obj = parseArgs(args);

        try {
            gateway = InetAddress.getByName(obj.get(0));
            if (obj.size() > 2) {
                path = Path.of(obj.get(2));
                server_port = Integer.parseInt(obj.get(1));
            } else if (obj.size() == 2) path = Path.of(obj.get(1));

        } catch (UnknownHostException e) {
            System.out.println("Invalid Gateway Address");
            exit(-2);
        } catch (NumberFormatException e) {
            System.out.println("Invalid Port Address");
            exit(-3);
        } catch (IndexOutOfBoundsException e) {
            if (obj.size() > 0)
                System.out.println("Invalid Parameters");
            else
                System.out.println("Missing Gateway IPAddress (Mandatory)");
            exit(-1);
        }

        FastFileServer main = null;
        try {
            main = new FastFileServer(gateway, local, path, local_port, server_port);
        } catch (IOException e) {
            System.out.println("An error has occurred.\n" + e.getMessage());
            exit(-1);
        }

        System.out.println("Starting Server\n\tConnecting with gateway ...");
        main.run();
        exit(0);
    }

    private static String help() {
        return "\nFormat:\n" +
                "./FastFileServer [GatewayIpAddress] [GatewayPort] [Directory] [Options...] \n" +
                "\n" +
                "Descriptions:\n" +
                "\t[GatewayIpAddress]   -> Gateway Internet Protocol Address (Mandatory)\n" +
                "\t[GatewayPort]        -> Gateway's Application Port        (Optional, default = 80)\n" +
                "\t[Directory]          -> Supplying directory               (Optional, default = Current Working Directory)\n" +
                "\t[Options..]:\n" +
                "\t\t-l, -local [Local IpAddress]  -> Specifies the local Interface from which messages will depart (default = localhost)\n" +
                "\t\t-p, -port  [Port]             -> Specifies the local Port from which messages will depart      (default = 80)\n" +
                "\n--help - help guide";
    }


    private static List<String> parseArgs(String[] args) {
        List<String> obj = new ArrayList<>();
        try {
            for (int i = 0; i < args.length; i++) {
                switch (args[i]) {
                    case "--help" -> {
                        System.out.println(help());
                        exit(0);
                    }
                    case "-l", "-local" -> local = InetAddress.getByName(args[++i]);
                    case "-p", "-port" -> local_port = Integer.parseInt(args[++i]);
                    default -> obj.add(args[i]);
                }

            }
        } catch (Exception e) {
            System.out.println("Error! We weren't able to process your arguments.\nIf you are lost use --help to get the server's options.");
            exit(-4);
        }
        return obj;
    }
}
