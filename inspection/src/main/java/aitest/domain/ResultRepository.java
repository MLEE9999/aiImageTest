package aitest.domain;

import aitest.domain.*;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

//<<< PoEAA / Repository
@RepositoryRestResource(collectionResourceRel = "results", path = "results")
public interface ResultRepository
    extends PagingAndSortingRepository<Result, Long> {}
