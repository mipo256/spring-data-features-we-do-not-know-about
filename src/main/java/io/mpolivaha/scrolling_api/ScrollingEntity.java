package io.mpolivaha.scrolling_api;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;

@Setter
@Getter
@Accessors(chain = true)
@Entity
public class ScrollingEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  @JdbcTypeCode(SqlTypes.BIGINT)
  private Long id;

  private String name;

  @CreatedDate
  private OffsetDateTime createdAt;
}
