package edu.ecnu.kb.controller;

import edu.ecnu.kb.service.GlobalService;
import edu.ecnu.kb.service.KnowledgeService;
import edu.ecnu.kb.service.util.SessionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class GlobalController extends BaseController {
    final static String BASE_API = "/global";

    @Autowired
    private GlobalService service;

    /**
     * 根据tag获取进度
     *
     * @param tag
     * @return
     */
    @RequestMapping(value = BASE_API + "/progress", method = RequestMethod.GET)
    public int getProgerss(@RequestParam String tag) {
        Map<String, Object> res = new HashMap<>();
        return (int) SessionUtil.get(tag);
    }

}
