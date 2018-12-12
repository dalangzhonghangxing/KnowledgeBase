package edu.ecnu.kb.service;

import edu.ecnu.kb.service.util.FileUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;

/**
 * 用加载模型训练结果
 */
@Service
public class ShowResultService {

    @Value("${ecnu.kb.python-project-path}")
    private String projectPath;

    private String resultPath = "data/result/";

    private final static int TRAIN_LOSS = 0;
    private final static int TEST_LOSS = 1;
    private final static int TRAIN_ACCURACY = 2;
    private final static int TEST_ACCURACY = 3;

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

            }
        }

        return res;
    }

    /**
     * 获取result目录下，所有文件名称。
     */
    public Set<String> getModelNamesInResult() {
        Set<String> res = new HashSet<>();
        for (String fileName : FileUtil.getFileNames(projectPath + resultPath)) {
            res.add(fileName.split("\\.")[0]);
        }
        return res;
    }

    /**
     * 根据模型的名称，获取多个模型的测试集loss对比折现图数据
     */
    public Map<String, Object> getLosses(String[] modelNames) {
        // 将modelNames作为legendData
        List<String> legendData = Arrays.asList(modelNames);

        // 封装series
        List<Map<String, Object>> series = new ArrayList<>();
        for (String modelName : modelNames) {
            series.add(getSeries(modelName, "line", getTestLoss(modelName)));
        }

        Map<String, Object> res = new HashMap<>();
        res.put("legendData", legendData);
        res.put("series", series);
        return res;
    }

    /**
     * 根据模型的名称，获取多个模型的测试集accuracy对比折现图数据
     */
    public Map<String, Object> getAccuracies(String[] modelNames) {
        // 将modelNames作为legendData
        List<String> legendData = Arrays.asList(modelNames);

        // 封装series
        List<Map<String, Object>> series = new ArrayList<>();
        for (String modelName : modelNames) {
            series.add(getSeries(modelName, "line", getTestAccuracy(modelName)));
        }

        Map<String, Object> res = new HashMap<>();
        res.put("legendData", legendData);
        res.put("series", series);
        return res;
    }

    /**
     * 获取一个模型的loss
     *
     * @param modelName
     * @return
     */
    private List<Double> getTestLoss(String modelName) {
        List<String> lines = FileUtil.readFile(projectPath + resultPath + modelName + ".result");
        return getDataByType(lines, TEST_LOSS);
    }

    /**
     * 获取一个模型的loss
     *
     * @param modelName
     * @return
     */
    private List<Double> getTestAccuracy(String modelName) {
        List<String> lines = FileUtil.readFile(projectPath + resultPath + modelName + ".result");
        return getDataByType(lines, TEST_ACCURACY);
    }

    /**
     * 根据数据类型，返回某个数据
     *
     * @param lines
     * @param type
     * @return
     */
    private List<Double> getDataByType(List<String> lines, int type) {
        List<Double> res = new ArrayList<>();
        while (type < lines.size()) {
            res.addAll(parseStringToDoubles(lines.get(type)));
            type += 4;
        }
        return res;
    }

    /**
     * 删除指定result
     *
     * @param modelName
     * @return
     */
    public Set<String> deleteResult(String modelName) {
        File dir = new File(projectPath + resultPath);
        FileUtil.removeFile(dir, modelName + ".result");
        FileUtil.removeFile(dir, modelName + ".pr");
        return getModelNamesInResult();
    }

    /**
     * 根据模型的名称，获取多个模型的测试集precision-recall对比折现图数据
     */
    public Map<String, Object> getPRs(String[] modelNames) {
        // 将modelNames作为legendData
        List<String> legendData = Arrays.asList(modelNames);

        // 封装series
        List<Map<String, Object>> series = new ArrayList<>();
        for (String modelName : modelNames) {
            series.add(getSeries(modelName, "line", getPR(modelName)));
        }

        Map<String, Object> res = new HashMap<>();
        res.put("legendData", legendData);
        res.put("series", series);
        return res;
    }


    /**
     * 根据modelName获取一个模型的Precision-Recall值
     * @param modelName
     * @return
     */
    private List<Double> getPR(String modelName) {
        List<String> lines = FileUtil.readFile(projectPath + resultPath + modelName + ".pr");
        if(lines.size() == 0)
            return new ArrayList<>();
        String[] values = lines.get(0).split(" ");
        List<Double> res = new ArrayList<>();
        for (int i = 0; i < values.length; i += 10) {
            res.add(Double.valueOf(values[i]));
        }

        return res;
    }
}
