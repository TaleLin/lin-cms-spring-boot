package io.github.talelin.latticy.vo;

import io.github.talelin.autoconfigure.bean.Code;
import io.github.talelin.latticy.common.util.ResponseUtil;
import org.springframework.http.HttpStatus;

/**
 * @author pedro@TaleLin
 */
public class UpdatedVO<T> extends UnifyResponseVO {

    public UpdatedVO() {
        super(Code.UPDATED.getCode());
        ResponseUtil.setCurrentResponseHttpStatus(HttpStatus.CREATED.value());
    }

    public UpdatedVO(int code) {
        super(code);
        ResponseUtil.setCurrentResponseHttpStatus(HttpStatus.CREATED.value());
    }

    public UpdatedVO(T message) {
        super(message);
        ResponseUtil.setCurrentResponseHttpStatus(HttpStatus.CREATED.value());
    }

    public UpdatedVO(int code, T message) {
        super(code, message);
        ResponseUtil.setCurrentResponseHttpStatus(HttpStatus.CREATED.value());
    }

}
