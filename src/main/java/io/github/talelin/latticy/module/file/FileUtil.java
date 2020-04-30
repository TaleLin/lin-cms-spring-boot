package io.github.talelin.latticy.module.file;

import org.springframework.util.DigestUtils;
import org.springframework.util.unit.DataSize;

import java.io.File;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;

/**
 * @author pedro@TaleLin
 */
public class FileUtil {

    public static FileSystem getDefaultFileSystem() {
        return FileSystems.getDefault();
    }

    public static boolean isAbsolute(String str) {
        Path path = getDefaultFileSystem().getPath(str);
        return path.isAbsolute();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void initStoreDir(String dir) {
        String absDir;
        if (isAbsolute(dir)) {
            absDir = dir;
        } else {
            String cmd = getCmd();
            Path path = getDefaultFileSystem().getPath(cmd, dir);
            absDir = path.toAbsolutePath().toString();
        }
        File file = new File(absDir);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public static String getCmd() {
        return System.getProperty("user.dir");
    }

    public static String getFileAbsolutePath(String dir, String filename) {
        if (isAbsolute(dir)) {
            return getDefaultFileSystem()
                    .getPath(dir, filename)
                    .toAbsolutePath().toString();
        } else {
            return getDefaultFileSystem()
                    .getPath(getCmd(), dir, filename)
                    .toAbsolutePath().toString();
        }
    }

    public static String getFileExt(String filename) {
        int index = filename.lastIndexOf('.');
        return filename.substring(index);
    }

    public static String getFileMD5(byte[] bytes) {
        return DigestUtils.md5DigestAsHex(bytes);
    }

    public static Long parseSize(String size) {
        DataSize singleLimitData = DataSize.parse(size);
        return singleLimitData.toBytes();
    }
}
