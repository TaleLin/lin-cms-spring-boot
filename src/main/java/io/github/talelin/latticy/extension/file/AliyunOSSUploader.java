package io.github.talelin.latticy.extension.file;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.PutObjectResult;
import io.github.talelin.latticy.module.file.AbstractUploader;
import io.github.talelin.latticy.module.file.FileConstant;
import io.github.talelin.latticy.module.file.FileProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.ByteArrayInputStream;

/**
 * 文件上传服务实现，上传到阿里云OSS
 *
 * 参考文档: https://help.aliyun.com/document_detail/84781.html?spm=a2c4g.11186623.6.941.568d56f1BIjYOc
 * @author auzqy
 */
@Slf4j
public class AliyunOSSUploader extends AbstractUploader {

    @Autowired
    private FileProperties fileProperties;

    @Value("${lin.file.aliyun.oss.accessKeyId}")
    private String accessKeyId;

    @Value("${lin.file.aliyun.oss.accessKeySecret}")
    private String accessKeySecret;

    @Value("${lin.file.aliyun.oss.endpoint}")
    private String endpoint;

    @Value("${lin.file.aliyun.oss.domain}")
    private String domain;

    @Value("${lin.file.aliyun.oss.bucketName}")
    private String bucketName;

    @Override
    protected FileProperties getFileProperties() {
        return fileProperties;
    }

    @Override
    protected String getStorePath(String newFilename) {
        return domain + newFilename;
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
        OSS ossClient = new OSSClientBuilder()
                .build(endpoint,
                        accessKeyId,
                        accessKeySecret);
        try {
            PutObjectResult putObjectResult = ossClient.putObject(
                    bucketName, newFilename, new ByteArrayInputStream(bytes));
            log.debug("aliyun oss uploaded, eTag: {}",putObjectResult.getETag());
            return true;
        } catch (OSSException | ClientException oe) {
            log.error("aliyun oss 简单上传文件出错 ", oe);
            return false;
        } finally {
            ossClient.shutdown();
        }
    }
}
