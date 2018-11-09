package edu.ecnu.kb.service;

import edu.ecnu.kb.service.util.FileUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用加载模型训练结果
 */
@Service
public class ShowResultService {

    @Value("${ecnu.kb.python-project-path}")
    private String projectPath;

    private String resultPath = "data/result/";

    /**
     * 根据模型的名称，加载训练中间结果。用于画折线图。
     * <p>
     * 结果文件以每4行一个单位，循环读取，顺序依次为train_losses, test_losses, train_accuracies, test_accuracies
     *
     * @param modelName
     * @return
     */
    public Map<String, Object> getResultByModelName(String modelName) {
        List<String> lines = FileUtil.readFile(projectPath + resultPath + modelName + ".result");

        int i = 0;
        List<Double> train_losses = new ArrayList<>();
        List<Double> test_losses = new ArrayList<>();
        List<Double> train_accuracies = new ArrayList<>();
        List<Double> test_accuracies = new ArrayList<>();
        while (i < lines.size()) {
            train_losses.addAll(parseStringToDoubles(lines.get(i)));
            test_losses.addAll(parseStringToDoubles(lines.get(i + 1)));
            train_accuracies.addAll(parseStringToDoubles(lines.get(i + 2)));
            test_accuracies.addAll(parseStringToDoubles(lines.get(i + 3)));
            i += 4;
        }
        Map<String, Object> res = new HashMap<>();

        // 封装series
        List<Map<String, Object>> lossSeries = new ArrayList<>();
        List<Map<String, Object>> accuracySeries = new ArrayList<>();
        lossSeries.add(getSeries("train", "line", train_losses));
        lossSeries.add(getSeries("test", "line", test_losses));
        accuracySeries.add(getSeries("train", "line", train_accuracies));
        accuracySeries.add(getSeries("test", "line", test_accuracies));

        //封装legendData
        List<String> legendData = new ArrayList<>();
        legendData.add("train");
        legendData.add("test");

        res.put("legendData", legendData);
        res.put("lossSeries", lossSeries);
        res.put("accuracySeries", accuracySeries);

        return res;

    }

    /**
     * 封装折线图的一个series
     *
     * @param name
     * @param type
     * @param data
     * @return
     */
    private Map<String, Object> getSeries(String name, String type, List data) {
        Map<String, Object> res = new HashMap<>();
        res.put("name", name);
        res.put("type", type);
        res.put("data", data);
        return res;
    }

    /**
     * 将一行String转化为List\<Double\>
     *
     * @param line
     * @return
     */
    private List<Double> parseStringToDoubles(String line) {
        String[] values = line.split(" ");
        List<Double> res = new ArrayList<>(values.length);

        for (String d : values) {
            try {
                res.add(Double.valueOf(d));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return res;
    }

    /**
     * 获取result目录下，所有文件名称。
     */
    public List<String> getModelNamesInResult() {
        List<String> res = new ArrayList<>();
        for (String fileName : FileUtil.getFileNames(projectPath + resultPath)) {
            res.add(fileName.split("\\.")[0]);
        }
        return res;
    }
}
