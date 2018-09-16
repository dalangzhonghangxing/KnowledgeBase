package edu.ecnu.kb.controller;

import edu.ecnu.kb.model.Knowledge;
import edu.ecnu.kb.service.KnowledgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.util.Map;

@RestController
public class KnowledgeController extends BaseController {
    final static String BASE_API = "/knowledge";

    @Autowired
    private KnowledgeService service;

    @RequestMapping(value = BASE_API + "/upload", method = RequestMethod.POST)
    public Map<String, Object> upload(MultipartHttpServletRequest req) {
        try {
            return service.upload(req.getFile("file").getInputStream(), req.getParameter("tag"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = BASE_API + "/page", method = RequestMethod.GET)
    public Page<Knowledge> getByPage(@RequestParam Integer page, @RequestParam Integer size) {
        return service.getByPage(page, size);
    }

    @RequestMapping(value = BASE_API + "/{id}", method = RequestMethod.DELETE)
    public Page<Knowledge> delete(@PathVariable Long id, @RequestParam Integer page, @RequestParam Integer size) {
        return service.delete(id,page, size);
    }
}
