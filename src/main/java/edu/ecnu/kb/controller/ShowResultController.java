package edu.ecnu.kb.controller;

import edu.ecnu.kb.service.ShowResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class ShowResultController extends BaseController {

    final static String BASE_API = "/result";

    @Autowired
    private ShowResultService service;

    @RequestMapping(value = BASE_API + "/line", method = RequestMethod.GET)
    public Map<String, Object> upload(@RequestParam String modelName) {
        return service.getResultByModelName(modelName);
    }

    @RequestMapping(value = BASE_API + "/modelNames", method = RequestMethod.GET)
    public List<String> getModelNamesInResult() {
        return service.getModelNamesInResult();
    }
}
