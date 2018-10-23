package edu.ecnu.kb.service;

import edu.ecnu.kb.model.Knowledge;
import edu.ecnu.kb.model.KnowledgeRepository;
import edu.ecnu.kb.model.Pair;
import edu.ecnu.kb.model.PairRepository;
import edu.ecnu.kb.service.upload.KnowledgeRowProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.*;

@Service
public class KnowledgeService extends BaseService {

    @Autowired
    private KnowledgeRepository repository;

    @Autowired
    private KnowledgeRowProcessor rowProcessor;

    @Autowired
    private PairRepository pairRepository;

    private final static Sort SORT_NAME_ASC = new Sort(Sort.Direction.ASC, "name");

    private static final String[] columnForExport = {"name"};

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
        return getByPage(page, size, repository, SORT_NAME_ASC);
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

    /**
     * 导出所有对象
     *
     * @return
     */
    public byte[] export() {
        List<Knowledge> knowledges = repository.findAll(SORT_ID_DESC);
        List<Map<String, Object>> data = new ArrayList<>();
        for (Knowledge knowledge : knowledges) {
            Map<String, Object> obj = new HashMap<>();
            obj.put(columnForExport[0], knowledge.getName());
            data.add(obj);
        }
        return export(columnForExport, data);
    }

    /**
     * 为一个知识点生成关系图。
     * <p>
     * 1. 找到所有与knowledge相关的pair
     * <p>
     * 2. 根据pair生成关系图，要将所有关系都正过来。
     */
    public Map<String, Object> getGraph(Knowledge knowledge, int nodeSize) {
        Map<String, Object> res = new HashMap<>();
        res.put("nodes", new HashSet<Map<String, Object>>());
        res.put("edges", new ArrayList<>());
        List<Pair> pairs = pairRepository.findByKnowledge(knowledge);
        for (Pair pair : pairs)
            addPairToGraph(pair, res, nodeSize);
        return res;
    }


    /**
     * 往graph中添加一个pair。
     * <p>
     * 如果节点不在nodes中，则加入nodes。
     * <p>
     * 如果relation不是正方向，则将关系正过来。
     *
     * @param pair
     * @param graph
     */
    private void addPairToGraph(Pair pair, Map<String, Object> graph, int nodeSize) {
        if (pair.getRelation() == null
                || pair.getRelation().getName().equals("无关")
                || pair.getRelation().getName().equals("待定"))
            return;

        Set<Map<String, Object>> nodes = (HashSet<Map<String, Object>>) graph.get("nodes");
        List<Map<String, Object>> edges = (List<Map<String, Object>>) graph.get("edges");

        Map<String, Object> nodeA = getNode(pair.getKnowledgeA(), nodeSize);
        Map<String, Object> nodeB = getNode(pair.getKnowledgeB(), nodeSize);
        nodes.add(nodeA);
        nodes.add(nodeB);

        Map<String, Object> edge = new HashMap<>();
        if (pair.getRelation().getInverseRelation() == null) {
            //正向关系，不用调整方向
            edge.put("sourceID", nodeA.get("id"));
            edge.put("targetID", nodeB.get("id"));
            edge.put("size", 4);
            edge.put("r", pair.getRelation().getName());
        } else {
            //逆关系，调整方向
            edge.put("targetID", nodeA.get("id"));
            edge.put("sourceID", nodeB.get("id"));
            edge.put("size", 4);
            edge.put("r", pair.getRelation().getInverseRelation().getName());
        }
        edges.add(edge);
    }

    /**
     * 将一个知识点封装成node格式。
     *
     * @param knowledge
     * @return
     */
    private Map<String, Object> getNode(Knowledge knowledge, int nodeSize) {
        Map<String, Object> node = new HashMap<>();
        node.put("label", knowledge.getName());
        node.put("id", knowledge.getId());
        node.put("size", nodeSize);
        return node;
    }
}
