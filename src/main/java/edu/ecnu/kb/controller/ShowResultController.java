package edu.ecnu.kb.controller;

import edu.ecnu.kb.service.ShowResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
public class ShowResultController extends BaseController {

    final static String BASE_API = "/result";

    @Autowired
    private ShowResultService service;

    /**
     * 根据模型名称，获取loss与accuracy
     * @param modelName
     * @return
     */
    @RequestMapping(value = BASE_API + "/line", method = RequestMethod.GET)
    public Map<String, Object> upload(@RequestParam String modelName) {
        return service.getResultByModelName(modelName);
    }

    /**
     * 获取已有训练结果的模型名称
     * @return
     */
    @RequestMapping(value = BASE_API + "/modelNames", method = RequestMethod.GET)
    public Set<String> getModelNamesInResult() {
        return service.getModelNamesInResult();
    }

    /**
     * 获取指定模型的loss
     * @return
     */
    @RequestMapping(value = BASE_API + "/loss", method = RequestMethod.GET)
    public Map<String, Object> getLosses(@RequestParam String[] modelNames) {
        return service.getLosses(modelNames);
    }

    /**
     * 获取已有训练结果的模型名称
     * @return
     */
    @RequestMapping(value = BASE_API + "/accuracy", method = RequestMethod.GET)
    public Map<String, Object> getAccuracies(@RequestParam String[] modelNames) {
        return service.getAccuracies(modelNames);
    }

    /**
     * 获取已有训练结果的模型名称
     * @return
     */
    @RequestMapping(value = BASE_API + "/pr", method = RequestMethod.GET)
    public Map<String, Object> getPRs(@RequestParam String[] modelNames) {
        return service.getPRs(modelNames);
    }

    /**
     * 删除指定结果名称
     * @return
     */
    @RequestMapping(value = BASE_API , method = RequestMethod.DELETE)
    public Set<String> deleteResult(@RequestParam String modelName) {
        return service.deleteResult(modelName);
    }


}
