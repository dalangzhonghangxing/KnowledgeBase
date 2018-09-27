package edu.ecnu.kb.controller;

import edu.ecnu.kb.model.Relation;
import edu.ecnu.kb.model.Sentence;
import edu.ecnu.kb.service.BaseService;
import edu.ecnu.kb.service.RelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.util.Map;

@RestController
public class RelationController extends BaseController {
    final static String BASE_API = "/relation";

    @Autowired
    private RelationService service;

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
    public Page<Relation> getByPage(@RequestParam Integer page,
                                    @RequestParam Integer size) {
        return service.getByPage(page, size);

    }

    @RequestMapping(value = BASE_API + "/{id}", method = RequestMethod.DELETE)
    public Page<Relation> delete(@PathVariable Long id, @RequestParam Integer page,
                                 @RequestParam Integer size) {
        return service.delete(id, page, size);

    }

    @RequestMapping(value = BASE_API + "/{id}", method = RequestMethod.POST)
    public void save(@PathVariable Long id, @RequestBody Map<String, Object> toSaveMap) {
        service.save(id, toSaveMap);
    }

    /**
     * 导出所有对象
     * @return
     */
    @RequestMapping(value = BASE_API + "/export", method = RequestMethod.GET)
    public byte[] export(){
        return service.export();
    }
}
