package edu.ecnu.kb.controller;

import edu.ecnu.kb.service.PairService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class PairController extends BaseController {
    final static String BASE_API = "/pair";

    @Autowired
    private PairService service;

    /**
     * 生成关系对，返回新生成的数量
     *
     * @return
     */
    @RequestMapping(value = BASE_API + "/generate", method = RequestMethod.POST)
    public Map<String, Object> generate() {
        return service.generate();
    }

}
