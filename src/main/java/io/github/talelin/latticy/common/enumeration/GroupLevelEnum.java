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
    private Integer value;

    private String description;

    GroupLevelEnum(Integer value , String description) {
        this.value = value;
        this.description = description;
    }

    public Integer getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }
}
