package io.mpolivaha.psersistable;

import io.mpolivaha.AbstractIntegrationTest;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

@Sql(
    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
    // language=sql
    statements = """
      CREATE TABLE IF NOT EXISTS root_aggregate(
        id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
        name VARCHAR
      );
    """
)
@Sql(
    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
    // language=sql
    statements = "DROP TABLE IF EXISTS root_aggregate;"
)
class RootAggregateRepoTest extends AbstractIntegrationTest {

  @Autowired
  private RootAggregateRepo rootAggregateRepo;

  @Test
  void testSaveNewWithId() {

    // Given.
    RootAggregate rootAggregate = RootAggregate.newWithId(1000L).setName("Alex");
    RootAggregate save = rootAggregateRepo.save(rootAggregate);

    // When.
    Optional<RootAggregate> result = rootAggregateRepo.findById(1000L);

    // Then.
    Assertions.assertThat(result).isPresent().hasValueSatisfying(it -> {
      Assertions.assertThat(it.getName()).isEqualTo("Alex");
    });
  }

  @Test
  void testSaveNewWithoutId() {
    // Given.
    RootAggregate rootAggregate = RootAggregate.newWithoutId().setName("Alex");
    RootAggregate save = rootAggregateRepo.save(rootAggregate);

    // When.
    Optional<RootAggregate> result = rootAggregateRepo.findById(save.getId());

    // Then.
    Assertions.assertThat(result).isPresent().hasValueSatisfying(it -> {
      Assertions.assertThat(it.getName()).isEqualTo("Alex");
    });
  }
}