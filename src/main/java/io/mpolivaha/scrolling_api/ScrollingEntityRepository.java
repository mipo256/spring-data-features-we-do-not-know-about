package io.mpolivaha.scrolling_api;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.ScrollPosition;
import org.springframework.data.domain.Window;
import org.springframework.data.repository.CrudRepository;

public interface ScrollingEntityRepository extends CrudRepository<ScrollingEntity, Long> {

  /**
   * Is <strong>roughly</strong> translated to:
   * <p>
   * <pre>
   *    SELECT
   *        se.id,
   *        se.name,
   *        se.created_at
   *    FROM scrolling_entity se
   *    WHERE se.name LIKE '%' || :name || '%'
   *    ORDER BY :param
   *    OFFSET :pageNumber * pageSize ROWS
   *    FETCH FIRST :pageSize ROWS ONLY;
   * </pre>
   */
  Page<ScrollingEntity> findByNameContains(String name, Pageable pageable);

  Window<ScrollingEntity> findTop10ByNameContainsOrderByName(String name, ScrollPosition scrollPosition);
}
