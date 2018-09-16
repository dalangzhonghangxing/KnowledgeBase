package edu.ecnu.kb.service;

import edu.ecnu.kb.model.Sentence;
import edu.ecnu.kb.model.SentenceRepository;
import edu.ecnu.kb.service.upload.SentenceRowProcessor;
import edu.ecnu.kb.service.upload.UploadProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SentenceService extends BaseService {
    @Autowired
    private SentenceRowProcessor rowProcessor;

    @Autowired
    private SentenceRepository repository;


    /**
     * 上传Sentence，返回上传成功的数量
     *
     * @param file
     * @param tag
     * @return Map<String       ,               Object>
     */
    public Map<String, Object> upload(InputStream file, String tag) {
        List<Sentence> sentences = new ArrayList<>();
        return uploadProcess(file, rowProcessor, tag, sentences, repository);
    }

    /**
     * 分页获取Sentence
     *
     * @param page
     * @param size
     * @return Page<Sentence>
     */
    public Page<Sentence> getByPage(Integer page, Integer size) {
        return repository.findAll(PageRequest.of(page - 1, size, SORT_ID_DESC));
    }

    /**
     * 删除指定句子，并返回分页结果
     *
     * @param id
     * @param page
     * @param size
     * @return
     */
    public Page<Sentence> delete(Long id, Integer page, Integer size) {
        repository.deleteById(id);
        return getByPage(page, size);
    }
}
