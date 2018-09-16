package edu.ecnu.kb.service.upload;

import edu.ecnu.kb.model.Knowledge;
import org.springframework.stereotype.Component;

@Component
public class KnowledgeRowProcessor implements RowProcessor {
    @Override
    public Object processor(String[] line) {
        Knowledge knowledge = new Knowledge();
        knowledge.setName(line[0]);
        return knowledge;
    }
}
