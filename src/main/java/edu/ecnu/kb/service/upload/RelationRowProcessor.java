package edu.ecnu.kb.service.upload;

import edu.ecnu.kb.model.Relation;
import org.springframework.stereotype.Component;

@Component
public class RelationRowProcessor implements RowProcessor {
    @Override
    public Object processor(String[] line) {
        Relation relation = new Relation();
        relation.setName(line[0]);
        relation.setExample(line[1]);
        return relation;
    }
}
