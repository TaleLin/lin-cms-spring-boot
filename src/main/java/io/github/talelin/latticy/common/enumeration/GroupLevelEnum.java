package io.github.talelin.latticy.common.enumeration;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * @author colorful@TaleLin
 */
public enum GroupLevelEnum {
    /**
     * 超级管理员
     */
    ROOT(1,"root"),
    /**
     * 游客
     */
    GUEST(2,"guest"),
    /**
     * 普通用户
     */
    USER(3,"user")
    ;

    @EnumValue
    private Integer code;

    private String value;

    GroupLevelEnum(Integer code , String value) {
        this.code = code;
        this.value = value;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
