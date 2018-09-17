package edu.ecnu.kb.controller;

import edu.ecnu.kb.model.Pair;
import edu.ecnu.kb.service.PairService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

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
    public Map<String, Object> generate(@RequestParam String tag) {
        return service.generate(tag);
    }

    /**
     * 分页获取
     *
     * @param page
     * @param size
     * @return
     */
    @RequestMapping(value = BASE_API + "/page", method = RequestMethod.GET)
    public Page<Pair> getByPage(@RequestParam Integer page, @RequestParam Integer size) {
        return service.getByPage(page, size);
    }

    /**
     * 打标签
     */
    @RequestMapping(value = BASE_API + "/{id}", method = RequestMethod.POST)
    public void tag(@PathVariable Long id, @RequestParam Long relationId) {
        service.tag(id, relationId);
    }

    /**
     * 删除指定对象
     *
     * @param id
     * @param page
     * @param size
     * @return
     */
    @RequestMapping(value = BASE_API + "/{id}", method = RequestMethod.DELETE)
    public Page<Pair> delete(@PathVariable Long id, @RequestParam Integer page,
                             @RequestParam Integer size) {
        return service.delete(id, page, size);

    }

    /**
     * 分页查询没有打标签的pair
     *
     * @param page
     * @param size
     * @return
     */
    @RequestMapping(value = BASE_API + "/untaged", method = RequestMethod.GET)
    public Page<Pair> getUntagedPairs(@RequestParam Integer page,
                                      @RequestParam Integer size) {
        return service.getUntagedPairs(page, size);

    }
}
