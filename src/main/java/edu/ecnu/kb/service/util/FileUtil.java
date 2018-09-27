package edu.ecnu.kb.service.util;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

/**
 * 文件操作工具类
 */
@Service
public class FileUtil {

    @Value("${ecnu.kb.filesystem}")
    protected String PATH;

    protected static File PIC_SUBDIR;

    protected static File DOC_SUBDIR;

    protected static File TEMP_SUBDIR;


    @Autowired
    public void initDirs() {
        PIC_SUBDIR = new File(PATH, "pictures");
        if (!PIC_SUBDIR.exists())
            PIC_SUBDIR.mkdirs();

        DOC_SUBDIR = new File(PATH, "docs");
        if (!DOC_SUBDIR.exists())
            DOC_SUBDIR.mkdirs();

        TEMP_SUBDIR = new File(PATH, "tmp");
        if (!TEMP_SUBDIR.exists())
            TEMP_SUBDIR.mkdirs();

    }

    /**
     * 在临时文件夹下创建一个文件并返回
     * @param fileName
     * @return
     */
    public static File createTmpFile(String fileName) {
        File file = new File(TEMP_SUBDIR, fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                return null;
            }
        }
        return file;
    }

    /**
     * 删除临时文件夹下的指定发文件
     * @param fileName
     */
    public static void removeTmpFile(String fileName) {
        File file = new File(TEMP_SUBDIR, fileName);
        if (file.exists())
            file.delete();
    }


    public static File getPIC_SUBDIR() {
        return PIC_SUBDIR;
    }

    public static File getDOC_SUBDIR() {
        return DOC_SUBDIR;
    }

    public static File getTEMP_SUBDIR() {
        return TEMP_SUBDIR;
    }

}
