package FileServerGateway;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class HTTPExchanger {

    public static String server;

    public static void setServerName(String host) {
        if (host != null && !host.isEmpty())
            server = "FileServerGateway/" + host;
        else
            server = "FileServerGateway/NULL";
    }

    public static void contextType(Headers h, String ending) {
        String res;
        switch (ending) {
            //case "html" -> res = "text/html"; //For now we will disable this
            //case "txt" -> res = "text/plain"; //------
            default -> res = "application/octet-stream";
        }
        h.add("Context-Type", res);
    }

    public static void lastModified(Headers h, long millis) {
        h.add("Last-Modified",  Instant.ofEpochMilli(millis).atZone(ZoneOffset.UTC).format(DateTimeFormatter.RFC_1123_DATE_TIME));
    }

    public static void setContentLength(Headers h, long length) {
        h.add("Content-Length", String.valueOf(length));
    }

    public static void date(Headers h) {
        h.add("Date", Instant.now().atZone(ZoneOffset.UTC).format(DateTimeFormatter.RFC_1123_DATE_TIME));
    }

    public static void global(Headers h) {

        h.add("Server", server);
    }

    public static void notSupported(HttpExchange exchange) throws IOException {
        String res = "<!DOCTYPE html>\n<html lang=\"html\">\n\t<body><center><h1>501 NOT IMPLEMENTED</h1></center>\t</body>\n</html>";
        exchange.sendResponseHeaders(501, 0);
        OutputStream body = exchange.getResponseBody();
        body.write(res.getBytes(StandardCharsets.UTF_8));
        body.close();
    }

    public static void notFound(HttpExchange exchange) throws IOException {
        String res = "<!DOCTYPE html>\n<html lang=\"html\">\n\t<body><center><h1>404 FILE NOT FOUND</h1></center>\t</body>\n</html>";
        exchange.sendResponseHeaders(404, 0);
        OutputStream body = exchange.getResponseBody();
        body.write(res.getBytes(StandardCharsets.UTF_8));
        body.close();
    }

    public static void internalError(HttpExchange exchange) throws IOException {
        String errorMSG = "<!DOCTYPE html>\n<html lang=\"html\">\n\t<body><center><h1>500 Internal Error</h1></center>\t</body>\n</html>";
        exchange.sendResponseHeaders(500, 0);
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(errorMSG.getBytes(StandardCharsets.UTF_8));
        outputStream.close();
    }

    public static void requestTimeout(HttpExchange exchange) throws IOException {
        String errorMSG = "<!DOCTYPE html>\n<html lang=\"html\">\n\t<body><center><h1>408 Request Timeout\n</h1></center>\t</body>\n</html>";
        exchange.sendResponseHeaders(408, 0);
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(errorMSG.getBytes(StandardCharsets.UTF_8));
        outputStream.close();
    }

}
