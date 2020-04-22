package io.github.talelin.merak.vo;

import io.github.talelin.autoconfigure.bean.Code;

public class UpdatedVO<T> extends UnifyResponseVO {

    public UpdatedVO() {
        super(Code.UPDATED.getCode());
    }

    public UpdatedVO(int code) {
        super(code);
    }

    public UpdatedVO(T message) {
        super(message);
    }

    public UpdatedVO(int code, T message) {
        super(code, message);
    }

}
