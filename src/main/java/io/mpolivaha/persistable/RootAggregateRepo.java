package io.mpolivaha.persistable;

import org.springframework.data.repository.CrudRepository;

public interface RootAggregateRepo extends CrudRepository<RootAggregate, Long> {

}
