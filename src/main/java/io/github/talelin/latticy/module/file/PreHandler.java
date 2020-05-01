package io.github.talelin.latticy.module.file;

/**
 * 文件前预处理器
 *
 * @author pedro@TaleLin
 */
public interface PreHandler {

    /**
     * 在文件写入本地或者上传到云之前调用此方法
     *
     * @return 是否上传，若返回false，则不上传
     */
    boolean handle(File file);
}
