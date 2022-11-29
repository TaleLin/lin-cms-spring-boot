package io.github.talelin.latticy.module.file;

/**
 * 上传预处理器
 *
 * @author pedro@TaleLin
 */
public interface UploadHandler {

    /**
     * 在文件写入本地或者上传到云之前调用此方法
     *
     * @param file 文件对象
     * @return 是否上传，若返回false，则不上传
     */
    boolean preHandle(File file);

    /**
     * 在文件写入本地或者上传到云之后调用此方法
     *
     * @param file 文件对象
     */
    void afterHandle(File file);
}
