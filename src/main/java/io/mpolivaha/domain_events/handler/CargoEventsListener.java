package io.mpolivaha.domain_events.handler;

import io.mpolivaha.domain_events.events.AbstractDomainEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CargoEventsListener  {

  @EventListener(AbstractDomainEvent.class)
  public void onCargoStatusChanged(AbstractDomainEvent<?, ?> event) {
    log.info("{} : {}", AbstractDomainEvent.class.getSimpleName(), event);
  }
}
