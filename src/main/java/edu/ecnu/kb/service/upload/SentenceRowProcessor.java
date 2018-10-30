package edu.ecnu.kb.service.upload;

import edu.ecnu.kb.model.Sentence;
import edu.ecnu.kb.service.util.SplitWordUtil;
import org.springframework.stereotype.Component;

@Component
public class SentenceRowProcessor implements RowProcessor {

    // 滤网，将不同格式的符号统一起来
    private final static String[][] filterNet = {
            {"（", "("},
            {"）", ")"},
            {"，", ","},
            {".", "。"},
            {"；", ";"},
            {"：", ":"},
            {"①", "1、"},
            {"②", "2、"},
            {"③", "3、"},
            {"④", "4、"},
            {"°", "度"},
            {"．", "、"},
            {"√", "根号"},
            {"²", "^2"},
    };

    @Override
    public Object processor(String[] line) {
        if (!isValided(line[0])) {
            System.out.println(line[0]);
            return null;
        }
        Sentence sentence = new Sentence();
        sentence.setOriginal(filter(line[0]));
        sentence.setSplited(SplitWordUtil.split(sentence.getOriginal()));
        return sentence;
    }

    /**
     * 检查句子是否有效
     * <p>
     * 从长度角度考虑
     * <p>
     * 包含证明符号的为非法
     * <p>
     * 以参见开头
     */
    private boolean isValided(String sentence) {
        if (sentence.length() <= 10 || sentence.length() > 150) return false;
        if (sentence.contains("∵")) return false;
        if (sentence.contains("∴")) return false;
        if (sentence.startsWith("参见")) return false;
        if (sentence.contains("年")) return false;
        if (sentence.contains("《")) return false;
        return true;
    }

    /**
     * 过滤句子
     */
    private String filter(String sentence) {
        for (String[] fn : filterNet) {
            sentence.replace(fn[0], fn[1]);
        }
        return sentence;
    }
}
