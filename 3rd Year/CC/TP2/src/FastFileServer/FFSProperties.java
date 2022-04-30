package FastFileServer;

import java.io.FileInputStream;
import java.util.Properties;

public class FFSProperties {
    private static final Properties configs = new Properties();
    public static int stdTimeOut, ackTimeOut, maxQueueSize, timeoutThreshold, dupThreshold,
            congestionAvoidanceSwitch, maxConnectionAttempts, maxPingAttempts;
    public static boolean seeThresholdResolve,seeIncomingFrames,extendFrameOutput,seeOutgoingFrames;

    static {

        try {
            configs.load(new FileInputStream("ffs.cfg"));
        } catch (Exception ignored) {
            System.out.println("Failed to open config file, assuming default values for All variables");
        }

        stdTimeOut = getOrDefaultInt("stdTimeOut", 5000); //Milliseconds
        ackTimeOut = getOrDefaultInt("ackTimeOut", 5000); //Nanoseconds
        maxQueueSize = getOrDefaultInt("maxQueueSize", 100); //#FSChunks
        timeoutThreshold = getOrDefaultInt("timeoutThreshold", 5); //#
        dupThreshold = getOrDefaultInt("dupThreshold", 5); //#
        congestionAvoidanceSwitch = getOrDefaultInt("congestionAvoidanceThreshold", 64);
        maxConnectionAttempts = getOrDefaultInt("maxConnectionAttempts", 3);
        maxPingAttempts = getOrDefaultInt("maxConnectionAttempts", 10);
        seeThresholdResolve = getOrDefaultBool("seeThresholdResolve",false);
        seeIncomingFrames = getOrDefaultBool("seeIncomingFrames",false);
        seeOutgoingFrames = getOrDefaultBool("seeOutgoingFrames",false);
        extendFrameOutput = getOrDefaultBool("extendFrameOutput",false);
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
        if (obj != null)
            return obj;
        else return value;
    }

}
