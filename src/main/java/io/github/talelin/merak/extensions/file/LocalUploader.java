package io.github.talelin.merak.extensions.file;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;

/**
 * 文件上传服务默认实现，上传到本地
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
        try {
            BufferedOutputStream stream =
                    new BufferedOutputStream(new FileOutputStream(new java.io.File(absolutePath)));
            stream.write(bytes);
            stream.close();
        } catch (Exception e) {
            log.error("write file to local err:", e);
            // throw new FailedException("read file date failed", 10190);
            return false;
        }
        return true;
    }

    @Override
    protected FileProperties getFileProperties() {
        return fileProperties;
    }

    @Override
    protected String getStorePath(String newFilename) {
        return newFilename;
    }

    @Override
    protected String getFileType() {
        return FileConstant.LOCAL;
    }
}
