package edu.ecnu.kb.service;

import edu.ecnu.kb.model.Sentence;
import edu.ecnu.kb.model.SentenceRepository;
import edu.ecnu.kb.service.upload.SentenceRowProcessor;
import edu.ecnu.kb.service.util.SessionUtil;
import edu.ecnu.kb.service.util.SplitWordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

    private static final String[] columnForExport = {"original"};

    /**
     * 上传Sentence，返回上传成功的数量
     *
     * @param file
     * @param tag
     * @return
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
        return getByPage(page, size, repository);
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


    /**
     * 将所有句子进行分词
     *
     * @param tag 进度条查询tag
     */
    public void split(String tag) {
        List<Sentence> sentences = repository.findAll();
        SessionUtil.set(tag, 10);

        int index = 0;
        for (Sentence sentence : sentences) {
            sentence.setSplited(SplitWordUtil.split(sentence.getOriginal()));
            SessionUtil.setProgress(tag, 10, index++, sentences.size(), 85);
        }

        batchSave(sentences, repository);
        SessionUtil.set(tag, 100);
    }

    /**
     * 保存一个对象。如果id为0，则新增；否则是保存。保存之前会重新分词。
     *
     * @param id
     * @param toSaveMap
     */
    public void save(Long id, Map<String, Object> toSaveMap) {
        Sentence sentence;
        if (id != 0)
            sentence = repository.getOne(id);
        else
            sentence = new Sentence();
        setNewValue(Sentence.class, sentence, toSaveMap);

        // 重新分词
        sentence.setSplited(SplitWordUtil.split(sentence.getOriginal()));
        repository.save(sentence);
    }

    /**
     * 导出所有对象
     *
     * @return
     */
    public byte[] export() {
        List<Sentence> sentences = repository.findAll(SORT_ID_DESC);
        List<Map<String, Object>> data = new ArrayList<>();
        for (Sentence sentence : sentences) {
            Map<String, Object> obj = new HashMap<>();
            obj.put(columnForExport[0], sentence.getOriginal());
            data.add(obj);
        }
        return export(columnForExport, data);
    }
}
