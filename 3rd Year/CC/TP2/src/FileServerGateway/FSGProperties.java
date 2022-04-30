package FileServerGateway;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;

public class FSGProperties {
    private static final Properties configs = new Properties();
    //Declarações
    public static int MTU, MaxServersCount, ServerTolerance, Backlog, MaxRequests;
    public static long PingTimer, GetTimeout;
    //HTTPS Stuff
    public static String BlackListFile, Alias, Password, KeystoreFile, KeyAlgorithm,
            SSLProtocol, KeyStoreAlgorithm, KeyManagerAlgorithm, TrustManagerAlgorithm;
    public static boolean seeIncomingFrames, extendFrameOutput, seeOutgoingFrames,seeOutgoingAcknowledgements;

    public static Set<InetAddress> ServerBlackList = new HashSet<>();

    static {

        try {
            configs.load(new FileInputStream("fsg.cfg"));
        } catch (Exception ignored) {
            System.out.println("Failed to open config file, assuming default values for All variables");
        }

        //Atribuições
        MaxRequests = getOrDefaultInt("MaxRequests", 50);
        MTU = getOrDefaultInt("MTU", 4096);
        MaxServersCount = getOrDefaultInt("MaxServersCount", 40);
        ServerTolerance = getOrDefaultInt("ServerTolerance", 3);
        PingTimer = getOrDefaultLong("PingTimer", 300L) * 1000L; // // Default 5 min
        GetTimeout = getOrDefaultLong("GetTimeout", 10L) * 1000L;
        Backlog = getOrDefaultInt("Backlog", 50);
        BlackListFile = getOrDefaultString("BlackListFile", null);
        seeOutgoingAcknowledgements= getOrDefaultBool("seeOutgoingAcknowledgements", false);
        seeIncomingFrames = getOrDefaultBool("seeIncomingFrames", false);
        seeOutgoingFrames = getOrDefaultBool("seeOutgoingFrames", false);
        extendFrameOutput = getOrDefaultBool("extendFrameOutput", false);

        //HTTPS
        //Certificate Variables
        Alias = getOrDefaultString("Alias", "Alias67");
        Password = getOrDefaultString("KeyPassword", "password");
        KeyAlgorithm = getOrDefaultString("KeyAlgorithm", "RSA");
        KeystoreFile = getOrDefaultString("Keystore", "keys.keystore");

        //Certificate Options
        SSLProtocol = getOrDefaultString("SSLProtocol", "TLS");
        KeyStoreAlgorithm = getOrDefaultString("KeyStoreAlgorithm", "JKS");
        KeyManagerAlgorithm = getOrDefaultString("KeyManagerAlgorithm", "SunX509");
        TrustManagerAlgorithm = getOrDefaultString("TrustManagerAlgorithm", "SunX509");

        setupBlackList();
    }

    private static void setupBlackList() {
        if (BlackListFile == null) return;
        try {
            Scanner in = new Scanner(new FileInputStream(BlackListFile));
            for (int i = 0; in.hasNext(); i++) {
                try {
                    ServerBlackList.add(InetAddress.getByName(in.nextLine()));
                } catch (UnknownHostException e) {
                    System.out.println("Invalid Address in line" + i + " " + e.getMessage());
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println("Failed to open BlackListFile :" + " \"" + BlackListFile + "\" ");
        }

    }


    private static int getOrDefaultInt(String property, int value) {
        try {
            String obj = configs.getProperty(property);
            if (obj != null)
                return Integer.parseInt(obj);
            else return value;

        } catch (NumberFormatException e) {
            return value;
        }
    }

    private static long getOrDefaultLong(String property, long value) {
        try {
            String obj = configs.getProperty(property);
            if (obj != null)
                return Long.parseLong(obj);
            else return value;
        } catch (NumberFormatException e) {
            return value;
        }
    }


    private static boolean getOrDefaultBool(String property, boolean value) {
        try {
            String obj = configs.getProperty(property);
            if (obj != null)
                return Boolean.parseBoolean(obj);
            else return value;
        } catch (NumberFormatException e) {
            return value;
        }
    }

    private static String getOrDefaultString(String property, String value) {
        String obj = configs.getProperty(property);
        if (obj != null && !obj.isEmpty() && !obj.isBlank())
            return obj;
        else return value;
    }

}
