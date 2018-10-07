package test;

import com.huaban.analysis.jieba.JiebaSegmenter;
import edu.ecnu.kb.service.util.FileUtil;
import edu.ecnu.kb.service.util.PythonUtil;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class MyTest {

    @Test
    public void splitWords() {
        JiebaSegmenter segmenter = new JiebaSegmenter();
        String[] sentences =
                new String[]{"这是一个伸手不见五指的黑夜。我叫孙悟空，我爱北京，我爱Python和C++。", "我不喜欢日本和服。", "雷猴回归人间。",
                        "工信处女干事每月经过下属科室都要亲口交代24口交换机等技术性器件的安装工作", "结果婚的和尚未结过婚的"};
        for (String sentence : sentences) {
            for (Term t : NlpAnalysis.parse(sentence).getTerms()) {
                System.out.println(t.getName());
            }
        }
    }

    @Test
    public void executePython() {
        FileUtil fu = new FileUtil();
        fu.initDirs();
        PythonUtil.execute("mnist", "rnn/mnist.py");
    }

    @Test
    public void parseResult(){
        FileUtil fu = new FileUtil();
        fu.initDirs();
        Map<String, List<Object>> result = PythonUtil.getResult("mnist");
        System.out.println(result);
    }
}
