package edu.ecnu.kb.service.upload;

import edu.ecnu.kb.model.Sentence;
import edu.ecnu.kb.service.util.SplitWordUtils;
import org.springframework.stereotype.Component;

@Component
public class SentenceRowProcessor implements RowProcessor {

    @Override
    public Object processor(String[] line) {
        Sentence sentence = new Sentence();
        sentence.setOriginal(line[0]);
        sentence.setSplited(SplitWordUtils.split(line[0]));
        return sentence;
    }
}
