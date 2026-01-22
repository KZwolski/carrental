package org.kzwolski.car.rental.infrastructure;

import org.kzwolski.car.rental.application.CarInventoryRepository;
import org.kzwolski.car.rental.domain.model.Car;
import org.kzwolski.car.rental.domain.model.CarType;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class CarInventoryRepositoryImpl implements CarInventoryRepository {

  private final Map<CarType, List<Car>> carsGroupedByType;

  public CarInventoryRepositoryImpl(Map<CarType, List<Car>> carsGroupedByType) {
    Objects.requireNonNull(carsGroupedByType, "carsGroupedByType must not be null");

    EnumMap<CarType, List<Car>> inventoryByType = new EnumMap<>(CarType.class);

    for (CarType type : CarType.values()) {
      List<Car> carsOfType = carsGroupedByType.getOrDefault(type, List.of());
      inventoryByType.put(type, List.copyOf(carsOfType));
    }

    this.carsGroupedByType = inventoryByType;
  }

  @Override
  public List<Car> findByType(final CarType type) {
    Objects.requireNonNull(type, "type must not be null");
    return carsGroupedByType.getOrDefault(type, List.of());
  }
}
