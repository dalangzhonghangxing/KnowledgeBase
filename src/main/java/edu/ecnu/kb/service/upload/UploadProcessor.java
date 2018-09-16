package edu.ecnu.kb.service.upload;

import edu.ecnu.kb.service.util.SessionUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 用于解析上传文件，只支持csv格式，中间使用#分割。
 *
 * @author
 */
public class UploadProcessor {
    /**
     * 将inputStream解析成List<List<String>>格式的数据。
     *
     * @param file
     * @return
     */
    private static List<String[]> parseFile(InputStream file) {
        List<String[]> values = null;
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(file,
                        "UTF-8"))) {
            String line = "";
            values = new ArrayList<>();

            while ((line = br.readLine()) != null)
                values.add(line.split("#"));

        } catch (IOException e) {
            e.printStackTrace();
        }
        return values;
    }

    /**
     * 将一个csv文件解析成对象列表
     *
     * @param file         文件
     * @param rowProcessor 对每一行的解析
     * @param tag          放到Session中的key，用于查询进度
     * @return
     */
    public static void process(InputStream file, RowProcessor rowProcessor, String tag, List target) {
        List<String[]> lines = parseFile(file);
        SessionUtil.set(tag, 10);
        if (lines == null) return;
        Object lineObject;

        int index = 0;
        for (String[] line : lines) {
            index++;
            lineObject = rowProcessor.processor(line);
            if (lineObject != null)
                target.add(lineObject);
            SessionUtil.set(tag, 10 + (index * 90) / lines.size());
        }
    }
}
