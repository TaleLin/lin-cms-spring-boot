package io.github.talelin.latticy.vo;

import io.github.talelin.autoconfigure.bean.Code;
import io.github.talelin.latticy.common.util.ResponseUtil;
import org.springframework.http.HttpStatus;

/**
 * @author colorful@TaleLin
 */
public class DeletedVO<T> extends UnifyResponseVO {

    public DeletedVO() {
        super(Code.DELETED.getCode());
        ResponseUtil.setCurrentResponseHttpStatus(HttpStatus.CREATED.value());
    }

    public DeletedVO(int code) {
        super(code);
        ResponseUtil.setCurrentResponseHttpStatus(HttpStatus.CREATED.value());
    }

    public DeletedVO(T message) {
        super(message);
        ResponseUtil.setCurrentResponseHttpStatus(HttpStatus.CREATED.value());
    }

    public DeletedVO(int code, T message) {
        super(code, message);
        ResponseUtil.setCurrentResponseHttpStatus(HttpStatus.CREATED.value());
    }

}
