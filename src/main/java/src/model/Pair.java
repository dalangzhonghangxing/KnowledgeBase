package src.model;

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
@Table
@Data
public class Pair extends BaseModel {
    // 知识点A
    @ManyToOne
    @JoinColumn
    private Knowledge knowledgeA;

    // 知识点B
    @ManyToOne
    @JoinColumn
    private Knowledge knowledgeB;

    // 关系
    @ManyToOne
    @JoinColumn
    private Relation relation;

    // 句子
    @ManyToMany
    private Set<Sentence> sentences = new HashSet<>();
}
