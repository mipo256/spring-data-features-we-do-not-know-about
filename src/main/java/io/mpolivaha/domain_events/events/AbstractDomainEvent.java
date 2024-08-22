package io.mpolivaha.domain_events.events;

import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AbstractDomainEvent<T, ID> {

  private T oldValue;
  private T newValue;
  private ID entityId;
  private Instant timestamp;
}
