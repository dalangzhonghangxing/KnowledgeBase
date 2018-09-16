package edu.ecnu.kb.service;

import edu.ecnu.kb.model.Knowledge;
import edu.ecnu.kb.model.Relation;
import edu.ecnu.kb.model.RelationRepository;
import edu.ecnu.kb.model.Sentence;
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
public class RelationService extends BaseService {
    @Autowired
    private RelationRepository repository;

    @Autowired
    private KnowledgeRowProcessor rowProcessor;

    public Map<String, Object> upload(InputStream file, String tag) {
        List<Knowledge> knowledges = new ArrayList<>();
        return uploadProcess(file, rowProcessor, tag, knowledges, repository);
    }

    public Page<Relation> getByPage(Integer page, Integer size) {
        return repository.findAll(PageRequest.of(page - 1, size, SORT_ID_DESC));
    }

    public Page<Relation> delete(Long id, Integer page, Integer size) {
        repository.deleteById(id);
        return getByPage(page, size);
    }
}
