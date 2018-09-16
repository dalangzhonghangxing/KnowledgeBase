package edu.ecnu.kb.model;

import lombok.Data;

import javax.persistence.*;

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
}
