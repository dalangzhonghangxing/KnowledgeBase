package edu.ecnu.kb.model;

import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * 记录一对知识点及关系，关系是有序的，即A->B的关系。
 *
 * @author guhang
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {
        "knowledge_a", "knowledge_b"}))
@Data
public class Pair extends BaseModel {
    // 知识点A
    @ManyToOne
    @JoinColumn(name = "knowledge_a")
    private Knowledge knowledgeA;

    // 知识点B
    @ManyToOne
    @JoinColumn(name = "knowledge_b")
    private Knowledge knowledgeB;

    // 关系
    @ManyToOne
    @JoinColumn(name = "relation")
    private Relation relation;

    // 句子
    @ManyToMany
    private Set<Sentence> sentences = new HashSet<>();

    // 根据该字段来排序，实现多人同时打标签
    @Column(columnDefinition="int default 0")
    private int seq=0;

    @Override
    public boolean equals(Object other) {
        if (other instanceof Pair) {
            if (this.knowledgeA.equals(((Pair) other).getKnowledgeA())
                    && this.knowledgeB.equals(((Pair) other).getKnowledgeB()))
                return true;
            return false;
        } else
            return false;
    }
}
