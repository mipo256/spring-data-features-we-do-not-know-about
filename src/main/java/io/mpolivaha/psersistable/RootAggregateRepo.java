package io.mpolivaha.psersistable;

import org.springframework.data.repository.CrudRepository;

public interface RootAggregateRepo extends CrudRepository<RootAggregate, Long> {

}
