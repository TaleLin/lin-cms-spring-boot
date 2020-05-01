package io.github.talelin.latticy.module.file;

import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 文件上传服务接口
 *
 * @author pedro@TaleLin
 */
public interface Uploader {

    /**
     * 上传文件
     *
     * @param fileMap 文件map
     * @return 文件数据
     */
    List<File> upload(MultiValueMap<String, MultipartFile> fileMap);

    /**
     * 上传文件
     *
     * @param fileMap    文件map
     * @param preHandler 预处理器
     * @return 文件数据
     */
    List<File> upload(MultiValueMap<String, MultipartFile> fileMap, PreHandler preHandler);
}
