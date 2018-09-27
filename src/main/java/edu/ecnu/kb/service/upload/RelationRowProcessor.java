package edu.ecnu.kb.service.upload;

import edu.ecnu.kb.model.Relation;
import org.springframework.stereotype.Component;

@Component
public class RelationRowProcessor implements RowProcessor {
    @Override
    public Object processor(String[] line) {
        Relation relation = new Relation();
        relation.setCode(line[0]);
        relation.setName(line[1]);
        relation.setExample(line[2]);
        return relation;
    }
}
