package edu.ecnu.kb.model;

import lombok.Data;

import javax.persistence.*;

/**
 * 关系类，记录关系与例子，方便用户理解该关系
 *
 * @author guhang
 */
@Entity
@Table
@Data
public class Relation extends BaseModel {
    // 关系名称
    @Column(unique = true)
    private String name;

    // 关系的例子
    @Column(columnDefinition = "text")
    private String example;

    // 编号，模型训练的时候使用该编号来表示关系
    // 使用该字段的主要原因是方便与之前的工作兼容
    @Column(unique = true)
    private String code;

    // 反关系，如果是正向的，则为null。
    @OneToOne
    @JoinColumn
    private Relation inverseRelation;

    public Relation() {

    }

    public Relation(Long relationId) {
        setId(relationId);
    }
}
