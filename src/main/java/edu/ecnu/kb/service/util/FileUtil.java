package edu.ecnu.kb.service.util;

import edu.ecnu.kb.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 文件操作工具类
 */
@Service
public class FileUtil {

    private static final Logger LOG = LoggerFactory
            .getLogger(FileUtil.class);

    @Value("${ecnu.kb.filesystem}")
    private String PATH;

    private static File PIC_SUBDIR;

    private static File MODEL_SUBDIR;

    private static File TEMP_SUBDIR;


    @Autowired
    public void initDirs() {
        PIC_SUBDIR = new File(PATH, "pictures");
        if (!PIC_SUBDIR.exists())
            PIC_SUBDIR.mkdirs();

        MODEL_SUBDIR = new File(PATH, "models");
        if (!MODEL_SUBDIR.exists())
            MODEL_SUBDIR.mkdirs();

        TEMP_SUBDIR = new File(PATH, "tmp");
        if (!TEMP_SUBDIR.exists())
            TEMP_SUBDIR.mkdirs();

    }

    /**
     * 获取在Tmp文件夹的一个文件，如果没有则会创建。
     *
     * @param fileName
     * @return
     */
    public static File getTmpFile(String fileName) {
        return getFile(TEMP_SUBDIR, fileName);
    }

    /**
     * 删除临时文件夹下的指定文件
     *
     * @param fileName
     */
    public static void removeTmpFile(String fileName) {
        removeFile(TEMP_SUBDIR, fileName);
    }

    /**
     * 获取在Model文件夹的一个文件，如果没有则会创建。
     *
     * @param fileName
     * @return
     */
    public static File getModelFile(String fileName) {
        return getFile(MODEL_SUBDIR, fileName);
    }

    /**
     * 删除Model文件夹下的指定文件
     *
     * @param fileName
     */
    public static void removeModelFile(String fileName) {
        removeFile(MODEL_SUBDIR, fileName);
    }


    /**
     * 获取指定目录下的一个文件，如果不存在则创建
     *
     * @param dir      目录
     * @param fileName 文件名称
     * @return
     */
    private static File getFile(File dir, String fileName) {
        File file = new File(dir, fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new BusinessException("文件创建异常：" + dir.getName() + "/" + fileName);
            }
        }
        return file;
    }

    /**
     * 删除指定目录下的某个文件
     *
     * @param dir      目录
     * @param fileName 文件名称
     */
    private static void removeFile(File dir, String fileName) {
        File file = new File(dir, fileName);
        if (file.exists())
            file.delete();
    }

    /**
     * 获取指定目录下所有文件
     *
     * @param dir
     * @return
     */
    private static File[] getFileList(File dir) {
        return dir.listFiles();
    }

    /**
     * 覆盖文件，如果文件不存在会新建。
     *
     * @param filePath 文件路径
     * @param content  文件内容
     */
    public static void overrideFile(String filePath, String content) {
        writeFile(filePath, content, false);
    }

    /**
     * 追加文件，如果文件不存在会新建。
     *
     * @param filePath 文件路径
     * @param content  要追加的内容
     */
    public static void appendFile(String filePath, String content) {
        writeFile(filePath, content, true);
    }

    /**
     * 覆盖制定文件。
     *
     * @param filePath 文件路径
     * @param content  文件内容
     * @param append   是否追加
     */
    private static void writeFile(String filePath, String content, boolean append) {
        File file = new File(filePath);

        if (!file.getParentFile().exists())
            file.getParentFile().mkdirs();
        try {
            if (!file.exists())
                file.createNewFile();

            BufferedWriter out = new BufferedWriter(new FileWriter(file, append));
            out.write(content);
            out.close();
        } catch (IOException e) {
            LOG.error("写文件异常：" + filePath + "\t" + e.getMessage());
            e.printStackTrace();
        }
    }
}
