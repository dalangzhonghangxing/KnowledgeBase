package edu.ecnu.kb.service.util;

//import edu.stanford.nlp.ling.CoreLabel;
//import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
//import edu.stanford.nlp.trees.Tree;

/**
 * 自然语言处理工具类。
 *
 * 未使用
 */
//@Service
public class NLPUtil {

//    private final static LexicalizedParser LP = LexicalizedParser.loadModel("edu/stanford/nlp/models/lexparser/chinesePCFG.ser.gz");

    /**
     * 为一句分词后的句子生成依赖树。
     */
//    public static Tree generateDependencyTree(String splitedSentence) {
//
//        // 按照空格分词，然后放入List<CoreLabel>，作为生成依赖树的输入
//        List<CoreLabel> rawWords = new ArrayList<>();
//        for (String word : splitedSentence.split("\\s+")) {
//            CoreLabel coreLabel = new CoreLabel();
//            coreLabel.setWord(word);
//            rawWords.add(coreLabel);
//        }
//
//        return LP.apply(rawWords);
//    }

    /**
     * 为一对知识点，从一句话中提取出SDP（最短依赖路径）。
     *
     * 只获取最先出现的两个
     * 并拼接成 SDP knowledgeA knowledgeB 的格式
     * @param tree
     * @param knowledgeA
     * @param knowledgeB
     * @return
     */
//    public static String getSDP(Tree tree,String knowledgeA,String knowledgeB){
//        for(Tree t : tree.getLeaves()){
//            if(t.value().equals(knowledgeA)){
//
//            }
//        }
//        return null;
//    }

}
