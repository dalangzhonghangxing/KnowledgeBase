package src.model;

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
    @Column
    private String name;
}
