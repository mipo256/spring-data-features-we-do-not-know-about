package io.mpolivaha.domain_events;

import java.time.OffsetDateTime;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Table("cargo")
public class CargoEntity extends AbstractAggregateRoot<CargoEntity> implements Persistable<Long> {

  @Id
  @EqualsAndHashCode.Include
  private Long id;

  private CargoStatus status;

  private double weight;

  @CreatedDate
  private OffsetDateTime createdAt;

  @LastModifiedDate
  private OffsetDateTime updatedAt;

  /**
   * The access modifier has to be extended since we do not have access to {@link super#registerEvent(Object)} outside the {@link CargoEntity}
   */
  @Override
  public <T> T registerEvent(T event) {
    return super.registerEvent(event);
  }

  @Override
  public boolean isNew() {
    return id == null;
  }

  @Override
  public Long getId() {
    return this.id;
  }
}
