package io.mpolivaha.psersistable;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table("root_aggregate")
public class RootAggregate implements Persistable<Long> {

  @Id
  @EqualsAndHashCode.Include
  private Long id;

  private String name;

  @Setter(AccessLevel.PRIVATE)
  @Transient
  private Boolean isNew;

  @PersistenceCreator
  public RootAggregate() {}

  public static RootAggregate newWithId(Long id) {
    return new RootAggregate().setId(id).setIsNew(true);
  }

  public static RootAggregate newWithoutId() {
    return new RootAggregate().setIsNew(true);
  }

  @Override
  public Long getId() {
    return this.id;
  }

  @Override
  public boolean isNew() {
    return this.isNew;
  }
}
