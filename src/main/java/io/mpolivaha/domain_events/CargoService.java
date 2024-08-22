package io.mpolivaha.domain_events;

import io.mpolivaha.domain_events.events.CargoStatusChangeDomainEvent;
import io.mpolivaha.domain_events.events.CargoWeightUpdatedDomainEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

@Service
@RequiredArgsConstructor
public class CargoService {

  private final CargoRepository cargoRepository;
  private final TransactionTemplate transactionTemplate;

  public CargoEntity updateCargo(CargoEntity newCargo) {
    return transactionTemplate.execute((status) -> cargoRepository
        .findById(newCargo.getId())
        .map(existingCargo -> {
          if (newCargo.getWeight() != existingCargo.getWeight()) {
            existingCargo.setWeight(newCargo.getWeight());
            newCargo.registerEvent(new CargoWeightUpdatedDomainEvent());
          }
          if (newCargo.getStatus() != existingCargo.getStatus()) {
            existingCargo.setStatus(newCargo.getStatus());
            newCargo.registerEvent(new CargoStatusChangeDomainEvent());
          }
          return newCargo;
        })
        .map(cargoRepository::save)
        .orElseThrow(RuntimeException::new));
  }
}