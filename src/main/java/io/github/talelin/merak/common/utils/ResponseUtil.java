package io.github.talelin.merak.common.utils;

import io.github.talelin.merak.vo.PageResponseVO;
import io.github.talelin.merak.vo.UnifyResponseVO;
import io.github.talelin.autoconfigure.exception.HttpException;
import io.github.talelin.autoconfigure.response.Created;
import io.github.talelin.autoconfigure.response.Success;
import io.github.talelin.autoconfigure.beans.Code;
import io.github.talelin.autoconfigure.utils.RequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


/**
 * 响应结果生成工具
 */
@Slf4j
public class ResponseUtil {

    /**
     * 获得当前响应
     *
     * @return 响应
     */
    public static HttpServletResponse getResponse() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
    }

    public static void setCurrentResponseHttpStatus(int httpStatus) {
        getResponse().setStatus(httpStatus);
    }

    public static UnifyResponseVO generateUnifyResponse(HttpException e) {
        return UnifyResponseVO.builder()
                .message(e.getMessage())
                .code(e.getCode())
                .request(RequestUtil.getSimpleRequest())
                .build();
    }

    public static <T> UnifyResponseVO<T> generateSuccessResponse(T data) {
        Success success = new Success();
        return (UnifyResponseVO<T>) UnifyResponseVO.builder()
                .message(data)
                .code(success.getCode())
                .request(RequestUtil.getSimpleRequest())
                .build();
    }

    public static <T> UnifyResponseVO<T> generateUnifyResponse(int code) {
        return (UnifyResponseVO<T>) UnifyResponseVO.builder()
                .code(code)
                .request(RequestUtil.getSimpleRequest())
                .build();
    }

    public static <T> UnifyResponseVO<T> generateCreatedResponse(T data) {
        Created created = new Created();
        setCurrentResponseHttpStatus(created.getHttpCode());
        return (UnifyResponseVO<T>) UnifyResponseVO.builder()
                .message(data)
                .code(created.getCode())
                .request(RequestUtil.getSimpleRequest())
                .build();
    }

    public static <T> UnifyResponseVO<T> generateUnifyResponse(Code code, int httpCode) {
        setCurrentResponseHttpStatus(httpCode);
        return (UnifyResponseVO<T>) UnifyResponseVO.builder()
                .code(code.getCode())
                .message(code.getDescription())
                .request(RequestUtil.getSimpleRequest())
                .build();
    }

    public static PageResponseVO generatePageResult(long total, List items, long page, long count) {
        return new PageResponseVO(total, items, page, count);
    }
}
