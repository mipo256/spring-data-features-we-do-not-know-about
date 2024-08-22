package io.mpolivaha.domain_events;

import io.mpolivaha.AbstractIntegrationTest;
import io.mpolivaha.domain_events.events.AbstractDomainEvent;
import io.mpolivaha.domain_events.events.CargoWeightUpdatedDomainEvent;
import io.mpolivaha.domain_events.handler.CargoEventsListener;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.jdbc.Sql;

@Sql(
    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
    // language=sql
    statements = """
      CREATE TABLE IF NOT EXISTS cargo(
        id BIGSERIAL PRIMARY KEY,
        status TEXT,
        weight DECIMAL,
        created_at TIMESTAMPTZ,
        updated_at TIMESTAMPTZ
      );
    """
)
@Sql(
    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
    // language=sql
    statements = "DROP TABLE IF EXISTS cargo;"
)
class CargoServiceTest extends AbstractIntegrationTest {

  @SpyBean
  private CargoEventsListener cargoEventsListener;

  @Autowired
  private CargoService cargoService;

  @Autowired
  private CargoRepository cargoRepository;

  @Test
  void testWeightChanged() {
    // Given.
    CargoEntity existing = cargoRepository.save(new CargoEntity().setStatus(CargoStatus.NEW).setWeight(12.3d));
    existing.setWeight(15.1d);

    // When.
    cargoService.updateCargo(existing);

    // Then.
    Mockito.verify(cargoEventsListener).onCargoStatusChanged(Mockito.any(CargoWeightUpdatedDomainEvent.class));
  }

  @Test
  void testBothWeightAndStatusChanged() {
    // Given.
    CargoEntity existing = cargoRepository.save(new CargoEntity().setStatus(CargoStatus.NEW).setWeight(12.3d));
    existing.setWeight(15.1d);
    existing.setStatus(CargoStatus.ROUTED);

    // When.
    cargoService.updateCargo(existing);

    // Then.
    Mockito
        .verify(cargoEventsListener, Mockito.times(2))
        .onCargoStatusChanged(Mockito.any(AbstractDomainEvent.class));
  }
}