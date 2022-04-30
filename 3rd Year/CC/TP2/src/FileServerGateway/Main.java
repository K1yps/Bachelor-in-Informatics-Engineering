package FileServerGateway;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.exit;

public class Main {
    static boolean secure = false;


    public static void main(String[] args) {

        FSGateway main;

        List<String> obj = parseArgs(args);

        try {
            switch (obj.size()) {
                case 1 -> main = new FSGateway(Integer.parseInt(obj.get(0)), InetAddress.getLocalHost(), secure);
                case 2 -> main = new FSGateway(Integer.parseInt(obj.get(0)), InetAddress.getByName(obj.get(1)), secure);
                case 3 -> {
                    Integer res = null;
                    try {
                        res = Integer.parseInt(obj.get(2));
                    } catch (NumberFormatException ignored) {
                    }
                    if (res != null)
                        main = new FSGateway(Integer.parseInt(obj.get(0)), InetAddress.getByName(obj.get(1)), res, secure);
                    else
                        main = new FSGateway(Integer.parseInt(obj.get(0)), InetAddress.getByName(obj.get(1)), InetAddress.getByName(obj.get(2)), 80, secure);
                }
                case 4 -> main = new FSGateway(Integer.parseInt(obj.get(0)), InetAddress.getByName(obj.get(1)), InetAddress.getByName(obj.get(2)), Integer.parseInt(obj.get(3)), secure);
                default -> {
                    System.out.println("Server Bound to " + InetAddress.getLocalHost().toString() + ":80");
                    main = new FSGateway(80, InetAddress.getLocalHost(), secure);
                }
            }
        } catch (Exception e) {
            System.out.println("Error Starting Gateway\n" + e.getMessage());
            exit(-1);
            return;
        }
        main.run();
    }

    private static void genCertificate() {
        try {
            String command = "keytool -v  -genkey " +
                    " -keyalg  " + FSGProperties.KeyAlgorithm +
                    " -alias " + FSGProperties.Alias +
                    " -keypass " + FSGProperties.Password +
                    " -storepass " + FSGProperties.Password +
                    " -keystore " + FSGProperties.KeystoreFile;

            System.out.println(command);
            Runtime.getRuntime().exec(command);
        } catch (Exception e) {
            System.out.println("Failed to create certificate\n" + e.getMessage());
            exit(-1);
        }
        exit(0);
    }

    private static String help() {
        return "\nFormats:\n" +
                "FileServerGateway [Options...]\n" +
                "FileServerGateway [UDP Port] [Options...]\n" +
                "FileServerGateway [UDP Port] [UDP Address] [Options...]\n" +
                "FileServerGateway [UDP Port] [UDP Address] [HTTP Address] [Options...]\n" +
                "FileServerGateway [UDP Port] [UDP Address] [HTTP Port] [Options...]\n" +
                "FileServerGateway [UDP Port] [UDP Address] [HTTP Address] [HTTP Port] [Options...]\n"
                + "\n" +
                "Descriptions:\n" +
                "\t[UDP Address]   -> Gateway's UDP listening Address  (default = localhost)\n" +
                "\t[UDP Port]      -> Gateway's UDP listening Port     (default = 80)\n" +
                "\t[HTTP Address]  -> Gateway's HTTP(s) server Address (default = localhost)\n" +
                "\t[HTTP Port]     -> Gateway's HTTP(s) server Port    ( HTTP default = 80, HTTPs default = 443)\n" +
                "\t[Options...]:\n" +
                "\t\t-https      -> Runs a HTTPSecure server instead of a regular HTTP server\n" +
                "\t\t-genC       -> Attempts to run a java keytool program in order to generate a signed HTTPS certificate \n" +
                "\t\t               (the command attempted is printed on console)\n" +
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
                    case "-genC" -> genCertificate();
                    case "-https" -> secure = true;
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
