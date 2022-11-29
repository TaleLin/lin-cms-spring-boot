package io.github.talelin.latticy.common.interceptor;

import io.github.talelin.latticy.common.util.IPUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author pedro@TaleLin
 * @author colorful@TaleLin
 * 请求日志拦截器
 */
@Slf4j
public class RequestLogInterceptor implements AsyncHandlerInterceptor {


    private final ThreadLocal<Long> startTime = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        startTime.set(System.currentTimeMillis());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        log.info("[{}] -> [{}] from: {} costs: {}ms",
                request.getMethod(),
                request.getServletPath(),
                IPUtil.getIPFromRequest(request),
                System.currentTimeMillis() - startTime.get()
        );
        startTime.remove();
    }
}
