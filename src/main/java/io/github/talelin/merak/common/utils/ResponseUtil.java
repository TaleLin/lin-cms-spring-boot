package io.github.talelin.merak.common.utils;

import io.github.talelin.merak.vo.UnifyResponseVO;
import io.github.talelin.autoconfigure.exception.HttpException;
import io.github.talelin.autoconfigure.response.Created;
import io.github.talelin.autoconfigure.response.Success;
import io.github.talelin.autoconfigure.beans.Code;
import io.github.talelin.autoconfigure.utils.RequestUtil;
import lombok.extern.slf4j.Slf4j;


/**
 * 响应结果生成工具
 */
@Slf4j
public class ResponseUtil {

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
        return (UnifyResponseVO<T>) UnifyResponseVO.builder()
                .message(data)
                .code(created.getCode())
                .request(RequestUtil.getSimpleRequest())
                .build();
    }

    public static <T> UnifyResponseVO<T> generateUnifyResponse(Code code, int httpCode) {
        return (UnifyResponseVO<T>) UnifyResponseVO.builder()
                .code(code.getCode())
                .message(code.getDescription())
                .request(RequestUtil.getSimpleRequest())
                .build();
    }
}
