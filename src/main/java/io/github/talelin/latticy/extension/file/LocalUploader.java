package io.github.talelin.latticy.extension.file;

import io.github.talelin.latticy.module.file.AbstractUploader;
import io.github.talelin.latticy.module.file.FileProperties;
import io.github.talelin.latticy.module.file.FileTypeEnum;
import io.github.talelin.latticy.module.file.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.io.BufferedOutputStream;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 文件上传服务默认实现，上传到本地
 *
 * @author pedro@TaleLin
 */
@Slf4j
public class LocalUploader extends AbstractUploader {

    @Autowired
    private FileProperties fileProperties;


    @PostConstruct
    public void initStoreDir() {
        // 本地存储需先初始化存储文件夹
        FileUtil.initStoreDir(this.fileProperties.getStoreDir());
    }

    @Override
    protected boolean handleOneFile(byte[] bytes, String newFilename) {
        String absolutePath =
                FileUtil.getFileAbsolutePath(fileProperties.getStoreDir(), getStorePath(newFilename));
        try (BufferedOutputStream stream =
                     new BufferedOutputStream(Files.newOutputStream(new File(absolutePath).toPath()))) {
            stream.write(bytes);
        } catch (Exception e) {
            log.error("write file to local err:", e);
            return false;
        }
        return true;
    }

    @Override
    protected FileProperties getFileProperties() {
        return fileProperties;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    protected String getStorePath(String newFilename) {
        Date now = new Date();
        String format = new SimpleDateFormat("yyyy/MM/dd").format(now);
        Path path = Paths.get(fileProperties.getStoreDir(), format).toAbsolutePath();
        java.io.File file = new File(path.toString());
        if (!file.exists()) {
            file.mkdirs();
        }
        return Paths.get(format, newFilename).toString();
    }

    @Override
    protected String getFileType() {
        return FileTypeEnum.LOCAL.getValue();
    }
}
