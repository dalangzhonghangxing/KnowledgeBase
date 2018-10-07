package edu.ecnu.kb.service.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PythonUtil {

    private static final Logger LOG = LoggerFactory
            .getLogger(PythonUtil.class);

    @Value("${ecnu.kb.python-project-path}")
    private static String projectPath = "/Users/hang/PycharmProjects/Test/";

    /**
     * 执行python脚本。
     * <p>
     * 将执行结果保存到指定文件中，并记录文件名称。
     * <p>
     *
     * @param name 保存结果的文件名称
     * @param path 脚本所在路径
     */
    public static void execute(String name, String path) {

        Process proc;
        try {
            // 第一个参数用来指定版本
            String[] args = {"python3", projectPath + path};
            proc = Runtime.getRuntime().
                    exec(args);
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            BufferedInputStream err = new BufferedInputStream(proc.getErrorStream());
            BufferedReader errBr = new BufferedReader(new InputStreamReader(err));

            StringBuilder content = new StringBuilder();
            String line;

            // 将结果写入指定文件
            while ((line = in.readLine()) != null) {
                LOG.info(line);
                content.append(line.replace("\n", "")).append("\n");
            }
            File resultFile = FileUtil.getModelFile(name);
            BufferedWriter bw = new BufferedWriter(new FileWriter(resultFile));
            bw.write(content.substring(0, content.length() - 1));
            bw.close();

            // 将错误记录到日志
            while ((line = errBr.readLine()) != null) {
                LOG.error(line);
            }
            in.close();
            proc.waitFor();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据名称获取实验结果。
     * <p>
     * 每条结果记录的格式为中间用空格分隔，每个维度的数据以key:value的形式给出。例如key:value key:value
     * <p>
     * 如果以"#"开头，则是注释
     *
     * @param name 结果文件的名称
     * @return 返回一个Map，其格式为
     * {
     * <p>
     * key1:[val1,val2,...valn],
     * <p>
     * key2:[val1,val2,...valn]
     * <p>
     * ,.....}
     */
    public static Map<String, List<Object>> getResult(String name) {
        Map<String, List<Object>> result = new HashMap<>();
        try {
            File resultFile = FileUtil.getModelFile(name);
            BufferedReader br = new BufferedReader(new FileReader(resultFile));

            String line, values[];
            while ((line = br.readLine()) != null) {
                if (line.equals("") || line.startsWith("#")) continue;
                values = line.split("\\s+");
                for (String value : values) {
                    String key = value.split(":")[0];
                    String val = value.split(":")[1];
                    List<Object> list = result.getOrDefault(key, new ArrayList<>());
                    list.add(val);
                    result.put(key, list);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
