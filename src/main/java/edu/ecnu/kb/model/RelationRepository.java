package edu.ecnu.kb.model;

import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RepositoryRestResource(exported = false)
public interface RelationRepository extends BaseRepository<Relation> {
}
