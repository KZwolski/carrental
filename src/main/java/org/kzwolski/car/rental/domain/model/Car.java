package org.kzwolski.car.rental.domain.model;

import java.util.Objects;
import java.util.UUID;

public class Car {
  private final UUID id;

  private final CarType type;

  public Car(CarType type) {
    this.id = UUID.randomUUID();
    this.type = Objects.requireNonNull(type, "type must not be null");
  }

  public UUID getId() {
    return id;
  }

  public CarType getType() {
    return type;
  }
}
