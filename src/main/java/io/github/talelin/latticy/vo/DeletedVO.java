package io.github.talelin.latticy.vo;

import io.github.talelin.autoconfigure.bean.Code;
import io.github.talelin.latticy.common.util.ResponseUtil;
import org.springframework.http.HttpStatus;

/**
 * @author colorful@TaleLin
 */
public class DeletedVO extends UnifyResponseVO<String> {

    public DeletedVO() {
        super(Code.DELETED.getCode());
        ResponseUtil.setCurrentResponseHttpStatus(HttpStatus.CREATED.value());
    }

    public DeletedVO(int code) {
        super(code);
        ResponseUtil.setCurrentResponseHttpStatus(HttpStatus.CREATED.value());
    }

    public DeletedVO(String message) {
        super(message);
        ResponseUtil.setCurrentResponseHttpStatus(HttpStatus.CREATED.value());
    }

    public DeletedVO(int code, String message) {
        super(code, message);
        ResponseUtil.setCurrentResponseHttpStatus(HttpStatus.CREATED.value());
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
