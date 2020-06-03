package io.github.talelin.latticy.bo;

import lombok.Data;

/**
 * @author pedro@TaleLin
 * @author Juzi@TaleLin
 */
@Data
public class FileBO {

    /**
     * 文件 id
     */
    private Integer id;

    /**
     * 文件 key，上传时指定的
     */
    private String key;

    /**
     * 文件路径
     */
    private String path;

    /**
     * 文件 URL
     */
    private String url;
}
