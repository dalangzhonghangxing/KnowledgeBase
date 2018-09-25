package edu.ecnu.kb.service;

import edu.ecnu.kb.model.Knowledge;
import edu.ecnu.kb.model.KnowledgeRepository;
import edu.ecnu.kb.service.upload.KnowledgeRowProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class KnowledgeService extends BaseService {

    @Autowired
    private KnowledgeRepository repository;

    @Autowired
    private KnowledgeRowProcessor rowProcessor;

    /**
     * 上传
     *
     * @param file
     * @param tag
     * @return
     */
    public Map<String, Object> upload(InputStream file, String tag) {
        List<Knowledge> knowledges = new ArrayList<>();
        return uploadProcess(file, rowProcessor, tag, knowledges, repository);
    }

    public Page<Knowledge> getByPage(Integer page, Integer size) {
        return getByPage(page, size, repository);
    }

    public Page<Knowledge> delete(Long id, Integer page, Integer size) {
        repository.deleteById(id);
        return getByPage(page, size);
    }

    public void save(Long id, Map<String, Object> toSaveMap) {
        Knowledge knowledge;
        if (id != 0)
            knowledge = repository.getOne(id);
        else
            knowledge = new Knowledge();
        setNewValue(Knowledge.class, knowledge, toSaveMap);
        repository.save(knowledge);
    }
}
