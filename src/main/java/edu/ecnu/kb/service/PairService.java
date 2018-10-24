package edu.ecnu.kb.service;

import com.alibaba.fastjson.JSON;
import edu.ecnu.kb.model.*;
import edu.ecnu.kb.service.util.FileUtil;
import edu.ecnu.kb.service.util.SessionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class PairService extends BaseService {

    @Value("${ecnu.kb.dataset-path}")
    private String datasetPath;

    @Autowired
    private SentenceRepository sentenceRepository;

    @Autowired
    private KnowledgeRepository knowledgeRepository;

    @Autowired
    private PairRepository repository;

    @Autowired
    private RelationRepository relationRepository;

    @Autowired
    private KnowledgeService knowledgeService;

    private static final Sort SORT_SEQ_ASC = new Sort(Sort.Direction.ASC, "seq");

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
            if (sentence.getSplited() != null) {
                Set<String> wordSet = new HashSet<>(Arrays.asList(sentence.getSplited().split("\\s+")));
                wordSets.add(wordSet);
            } else
                wordSets.add(new HashSet<>());
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
                // 先查询数据库中是否存在该知识对，如果不存在则新增，如果存在则判断句子是否都合法。
                Pair newPair = repository.findByKnowledgeAAndKnowledgeB(knowledges.get(i), knowledges.get(j));
                if (newPair == null) {
                    newPair = new Pair();
                    newPair.setKnowledgeA(knowledges.get(i));
                    newPair.setKnowledgeB(knowledges.get(j));
                } else {
                    // 如果pair已存在，检查已有句子是否合法。
                    newPair = checkSentence(newPair);
                }
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
     * 检查pair的句子是否合法。
     * <p>
     * 如果不合法，则将句子删除。如果句子的数量为0，则将这个知识点删除，并返回一个新的对象。
     *
     * @param pair
     * @return
     */
    @Transactional
    public Pair checkSentence(Pair pair) {
        Set validedSentence = new HashSet();
        for (Sentence sentence : pair.getSentences()) {
            Set set = new HashSet<>(Arrays.asList(sentence.getSplited().split("\\s+")));
            if (set.contains(pair.getKnowledgeA().getName()) &&
                    set.contains(pair.getKnowledgeB().getName()))
                validedSentence.add(sentence);
        }
        if (validedSentence.size() == 0) {
            Pair newPair = new Pair();
            newPair.setKnowledgeA(pair.getKnowledgeA());
            newPair.setKnowledgeB(pair.getKnowledgeB());
            newPair.setRelation(pair.getRelation());
            repository.delete(pair);
            return newPair;
        }
        pair.setSentences(validedSentence);
        return pair;
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
     * 带有条件的分页查询
     *
     * @param page
     * @param size
     * @param condition
     * @return
     */
    public Page<Pair> getByPageWithCondition(Integer page, Integer size, String condition) {
        Pair pair = new Pair();
        for (Map.Entry<String, Object> entry : ((Map<String, Object>) JSON.parse(condition)).entrySet()) {
            if (entry.getValue() == null || entry.getValue().equals(""))
                continue;
            if (entry.getKey().equals("knowledgeA")) {
                pair.setKnowledgeA(knowledgeRepository.findByName(entry.getValue().toString()));
            } else if (entry.getKey().equals("knowledgeB")) {
                pair.setKnowledgeB(knowledgeRepository.findByName(entry.getValue().toString()));
            } else if (entry.getKey().equals("relation")) {
                pair.setRelation(relationRepository.getOne(Long.parseLong(entry.getValue().toString())));
            }
        }

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnorePaths("id")
                .withIgnorePaths("seq");

        return repository.findAll(Example.of(pair, matcher), PageRequest.of(page - 1, size, SORT_ID_DESC));
    }

    /**
     * 获取没有打过标签的pair，根据seq排序，获取之后将seq+1。
     *
     * @param page
     * @param size
     * @return
     */
    public Page<Pair> getUntagedPairs(Integer page, Integer size) {
        Page<Pair> res = repository.findUntagedPairs(PageRequest.of(page - 1, size, SORT_SEQ_ASC));
        List<Pair> pairs = res.getContent();
        for (Pair pair : pairs)
            pair.setSeq(pair.getSeq() + 1);
        repository.saveAll(pairs);
        return res;
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
            obj.put(columnForExport[0], pair.getKnowledgeA().getName());
            obj.put(columnForExport[1], pair.getKnowledgeB().getName());
            if (pair.getRelation() != null) {
                obj.put(columnForExport[2], pair.getRelation().getName());
                obj.put(columnForExport[3], pair.getRelation().getCode());
            } else {
                obj.put(columnForExport[2], "");
                obj.put(columnForExport[3], "");
            }
            data.add(obj);
        }
        return export(columnForExport, data);
    }

    /**
     * 生成数据集，只生成已标注的关系对。
     * <p>
     * 数据集格式为 原句 knowledgeA knowledgeB relationName relationCode\n
     *
     * @return 生成的数据集数量
     */
    public Map<String, Object> generateDataset() {
        List<Pair> tagedPairs = repository.findTagedPairs();
        StringBuilder content = new StringBuilder();
        int size = 0;

        for (Pair tagedPair : tagedPairs) {
            for (Sentence sentence : tagedPair.getSentences()) {
                content.append(sentence.getOriginal()).append(" ")
                        .append(tagedPair.getKnowledgeA().getName()).append(" ")
                        .append(tagedPair.getKnowledgeB().getName()).append(" ")
                        .append(tagedPair.getRelation().getName()).append(" ")
                        .append(tagedPair.getRelation().getCode()).append("\n");
                size++;
            }
        }

        FileUtil.overrideFile(datasetPath, content.toString());

        Map<String, Object> res = new HashMap<>();
        res.put("size", size);
        return res;
    }

    /**
     * 获取基本信息，包括  总数、已标记数量、实例数(同时包含两个实体的句子)
     *
     * @return
     */
    public Map<String, Object> getInfo() {
        Map<String, Object> res = new HashMap<>();
        long tagedCount = 0;
        long instanceCount = 0;
        List<Pair> pairs = repository.findAll();
        long count = pairs.size();
        for (Pair pair : pairs) {
            if (pair.getRelation() != null)
                tagedCount += 1;
            instanceCount += pair.getSentences().size();
        }
        res.put("count", count);
        res.put("tagedCount", tagedCount);
        res.put("instanceCount", instanceCount);
        return res;
    }

    public Pair getById(long id) {
        return repository.getOne(id);
    }

    /**
     * 获取指定pair的关系图。两个主节点的颜色分别为绿色与蓝色，其它为红色
     * <p>
     * 具体做法：
     * <p>
     * 1. 获取该piar的两个knowledge。
     * <p>
     * 2. 分别获取每个knowledge的one-hot关系图。
     * <p>
     * 3. 将两个关系图合并。
     */
    public Map<String, Object> getGraph(long id) {
        Pair pair = repository.getOne(id);

        Map<String, Object> graphA = knowledgeService.getGraph(pair.getKnowledgeA(), 30);
        Map<String, Object> graphB = knowledgeService.getGraph(pair.getKnowledgeB(), 30);

        Map<String, Object> res = mergeGraphes(graphA, graphB);
        for (Map<String, Object> node : (HashSet<Map<String, Object>>) res.get("nodes")) {
            node.put("x", Math.random() * 100);
            node.put("y", Math.random() * 100);

            if (node.get("id").equals(pair.getKnowledgeA().getId())) {
                node.put("color", "green");
            } else if (node.get("id").equals(pair.getKnowledgeB().getId())) {
                node.put("color", "blue");
            } else {
                node.put("color", "red");
            }
        }
        return res;
    }

    /**
     * 获取整个知识体系的关系图。
     * <p>
     * 1. 获取所有的pair，将边与相关node加进去。
     * <p>
     * 2. 获取所有的node，再加进去。避免遗漏孤立节点。
     */
    public Map<String, Object> getGraph() {
        Map<String, Object> res = new HashMap<>();
        Set<Map<String, Object>> nodes = new HashSet<>();
        List<Map<String, Object>> edge = new ArrayList<>();
        res.put("nodes", nodes);
        res.put("edges", edge);

        // 将所有的边与相关节点放入图中
        List<Pair> pairs = repository.findAll();
        for (Pair pair : pairs) {
            knowledgeService.addPairToGraph(pair, res, 50);
        }

        // 将所有的节点放入nodes中
        List<Knowledge> knowledges = knowledgeRepository.findAll();
        for (Knowledge knowledge : knowledges) {
            nodes.add(knowledgeService.getNode(knowledge, 50));
        }

        // 给每个节点设置位置
        for (Map<String, Object> node : (HashSet<Map<String, Object>>) res.get("nodes")) {
            node.put("x", Math.random() * 100);
            node.put("y", Math.random() * 100);
        }
        return res;
    }

}
