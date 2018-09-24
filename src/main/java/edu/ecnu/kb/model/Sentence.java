package edu.ecnu.kb.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * 句子，记录原句以及分词后的句子
 *
 * @author guhang
 */
@Table
@Entity
@Data
public class Sentence extends BaseModel {

    // 原句
    @Column(length = 255, unique = true)
    private String original;

    //分词后的句子
    @Column(length = 255)
    private String splited;

    public void setSplited(List<String> words) {
        StringBuffer sb = new StringBuffer();
        for (String word : words)
            sb.append(word).append(" ");
        this.splited = sb.subSequence(0, sb.length() - 1).toString();
    }
}
