package edu.ecnu.kb.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@RepositoryRestResource(exported = false)
public interface PairRepository extends BaseRepository<Pair> {

    @Query(value = "select pair from Pair as pair where relation is null")
    Page<Pair> findUntagedPairs(Pageable pageable);

    @Query("select  pair from Pair as pair where relation is not null")
    List<Pair> findTagedPairs();

    @Query("select count(pair) from  Pair as pair where relation is not null")
    long countTaged();

    Pair findByKnowledgeAAndKnowledgeB(Knowledge knowledgeA,Knowledge knowledgeb);
}
