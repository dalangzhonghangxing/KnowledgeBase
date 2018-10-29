package edu.ecnu.kb.service.upload;

import edu.ecnu.kb.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PairRowProcessor implements RowProcessor {

    @Autowired
    private KnowledgeRepository knowledgeRepository;

    @Autowired
    private RelationRepository relationRepository;

    /**
     * 格式 knowledgeA#knowledgeB#relationCode#relationName
     *
     * @param line
     * @return
     */
    @Override
    public Object processor(String[] line) {
        if (!isValid(line))
            return null;
        Knowledge knowledgeA = getKnowledgeByName(line[0]);
        Knowledge knowledgeB = getKnowledgeByName(line[1]);
        Relation relation = getRelation(line[2], line[3]);
        Pair pair = new Pair();
        pair.setRelation(relation);
        pair.setKnowledgeB(knowledgeB);
        pair.setKnowledgeA(knowledgeA);
        return pair;
    }

    /**
     * 检查当前行是否有效，knowledgeA与knowledgeB不能为""或null
     *
     * @param line
     * @return
     */
    private boolean isValid(String[] line) {
        if (line[0] == null || line[1] == null
                || line[0].equals("") || line[1].equals(""))
            return false;
        return true;
    }

    /**
     * 根据名称获取Knowledge，如果不存在则新增
     *
     * @param knowledgeName
     * @return
     */
    private Knowledge getKnowledgeByName(String knowledgeName) {
        Knowledge knowledge = knowledgeRepository.findByName(knowledgeName);
        if (knowledge == null) {
            // 如果该知识点不存在，则添加
            knowledge = new Knowledge();
            knowledge.setName(knowledgeName);
            knowledge = knowledgeRepository.save(knowledge);
        }
        return knowledge;
    }

    /**
     * 根据code或name加载关系
     *
     * @param code
     * @param name
     * @return
     */
    private Relation getRelation(String code, String name) {
        Relation relation = null;
        if (code != null && !code.equals(""))
            relation = relationRepository.findByCode(code);
        else {
            if (name != null && !name.equals("")) {
                relation = relationRepository.findByName(name);
            }
        }
        return relation;
    }
}
