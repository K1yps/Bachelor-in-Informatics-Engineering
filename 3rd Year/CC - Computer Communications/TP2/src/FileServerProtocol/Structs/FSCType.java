package FileServerProtocol.Structs;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum FSCType {

    //--- Values ------------------------------------------------------------------------------------------------------
    //Who_are_you(-2),
    Bye(-1),

    GET(0),
    Announcement(1),
    File_Cache(2),

    ReGET(3),
    Ping_Request(4),
    Ping_Response(5),

    Data_Acknowledgement(6),
    Cache_Acknowledgement(7),


    OK(200),
    Accepted(202),

    Bad_Request(400),
    Not_Found(404),
    Not_Acceptable(406),
    Gateway_Timeout(504),


    //Bad_Checksum(418)
    ;

    //--- ------- -----------------------------------------------------------------------------------------------------

    private static final Map<Integer, FSCType> map =
            Arrays.stream(FSCType.values()).collect(Collectors.toMap(v -> v.code, v -> v));
    private final int code;

    FSCType(int code) {
        this.code = code;
    }

    public static FSCType get(int code) {
        return map.get(code);
    }

    public int getCode() {
        return code;
    }


    // HTTP ERROR CODES
    /*
    //1×× Informational
    100 Continue
    101 Switching Protocols
    102 Processing

    //2×× Success
    200 OK
    201 Created
    202 Accepted
    203 Non-authoritative Information
    204 No Content
    205 Reset Content
    206 Partial Content
    207 Multi-Status
    208 Already Reported
    226 IM Used

    //3×× Redirection
    300 Multiple Choices
    301 Moved Permanently
    302 Found
    303 See Other
    304 Not Modified
    305 Use Proxy
    307 Temporary Redirect
    308 Permanent Redirect

    //4×× Client Error
    400 Bad Request
    401 Unauthorized
    402 Payment Required
    403 Forbidden
    404 Not Found
    405 Method Not Allowed
    406 Not Acceptable
    407 Proxy Authentication Required
    408 Request Timeout
    409 Conflict
    410 Gone
    411 Length Required
    412 Precondition Failed
    413 Payload Too Large
    414 Request-URI Too Long
    415 Unsupported Media Type
    416 Requested Range Not Satisfiable
    417 Expectation Failed
    418 I'm a teapot
    421 Misdirected Request
    422 Unprocessable Entity
    423 Locked
    424 Failed Dependency
    426 Upgrade Required
    428 Precondition Required
    429 Too Many Requests
    431 Request Header Fields Too Large
    444 Connection Closed Without Response
    451 Unavailable For Legal Reasons
    499 Client Closed Request

    //5×× Server Error
    500 Internal Server Error
    501 Not Implemented
    502 Bad Gateway
    503 Service Unavailable
    504 Gateway Timeout
    505 HTTP Version Not Supported
    506 Variant Also Negotiates
    507 Insufficient Storage
    508 Loop Detected
    510 Not Extended
    511 Network Authentication Required
    599 Network Connect Timeout Error
    */
}
