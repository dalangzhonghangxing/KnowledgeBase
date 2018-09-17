package edu.ecnu.kb.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RepositoryRestResource(exported = false)
public interface PairRepository extends BaseRepository<Pair> {

    @Query(value = "from Pair where relation=null")
    Page<Pair> findUntagedPairs(PageRequest of);
}
