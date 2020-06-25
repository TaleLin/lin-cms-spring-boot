package io.github.talelin.latticy.module.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 记录 access log 的过滤器
 * 会把 request、response 相关信息（如 IP、URL）放入 Logback 的 MDC中
 *
 * @author Juzi@TaleLin
 * @date 2020/6/20 09:58
 */
public class MDCAccessServletFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(MDCAccessServletFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        putRequestMDC(request);
        try {
            chain.doFilter(request, response);
            putResponseMDC(response);
            accessLog();
        } finally {
            clearMDC();
        }
    }

    /**
     * 记录 access log
     */
    public void accessLog() {
        log.info("");
    }

    public void putResponseMDC(ServletResponse servletResponse) {
        if (servletResponse instanceof HttpServletResponse) {
            HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
            MDC.put(MDCAccessConstant.RESPONSE_STATUS_MDC_KEY, String.valueOf(httpServletResponse.getStatus()));
        }
    }

    /**
     * 放入 request 信息
     */
    public void putRequestMDC(ServletRequest servletRequest) throws IOException {
        MDC.put(MDCAccessConstant.REQUEST_REMOTE_HOST_MDC_KEY, servletRequest.getRemoteHost());
        MDC.put(MDCAccessConstant.REQUEST_REMOTE_ADDR_MDC_KEY, servletRequest.getRemoteAddr());
        MDC.put(MDCAccessConstant.REQUEST_PROTOCOL_MDC_KEY, servletRequest.getProtocol());
        MDC.put(MDCAccessConstant.REQUEST_PROTOCOL_MDC_KEY, servletRequest.getProtocol());
        MDC.put(MDCAccessConstant.REQUEST_REMOTE_PORT_MDC_KEY, String.valueOf(servletRequest.getRemotePort()));
        MDC.put(MDCAccessConstant.REQUEST_BODY_BYTES_SENT_MDC_KEY, String.valueOf(servletRequest.getContentLength()));

        if (servletRequest instanceof HttpServletRequest) {
            HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
            MDC.put(MDCAccessConstant.REQUEST_REQUEST_URI_MDC_KEY, httpServletRequest.getRequestURI());
            StringBuffer requestUrl = httpServletRequest.getRequestURL();
            if (requestUrl != null) {
                MDC.put(MDCAccessConstant.REQUEST_REQUEST_URL_MDC_KEY, requestUrl.toString());
            }
            MDC.put(MDCAccessConstant.REQUEST_METHOD_MDC_KEY, httpServletRequest.getMethod());
            MDC.put(MDCAccessConstant.REQUEST_QUERY_STRING_MDC_KEY, httpServletRequest.getQueryString());
            MDC.put(MDCAccessConstant.REQUEST_USER_AGENT_MDC_KEY, httpServletRequest.getHeader("User-Agent"));
            MDC.put(MDCAccessConstant.REQUEST_X_FORWARDED_FOR_MDC_KEY, httpServletRequest.getHeader("X-Forwarded-For"));
            MDC.put(MDCAccessConstant.REQUEST_REFERER_MDC_KEY, httpServletRequest.getHeader("referer"));
        }
    }

    /**
     * 清理 MDC，特别重要
     * 因为 MDC 由 ThreadLocal 实现，如果不清理，线程池的复用机制会导致 MDC 数据受到污染
     */
    public void clearMDC() {
        MDC.remove(MDCAccessConstant.REQUEST_METHOD_MDC_KEY);
        MDC.remove(MDCAccessConstant.RESPONSE_STATUS_MDC_KEY);
        MDC.remove(MDCAccessConstant.REQUEST_REFERER_MDC_KEY);
        MDC.remove(MDCAccessConstant.REQUEST_PROTOCOL_MDC_KEY);
        MDC.remove(MDCAccessConstant.REQUEST_USER_AGENT_MDC_KEY);
        MDC.remove(MDCAccessConstant.REQUEST_REMOTE_HOST_MDC_KEY);
        MDC.remove(MDCAccessConstant.REQUEST_REMOTE_ADDR_MDC_KEY);
        MDC.remove(MDCAccessConstant.REQUEST_REQUEST_URI_MDC_KEY);
        MDC.remove(MDCAccessConstant.REQUEST_REQUEST_URL_MDC_KEY);
        MDC.remove(MDCAccessConstant.REQUEST_QUERY_STRING_MDC_KEY);
        MDC.remove(MDCAccessConstant.REQUEST_X_FORWARDED_FOR_MDC_KEY);
        MDC.remove(MDCAccessConstant.REQUEST_BODY_BYTES_SENT_MDC_KEY);
        MDC.remove(MDCAccessConstant.REQUEST_REMOTE_PORT_MDC_KEY);
    }

}
