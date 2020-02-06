package io.github.talelin.merak.bo;

import lombok.Data;

@Data
public class FileBO {

    private Long id;

    private String path;

    /**
     * LOCAL REMOTE
     */
    private String type;

    private String name;

    private String extension;

    private Integer size;
}
