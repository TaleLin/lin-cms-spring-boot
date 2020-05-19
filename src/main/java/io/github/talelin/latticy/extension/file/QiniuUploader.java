package io.github.talelin.latticy.extension.file;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import io.github.talelin.latticy.module.file.AbstractUploader;
import io.github.talelin.latticy.module.file.FileConstant;
import io.github.talelin.latticy.module.file.FileProperties;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;

/**
 * 文件上传服务实现，上传到七牛
 *
 * @author pedro@TaleLin
 */
@Slf4j
public class QiniuUploader extends AbstractUploader {

    @Autowired
    private FileProperties fileProperties;

    @Value("${lin.file.qiniuyun.access-key}")
    private String accessKey;

    @Value("${lin.file.qiniuyun.secret-key}")
    private String secretKey;

    @Value("${lin.file.qiniuyun.bucket}")
    private String bucket;

    private UploadManager uploadManager;

    private String upToken;

    /**
     * 因为需要得到 spring-boot 提供的配置，所以不能在 constructor 中初始化
     * 使用 PostConstruct 来初始化
     */
    @PostConstruct
    public void initUploadManager() {
        Configuration cfg = new Configuration(Region.region2());
        uploadManager = new UploadManager(cfg);
        Auth auth = Auth.create(accessKey, secretKey);
        upToken = auth.uploadToken(bucket);
    }

    @Override
    protected FileProperties getFileProperties() {
        return fileProperties;
    }

    @Override
    protected String getStorePath(String newFilename) {
        return fileProperties.getDomain() + newFilename;
    }

    @Override
    protected String getFileType() {
        return FileConstant.REMOTE;
    }

    /**
     * 处理一个文件数据
     *
     * @param bytes       文件数据，比特流
     * @param newFilename 新文件名称
     * @return 处理是否成功，如果出现异常则返回 false，避免把失败的写入数据库
     */
    @Override
    protected boolean handleOneFile(byte[] bytes, String newFilename) {
        ByteArrayInputStream byteInputStream = new ByteArrayInputStream(bytes);
        try {
            Response response = uploadManager.put(byteInputStream, newFilename, upToken, null, null);
            log.info(response.toString());
            return response.isOK();
        } catch (QiniuException ex) {
            Response r = ex.response;
            log.error("qiniuyun upload file error: {}", r.error);
            return false;
        }
    }
}
