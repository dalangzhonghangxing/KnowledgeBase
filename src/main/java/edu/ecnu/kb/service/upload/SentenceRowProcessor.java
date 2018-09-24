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
        StringBuffer splited = new StringBuffer();
        for (String word:SplitWordUtils.split(line[0])) {
            splited.append(word).append(" ");
        }
        sentence.setSplited(splited.toString());
        return sentence;
    }
}
