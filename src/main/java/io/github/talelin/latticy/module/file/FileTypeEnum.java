package io.github.talelin.latticy.module.file;

import com.baomidou.mybatisplus.annotation.IEnum;

/**
 * @author colorful
 * 文件类型枚举
 */
public enum FileTypeEnum implements IEnum<String> {

    /**
     * 本地文件
     */
    LOCAL("LOCAL"),

    /**
     * 远程文件，例如OSS
     */
    REMOTE("REMOTE")
    ;

    final String value;

    FileTypeEnum(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }
}
