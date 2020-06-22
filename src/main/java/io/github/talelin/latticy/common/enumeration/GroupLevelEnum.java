package io.github.talelin.latticy.common.enumeration;

import com.baomidou.mybatisplus.core.enums.IEnum;

/**
 * @author colorful@TaleLin
 */
public enum GroupLevelEnum implements IEnum<Integer> {
    /**
     * 超级管理员
     */
    ROOT(1),
    /**
     * 游客
     */
    GUEST(2),
    /**
     * 普通用户
     */
    USER(3)
    ;

    private Integer value;

    GroupLevelEnum(Integer value) {
        this.value = value;
    }

    @Override
    public Integer getValue() {
        return value;
    }

}
