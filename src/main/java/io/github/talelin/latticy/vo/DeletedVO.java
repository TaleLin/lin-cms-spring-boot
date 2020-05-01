package io.github.talelin.latticy.vo;

import io.github.talelin.autoconfigure.bean.Code;

/**
 * @author colorful@TaleLin
 */
public class DeletedVO<T> extends UnifyResponseVO {

    public DeletedVO() {
        super(Code.DELETED.getCode());
    }

    public DeletedVO(int code) {
        super(code);
    }

    public DeletedVO(T message) {
        super(message);
    }

    public DeletedVO(int code, T message) {
        super(code, message);
    }

}
