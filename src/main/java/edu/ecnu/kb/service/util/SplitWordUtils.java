package edu.ecnu.kb.service.util;

import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.SegToken;
import edu.ecnu.kb.model.Knowledge;
import edu.ecnu.kb.model.KnowledgeRepository;
import org.ansj.domain.Term;
import org.ansj.library.DicLibrary;
import org.ansj.splitWord.analysis.DicAnalysis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 分词工具类
 */
@Component
public class SplitWordUtils {

    private static final Logger LOG = LoggerFactory
            .getLogger(SplitWordUtils.class);

    private static final JiebaSegmenter JIEBA_SEGMENTER = new JiebaSegmenter();

    @Autowired
    public void init(KnowledgeRepository knowledgeRepository) {
        // 将所有知识点添加到关键词库中
        updateWordBase(knowledgeRepository);
    }

    /**
     * 更新关键词词库，新增加的关键词的词性都设置为"knowledge"
     */
    public static void updateWordBase(KnowledgeRepository knowledgeRepository) {
        List<Knowledge> knowledges = knowledgeRepository.findAll();
        for (Knowledge knowledge : knowledges) {
            DicLibrary.insert(DicLibrary.DEFAULT, knowledge.getName(), "knowledge", 1000);
        }
        LOG.info("自定义词库更新成功，总共更新"+knowledges.size()+"个知识点");
    }

    /**
     * 分词
     *
     * @param sentence 待分词的句子
     * @return 分词后的list
     */
    public static List<String> split(String sentence) {
        return splitByAnsj(sentence);
    }

    /**
     * 使用结巴分词
     *
     * @param sentence
     * @return
     */
    public static List<String> splitByJieba(String sentence) {
        List<String> words = new ArrayList<>();
        for (SegToken token : JIEBA_SEGMENTER.process(sentence, JiebaSegmenter.SegMode.SEARCH)) {
            words.add(token.word);
        }
        return words;
    }

    /**
     * 使用ansj分词
     */
    public static List<String> splitByAnsj(String sentence) {
        List<String> words = new ArrayList<>();
        for (Term term : DicAnalysis.parse(sentence))
            words.add(term.getName());
        return words;
    }
}
