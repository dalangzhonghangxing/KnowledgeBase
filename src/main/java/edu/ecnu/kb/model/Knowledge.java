package edu.ecnu.kb.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 知识点
 *
 * @author guhang
 */
@Table
@Entity
@Data
public class Knowledge extends BaseModel {

    //知识点名称
    @Column(unique = true)
    private String name;
}
