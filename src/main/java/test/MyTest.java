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
    public void parseResult() {
        FileUtil fu = new FileUtil();
        fu.initDirs();
        Map<String, List<Object>> result = PythonUtil.getResult("mnist");
        System.out.println(result);
    }

    @Test
    public void stanfordParser() {
//        runChineseAnnotators();
    }

//    public void runChineseAnnotators() {
////        StanfordCoreNLP corenlp = new StanfordCoreNLP("StanfordCoreNLP-chinese.properties");
//
//        LexicalizedParser lp = LexicalizedParser.loadModel("edu/stanford/nlp/models/lexparser/chinesePCFG.ser.gz");
//        String text = "等腰梯形 判定 定理 1 ： 在 同一 底 边上 的 两个 内角 相等 的 梯形 是 等腰梯形 。";
//
//        List<CoreLabel> rawWords = new ArrayList<>();
//        for(String word:text.split("\\s+")){
//            CoreLabel coreLabel = new CoreLabel();
//            coreLabel.setWord(word);
//            rawWords.add(coreLabel);
//        }
//
//        Tree parse = lp.apply(rawWords);
//        parse.pennPrint();
//        System.out.println();
//        TreebankLanguagePack tlp = lp.getOp().langpack();
//        GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
//        GrammaticalStructure gs = gsf.newGrammaticalStructure(parse);
//        List<TypedDependency> tdl = gs.typedDependenciesCCprocessed();
//        System.out.println(tdl);
//
////        Annotation document = new Annotation(text);
////
////        corenlp.annotate(document);
////        parserOutput(document);
//    }
//
//    public void parserOutput(Annotation document) {
//        // these are all the sentences in this document
//        // a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
//        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
//        for (CoreMap sentence : sentences) {            // traversing the words in the current sentence
//            // a CoreLabel is a CoreMap with additional token-specific methods
//            for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
//                // this is the text of the token
//                String word = token.get(CoreAnnotations.TextAnnotation.class);
//                // this is the POS tag of the token
//                String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
//                // this is the NER label of the token
//                String ne = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);
//                System.out.println(word + "\t" + pos + "\t" + ne);
//            }
//            // this is the parse tree of the current sentence
//            Tree tree = sentence.get(TreeCoreAnnotations.TreeAnnotation.class);
//            System.out.println("语法树：");
//            System.out.println(tree.toString());
//            // this is the Stanford dependency graph of the current sentence
//            SemanticGraph dependencies = sentence.get(SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation.class);
//            System.out.println("依存句法：");
//            System.out.println(dependencies.toString());
//        }         // This is the coreference link graph
//        // Each chain stores a set of mentions that link to each other,
//        // along with a method for getting the most representative mention
//        // Both sentence and token offsets start at 1!
//        Map<Integer, CorefChain> graph =
//                document.get(CorefCoreAnnotations.CorefChainAnnotation.class);
//    }

}
