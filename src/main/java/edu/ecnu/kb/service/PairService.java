package edu.ecnu.kb.service;

import edu.ecnu.kb.model.*;
import org.springframework.beans.factory.annotation.Autowired;
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

    /**
     * 生成关系对，返回新生成的数量。
     * <p>
     * 1. 获取所有的sentence
     * <p>
     * 2. 获取所有Knowledge
     * <p>
     * 3. 获取所有已经存在的知识点对
     *
     * @return Map
     */
    public Map<String, Object> generate() {
        // 通过按照id排序来避免多次生成的时候，会产生知识点相反的pair
        List<Sentence> sentences = sentenceRepository.findAll(SORT_ID_DESC);
        List<Knowledge> knowledges = knowledgeRepository.findAll(SORT_ID_DESC);
        List<Pair> pairs = repository.findAll(SORT_ID_DESC);

        // 构建已存在的知识点set，提高判断是否存在的性能
        Set<Knowledge[]> existedPairs = new HashSet<>();
        for (Pair p : pairs) {
            Knowledge[] existedPair = new Knowledge[2];
            existedPair[0] = p.getKnowledgeA();
            existedPair[1] = p.getKnowledgeB();
            existedPairs.add(existedPair);
        }

        // 遍历所有的知识点组合，将不存在的组合生成新的pair。
        List<Pair> newPairs = new ArrayList<>();
        for (int i = 0; i < knowledges.size(); i++)
            for (int j = i + 1; j < knowledges.size(); j++) {
                Knowledge[] currentPair = {knowledges.get(i), knowledges.get(j)};
                if (existedPairs.contains(currentPair))
                    continue;
                // TODO: 是否修改模型，增加单词表，将sentence分词后的单词从单词表中加载。类似倒排索引，来提高效率。
            }

        //
        return null;
    }
}
