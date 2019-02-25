package edu.ecnu.kb.model;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@RepositoryRestResource(exported = false)
public interface RelationRepository extends BaseRepository<Relation> {
    Relation findByName(String name);

    Relation findByCode(String code);
}
