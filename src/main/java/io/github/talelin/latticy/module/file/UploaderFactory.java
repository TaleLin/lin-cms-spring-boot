package io.github.talelin.latticy.module.file;

import io.github.talelin.latticy.extension.file.LocalUploader;
import io.github.talelin.latticy.extension.file.QiniuUploader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author pedro@TaleLin
 */
@Component
public class UploaderFactory {

    @Value("${lin.cms.file.uploader:local}")
    private String uploader;

    @Bean
    public Uploader createUploader() {
        switch (uploader) {
            case "local":
                return new LocalUploader();
            case "qiniuyun":
                return new QiniuUploader();
            default:
                break;
        }
        return null;
    }
}
