package io.github.talelin.latticy.module.file;

import lombok.*;

/**
 * @author pedro@TaleLin
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class File {

    /**
     * 真实url
     */
    private String url;

    /**
     * 前端上传的key
     */
    private String key;

    /**
     * 若 local，表示文件路径
     */
    private String path;

    /**
     * LOCAL REMOTE
     */
    private String type;

    /**
     * 文件名称
     */
    private String name;

    /**
     * 扩展名，例：.jpg
     */
    private String extension;

    /**
     * 文件大小
     */
    private Integer size;

    /**
     * md5值，防止上传重复文件
     */
    private String md5;
}
