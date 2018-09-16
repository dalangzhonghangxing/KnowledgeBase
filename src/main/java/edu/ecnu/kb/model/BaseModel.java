package edu.ecnu.kb.model;

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
@MappedSuperclass
public abstract class BaseModel implements Serializable {
    private static final long serialVersionUID = 188123671496376397L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected long id;

}
