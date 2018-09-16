package edu.ecnu.kb.controller;

import edu.ecnu.kb.model.Sentence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import edu.ecnu.kb.service.SentenceService;

import java.io.IOException;
import java.util.Map;

@RestController
public class SentenceController extends BaseController {

    final static String BASE_API = "/sentence";

    @Autowired
    private SentenceService service;

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
    public Page<Sentence> getByPage(@RequestParam Integer page,
                                    @RequestParam Integer size) {
        return service.getByPage(page, size);

    }

    @RequestMapping(value = BASE_API + "/{id}", method = RequestMethod.DELETE)
    public Page<Sentence> delete(@PathVariable Long id, @RequestParam Integer page,
                                 @RequestParam Integer size) {
        return service.delete(id, page, size);

    }


}
