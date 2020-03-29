package io.github.talelin.merak.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 统一API响应结果封装
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UnifyResponseVO<T> {

    private int code;

    private T message;

    private String request;
}
