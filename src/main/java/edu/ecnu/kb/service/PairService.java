package edu.ecnu.kb.service;

import edu.ecnu.kb.model.*;
import edu.ecnu.kb.service.util.SessionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PairService extends BaseService {

    @Autowired
    private SentenceRepository sentenceRepository;

    @Autowired
    private KnowledgeRepository knowledgeRepository;

    @Autowired
    private PairRepository repository;

    private static final String[] columnForExport = {"knowledgeA", "knowledgeB", "relationName", "relationCode"};

    /**
     * 生成关系对，返回新生成的数量。
     * <p>
     * 支持进度条。
     *
     * @return Map
     */
    public Map<String, Object> generate(String tag) {
        // 通过按照id排序来避免多次生成的时候，会产生知识点相反的pair
        List<Sentence> sentences = sentenceRepository.findAll(SORT_ID_DESC);
        List<Knowledge> knowledges = knowledgeRepository.findAll(SORT_ID_DESC);
        List<Pair> pairs = repository.findAll(SORT_ID_DESC);
        SessionUtil.set(tag, 5);

        // 构建已存在的知识点set，提高判断是否存在的性能
        Set<Knowledge[]> existedPairs = new HashSet<>();
        for (Pair p : pairs) {
            Knowledge[] existedPair = new Knowledge[2];
            existedPair[0] = p.getKnowledgeA();
            existedPair[1] = p.getKnowledgeB();
            existedPairs.add(existedPair);
        }
        SessionUtil.set(tag, 10);

        // 为每句句子分词后的单词生一个wordset，从而提高查询性能
        List<Set<String>> wordSets = new ArrayList<>();
        for (Sentence sentence : sentences) {
            Set<String> wordSet = new HashSet<>(Arrays.asList(sentence.getSplited().split(" ")));
            wordSets.add(wordSet);
        }
        SessionUtil.set(tag, 15);

        // 遍历所有的知识点组合，将不存在的组合生成新的pair。
        // 因为知识点的顺序是总是按照id，有序地从数据库里面拿，因此不会重复。
        List<Pair> newPairs = new ArrayList<>();
        for (int i = 0; i < knowledges.size(); i++)
            for (int j = i + 1; j < knowledges.size(); j++) {
                Knowledge[] currentPair = {knowledges.get(i), knowledges.get(j)};
                if (existedPairs.contains(currentPair))
                    continue;
                Pair newPair = new Pair();
                newPair.setKnowledgeA(knowledges.get(i));
                newPair.setKnowledgeB(knowledges.get(j));
                for (int k = 0; k < wordSets.size(); k++) {
                    if (wordSets.get(k).contains(newPair.getKnowledgeA().getName())
                            && wordSets.get(k).contains(newPair.getKnowledgeB().getName())) {
                        newPair.getSentences().add(sentences.get(k));
                    }
                }
                if (newPair.getSentences().size() > 0)
                    newPairs.add(newPair);
                SessionUtil.set(tag, 70 * (i + 1) * j / (Math.pow(knowledges.size(), 2)));
            }
        //调用batchSave进行批量保存
        batchSave(newPairs, repository);
        SessionUtil.set(tag, 98);

        Map<String, Object> res = new HashMap<>();
        res.put("size", newPairs.size());
        return res;
    }

    /**
     * 分页
     *
     * @param page
     * @param size
     * @return
     */
    public Page<Pair> getByPage(Integer page, Integer size) {
        return getByPage(page, size, repository);
    }

    /**
     * 获取没有打过标签的pair
     *
     * @param page
     * @param size
     * @return
     */
    public Page<Pair> getUntagedPairs(Integer page, Integer size) {
        return repository.findUntagedPairs(PageRequest.of(page - 1, size, SORT_ID_DESC));
    }

    /**
     * 删除指定对象
     *
     * @param id
     * @param page
     * @param size
     * @return
     */
    public Page<Pair> delete(Long id, Integer page, Integer size) {
        repository.deleteById(id);
        return getByPage(page, size);
    }

    /**
     * 给指定pair打上id为relationId的关系
     *
     * @param id
     * @param relationId
     */
    public void tag(Long id, Long relationId) {
        Pair pair = repository.getOne(id);
        pair.setRelation(new Relation(relationId));
        repository.save(pair);
    }

    /**
     * 导出所有对象
     *
     * @return
     */
    public byte[] export() {
        List<Pair> pairs = repository.findAll(SORT_ID_DESC);
        List<Map<String, Object>> data = new ArrayList<>();
        for (Pair pair : pairs) {
            Map<String, Object> obj = new HashMap<>();
            obj.put(columnForExport[0], pair.getKnowledgeA());
            obj.put(columnForExport[1], pair.getKnowledgeB());
            obj.put(columnForExport[2], pair.getRelation().getName());
            obj.put(columnForExport[2], pair.getRelation().getCode());
            data.add(obj);
        }
        return export(columnForExport, data);
    }
}
