package edu.ecnu.kb.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * 基础模型
 *
 * @author guhang
 */
@Data
@MappedSuperclass
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public abstract class BaseModel implements Serializable {
    private static final long serialVersionUID = 188123671496376397L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

}
