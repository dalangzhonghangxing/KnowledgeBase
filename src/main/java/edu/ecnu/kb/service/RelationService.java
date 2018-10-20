package edu.ecnu.kb.service;

import edu.ecnu.kb.model.Knowledge;
import edu.ecnu.kb.model.Relation;
import edu.ecnu.kb.model.RelationRepository;
import edu.ecnu.kb.service.upload.RelationRowProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RelationService extends BaseService {
    @Autowired
    private RelationRepository repository;

    @Autowired
    private RelationRowProcessor rowProcessor;

    private static final String[] columnForExport = {"code", "name", "example"};

    public Map<String, Object> upload(InputStream file, String tag) {
        List<Knowledge> knowledges = new ArrayList<>();
        return uploadProcess(file, rowProcessor, tag, knowledges, repository);
    }

    public Page<Relation> getByPage(Integer page, Integer size) {
        return getByPage(page, size, repository);
    }

    public Page<Relation> delete(Long id, Integer page, Integer size) {
        repository.deleteById(id);
        return getByPage(page, size);
    }

    public void save(Long id, Map<String, Object> toSaveMap) {
        Relation relation;
        if (id != 0)
            relation = repository.getOne(id);
        else
            relation = new Relation();
        setNewValue(Relation.class, relation, toSaveMap);
        repository.save(relation);
    }

    /**
     * 导出所有对象
     *
     * @return
     */
    public byte[] export() {
        List<Relation> relations = repository.findAll(SORT_ID_DESC);
        List<Map<String, Object>> data = new ArrayList<>();
        for (Relation relation : relations) {
            Map<String, Object> obj = new HashMap<>();
            obj.put(columnForExport[0], relation.getCode());
            obj.put(columnForExport[1], relation.getName());
            obj.put(columnForExport[2], relation.getExample());
            data.add(obj);
        }
        return export(columnForExport, data);
    }

    /**
     * 获取所有关系
     * @return
     */
    public List<Map<String, Object>> getAll() {
        List<Map<String, Object>> res = new ArrayList<>();
        List<Relation> relations = repository.findAll(SORT_ID_DESC);
        for (Relation relation : relations) {
            Map<String, Object> one = new HashMap<>();
            one.put("id", relation.getId());
            one.put("name", relation.getName());
            one.put("example", relation.getExample());
            res.add(one);
        }
        return res;
    }
}
