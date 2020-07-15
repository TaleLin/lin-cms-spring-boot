package io.github.talelin.latticy.module.log;

/**
 * @author Juzi@TaleLin
 * @date 2020/6/20 10:22
 */
public class MDCAccessConstant {
    public static final String REQUEST_METHOD_MDC_KEY = "req.method";
    public static final String RESPONSE_STATUS_MDC_KEY = "res.status";
    public static final String REQUEST_REFERER_MDC_KEY = "req.referer";
    public static final String REQUEST_PROTOCOL_MDC_KEY = "req.protocol";
    public static final String REQUEST_USER_AGENT_MDC_KEY = "req.userAgent";
    public static final String REQUEST_REMOTE_HOST_MDC_KEY = "req.remoteHost";
    public static final String REQUEST_REMOTE_ADDR_MDC_KEY = "req.remoteAddr";
    public static final String REQUEST_REQUEST_URI_MDC_KEY = "req.requestURI";
    public static final String REQUEST_REQUEST_URL_MDC_KEY = "req.requestURL";
    public static final String REQUEST_QUERY_STRING_MDC_KEY = "req.queryString";
    public static final String REQUEST_X_FORWARDED_FOR_MDC_KEY = "req.xForwardedFor";
    public static final String REQUEST_BODY_BYTES_SENT_MDC_KEY = "req.bodyBytesSent";
    public static final String REQUEST_REMOTE_PORT_MDC_KEY = "req.remotePort";
}
