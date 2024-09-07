package io.mpolivaha.scrolling_api;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.ScrollPosition;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Window;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.transaction.annotation.Transactional;

import io.mpolivaha.AbstractIntegrationTest;

@Sql(
    executionPhase = ExecutionPhase.BEFORE_TEST_METHOD,
    //language=sql
    statements = """
    CREATE TABLE IF NOT EXISTS scrolling_entity(
      id BIGSERIAL PRIMARY KEY,
      name TEXT,
      created_at TIMESTAMPTZ
    );
    """
)
@Sql(
    executionPhase = ExecutionPhase.AFTER_TEST_METHOD,
    //language=sql
    statements = """
    DROP TABLE IF EXISTS scrolling_entity;
    """
)
class ScrollingEntityRepositoryTest extends AbstractIntegrationTest {

  @Autowired
  private ScrollingEntityRepository scrollingEntityRepository;

  @Test
  @Transactional
  void testOrdinaryPaging() {

    // Given.
    scrollingEntityRepository.save(new ScrollingEntity().setName("Oliver"));
    scrollingEntityRepository.save(new ScrollingEntity().setName("Alex"));
    scrollingEntityRepository.save(new ScrollingEntity().setName("Neo"));

    // When.
    Page<ScrollingEntity> result = scrollingEntityRepository.findByNameContains("l", PageRequest.of(0, 10, Sort.by("name")));

    // Then.
    Assertions.assertThat(result.getTotalElements()).isEqualTo(2);
  }

  @Test
  @Transactional
  void testWindowPaginationWithOffset() {
    // Given.
    scrollingEntityRepository.save(new ScrollingEntity().setName("Oliver"));
    scrollingEntityRepository.save(new ScrollingEntity().setName("Alex"));
    scrollingEntityRepository.save(new ScrollingEntity().setName("Neo"));
    scrollingEntityRepository.save(new ScrollingEntity().setName("Stella"));

    // When.
    Window<ScrollingEntity> firstWindow = scrollingEntityRepository.findTop10ByNameContainsOrderByName("e", ScrollPosition.offset());
    Window<ScrollingEntity> secondWindow = scrollingEntityRepository.findTop10ByNameContainsOrderByName(
        "l",
        firstWindow.positionAt(firstWindow.size() - 1)
    );

    // Then.
    Assertions.assertThat(firstWindow.size()).isEqualTo(4);
    Assertions.assertThat(secondWindow.size()).isEqualTo(0);
  }

  @Test
  @Transactional
  void testWindowPaginationWithSeekMethod() {
    // Given.
    scrollingEntityRepository.save(new ScrollingEntity().setName("Oliver"));
    scrollingEntityRepository.save(new ScrollingEntity().setName("Alex"));
    scrollingEntityRepository.save(new ScrollingEntity().setName("Neo"));
    scrollingEntityRepository.save(new ScrollingEntity().setName("Stella"));

    // When.
    Window<ScrollingEntity> firstWindow = scrollingEntityRepository.findTop10ByNameContainsOrderByName("e", ScrollPosition.keyset().forward());
    Window<ScrollingEntity> secondWindow = scrollingEntityRepository.findTop10ByNameContainsOrderByName(
        "l",
        firstWindow.positionAt(firstWindow.size() - 1)
    );

    // Then.
    Assertions.assertThat(firstWindow.size()).isEqualTo(4);
    Assertions.assertThat(secondWindow.size()).isEqualTo(0);
  }

  // Albert, Alex, Claus, Oliver, Stella
  @Test
  @Transactional
  void testMissingRowProblemWithOffsetBasedPagination() {
    // Given.
    ScrollingEntity albert = new ScrollingEntity().setName("Albert");
    scrollingEntityRepository.save(albert);
    scrollingEntityRepository.save(new ScrollingEntity().setName("Alex"));
    scrollingEntityRepository.save(new ScrollingEntity().setName("Claus"));
    scrollingEntityRepository.save(new ScrollingEntity().setName("Oliver"));
    scrollingEntityRepository.save(new ScrollingEntity().setName("Stella"));

    // When.
    Page<ScrollingEntity> firstResult = scrollingEntityRepository.findByNameContains("l", PageRequest.of(0, 3, Sort.by("name")));

    scrollingEntityRepository.deleteById(albert.getId());

    Page<ScrollingEntity> secondResult = scrollingEntityRepository.findByNameContains("l", PageRequest.of(1, 3, Sort.by("name")));

    // Then.
    Assertions
      .assertThat(firstResult.getSize())
      .isEqualTo(3);

    Assertions
      .assertThat(firstResult)
      .extracting(ScrollingEntity::getName)
      .containsOnly("Albert", "Alex", "Claus");

    Assertions
      .assertThat(secondResult.getTotalElements())
      .isEqualTo(4);

    Assertions
      .assertThat(secondResult)
      .extracting(ScrollingEntity::getName)
      .containsOnly( "Stella");
  }
}