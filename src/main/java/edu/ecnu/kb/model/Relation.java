package edu.ecnu.kb.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

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
}
