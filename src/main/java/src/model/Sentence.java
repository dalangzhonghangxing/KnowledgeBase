package src.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

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
    @Column(columnDefinition = "text")
    private String original;

    //分词后的句子
    @Column(columnDefinition = "text")
    private String splited;
}
