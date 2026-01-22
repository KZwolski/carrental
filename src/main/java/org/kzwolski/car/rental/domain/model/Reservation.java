package org.kzwolski.car.rental.domain.model;

import java.util.Objects;
import java.util.UUID;

public class Reservation {
  private final UUID id;
  private final UUID carId;
  private final CarType carType;
  private final TimeRange timeRange;

  public Reservation(UUID carId, CarType carType, TimeRange timeRange) {
    this.id = UUID.randomUUID();
    this.carId = Objects.requireNonNull(carId, "carId must not be null");
    this.carType = Objects.requireNonNull(carType, "type must not be null");
    this.timeRange = Objects.requireNonNull(timeRange, "range must not be null");
  }

  public UUID getCarId() {
    return carId;
  }

  public UUID getId() {
    return id;
  }

  public CarType getCarType() {
    return carType;
  }

  public TimeRange getTimeRange() {
    return timeRange;
  }
}

