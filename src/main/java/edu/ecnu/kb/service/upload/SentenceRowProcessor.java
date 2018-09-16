package edu.ecnu.kb.service.upload;

import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.SegToken;
import edu.ecnu.kb.model.Sentence;
import org.springframework.stereotype.Component;

@Component
public class SentenceRowProcessor implements RowProcessor {
    private static final JiebaSegmenter JIEBA_SEGMENTER = new JiebaSegmenter();

    @Override
    public Object processor(String[] line) {
        Sentence sentence = new Sentence();
        sentence.setOriginal(line[0]);
        StringBuffer splited = new StringBuffer();
        for (SegToken token : JIEBA_SEGMENTER.process(line[0], JiebaSegmenter.SegMode.INDEX)) {
            splited.append(token.word).append(" ");
        }
        sentence.setSplited(splited.toString());
        return sentence;
    }
}
