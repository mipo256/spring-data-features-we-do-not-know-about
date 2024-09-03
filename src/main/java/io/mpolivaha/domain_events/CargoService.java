package io.mpolivaha.domain_events;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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
            existingCargo.changeWeight(newCargo.getWeight());
          }
          if (newCargo.getStatus() != existingCargo.getStatus()) {
            existingCargo.changeStatus(newCargo.getStatus());
          }
          return existingCargo;
        })
        .map(cargoRepository::save)
        .orElseThrow(RuntimeException::new));
  }
}