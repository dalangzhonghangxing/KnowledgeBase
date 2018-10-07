package edu.ecnu.kb.controller;

import edu.ecnu.kb.service.PythonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PythonController extends BaseController {
    final static String BASE_API = "/python";

    @Autowired
    private PythonService service;

    @RequestMapping(value = BASE_API + "/test", method = RequestMethod.GET)
    public void test(@RequestParam String name) {
        service.test(name);
    }

}
