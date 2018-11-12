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

    Pair findByKnowledgeAAndKnowledgeB(Knowledge knowledgeA, Knowledge knowledgeb);

    @Query("select pair from Pair as pair where knowledgeA=?1 or knowledgeB=?1")
    List<Pair> findByKnowledge(Knowledge knowledge);

    @Query(value = "select count(*) from pair left join pair_sentences on pair.id = pair_sentences.pair_id",
            nativeQuery = true)
    long findInstanceCount();

    @Query(value = "select count(*) from pair where relation is not null",
            nativeQuery = true)
    long findTagedCount();

    @Query(value = "select * from pair where relation in ?1", nativeQuery = true)
    List<Pair> findAllByRelationId(Long[] relationIds);

    @Query(value = "select count(*) from pair left join pair_sentences on pair.id = pair_sentences.pair_id where relation is not null",
            nativeQuery = true)
    Object findTagedInstanceCount();

    @Query(value = "select r.name,count(*) as count from pair right join relation r on pair.relation = r.id group by r.name  order by count",nativeQuery = true)
    List<Object[]> getCountByGroup();
}
