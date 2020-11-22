package io.github.talelin.latticy.extension.file;

import io.github.talelin.latticy.module.file.AbstractUploader;
import io.github.talelin.latticy.module.file.FileConstant;
import io.github.talelin.latticy.module.file.FileProperties;
import io.minio.MinioClient;
import io.minio.errors.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.xmlpull.v1.XmlPullParserException;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * 文件上传服务实现，上传到MinIO, MinIO服务建议用户自己搭建
 *
 * @author lhsheild
 */
@Slf4j
public class MinIOUploader extends AbstractUploader {

    @Autowired
    private FileProperties fileProperties;

    @Value("${lin.file.minio.access-key}")
    private String accessKey;

    @Value("${lin.file.minio.secret-key}")
    private String secretKey;

    @Value("${lin.file.minio.bucket}")
    private String bucket;

    @Value("${lin.file.minio.endpoint}")
    private String minioEndpoint;

    @Value("${lin.file.minio.port}")
    private Integer minioPort;

    @Value("${lin.file.minio.secure}")
    private Boolean secure;

    @Value("${lin.file.minio.retry-num}")
    private Integer retryNum;

    private MinioClient minioClient;

    /**
     * 因为需要得到 spring-boot 提供的配置，所以不能在 constructor 中初始化
     * 使用 PostConstruct 来初始化
     */
    @PostConstruct
    public void initMinioClient() throws InvalidPortException, InvalidEndpointException {
        this.minioClient = new MinioClient(minioEndpoint, minioPort, accessKey, secretKey, secure);
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
            this.putObjectWithRetry(bucket, newFilename, byteInputStream, null);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void putObjectWithRetry(String bucketName, String objectName, InputStream stream, String contentType) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InvalidArgumentException, NoResponseException, InvalidBucketNameException, XmlPullParserException, InternalException {
        int current = 0;
        boolean isSuccess = false;
        while (!isSuccess && current < retryNum) {
            try {
                minioClient.putObject(bucketName, objectName, stream, contentType);
                isSuccess = true;
            } catch (ErrorResponseException e) {
                log.warn("[minio] putObject stream, ErrorResponseException occur for time =" + current, e);
                current++;
            }
        }
        if (current == retryNum) {
            log.error("[minio] putObject, bucketName={}, objectName={}, failed finally!", bucketName, objectName);
        }
    }
}
