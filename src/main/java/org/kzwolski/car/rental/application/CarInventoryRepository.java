package org.kzwolski.car.rental.application;

import org.kzwolski.car.rental.domain.model.Car;
import org.kzwolski.car.rental.domain.model.CarType;

import java.util.List;

public interface CarInventoryRepository {
  List<Car> findByType(CarType type);
}
