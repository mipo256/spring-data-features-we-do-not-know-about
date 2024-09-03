package io.mpolivaha.domain_events;

import io.mpolivaha.domain_events.events.CargoStatusChangeDomainEvent;
import io.mpolivaha.domain_events.events.CargoWeightUpdatedDomainEvent;
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

  @Override
  public boolean isNew() {
    return id == null;
  }

  @Override
  public Long getId() {
    return this.id;
  }

  public CargoEntity changeWeight(double weight) {
    this.weight = weight;
    super.registerEvent(new CargoWeightUpdatedDomainEvent());
    return this;
  }

  public CargoEntity changeStatus(CargoStatus status) {
    this.status = status;
    super.registerEvent(new CargoStatusChangeDomainEvent());
    return this;
  }
}
