package edu.ecnu.kb.controller;

import com.alibaba.fastjson.JSON;
import edu.ecnu.kb.model.Pair;
import edu.ecnu.kb.service.PairService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
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
    public Map<String, Object> generate(@RequestBody String tag) {
        return service.generate(JSON.parseObject(tag).getString("tag"));
    }

    /**
     * 将已有关系对于句子关联起来
     */
    @RequestMapping(value = BASE_API + "/sentences", method = RequestMethod.POST)
    public Map<String, Object> accocateSentences(@RequestBody String tag) {
        return service.accocateSentences(JSON.parseObject(tag).getString("tag"));
    }


    /**
     * 分页获取
     *
     * @param page
     * @param size
     * @return
     */
    @RequestMapping(value = BASE_API + "/page", method = RequestMethod.GET)
    public Page<Pair> getByPage(@RequestParam Integer page, @RequestParam Integer size,
                                @RequestParam String condition) {
        return service.getByPageWithCondition(page, size, condition);
    }

    /**
     * 获取基本信息
     *
     * @return
     */
    @RequestMapping(value = BASE_API + "/info", method = RequestMethod.GET)
    public Map<String, Object> getInfo() {
        return service.getInfo();
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

    /**
     * 导出所有对象
     */
    @RequestMapping(value = BASE_API + "/export", method = RequestMethod.GET)
    public byte[] export() {
        return service.export();
    }

    /**
     * 生成数据集
     */
    @RequestMapping(value = BASE_API + "/generate-dataset", method = RequestMethod.POST)
    public Map<String, Object> generateDataset() {
        return service.generateDataset();
    }

    /**
     * 根据Id查找Pair
     */
    @RequestMapping(value = BASE_API + "/{id}", method = RequestMethod.GET)
    public Pair getById(@PathVariable long id) {
        return service.getById(id);
    }

    /**
     * 根据id，获取pair的关系图
     */
    @RequestMapping(value = BASE_API + "/graph/{id}", method = RequestMethod.GET)
    public Map<String, Object> getGraph(@PathVariable long id) {
        return service.getGraph(id);
    }

    /**
     * 获取整个知识体系的关系图
     */
    @RequestMapping(value = BASE_API + "/graph", method = RequestMethod.GET)
    public Map<String, Object> getGraph(@RequestParam Long[] relationIds) {
        return service.getGraph(relationIds);
    }

    @RequestMapping(value = BASE_API + "/upload", method = RequestMethod.POST)
    public Map<String, Object> upload(MultipartHttpServletRequest req) {
        try {
            return service.upload(req.getFile("file").getInputStream(), req.getParameter("tag"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取柱状图分析数据
     *
     * @return
     */
    @RequestMapping(value = BASE_API + "/count", method = RequestMethod.GET)
    public Map<String, Object> getBarData() {
        return service.getBarData();
    }
}
