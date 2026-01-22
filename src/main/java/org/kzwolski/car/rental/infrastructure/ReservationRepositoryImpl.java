package org.kzwolski.car.rental.infrastructure;

import org.kzwolski.car.rental.application.ReservationRepository;
import org.kzwolski.car.rental.domain.model.Reservation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public final class ReservationRepositoryImpl implements ReservationRepository {

  private final Map<UUID, List<Reservation>> reservationsGroupedByCarId;

  public ReservationRepositoryImpl() {
    this.reservationsGroupedByCarId = new HashMap<>();
  }

  @Override
  public void save(final Reservation reservation) {
    Objects.requireNonNull(reservation, "reservation must not be null");

    final var carId = reservation.getCarId();

    reservationsGroupedByCarId
            .computeIfAbsent(
                    carId,
                    id -> new ArrayList<>()
            )
            .add(reservation);
  }

  @Override
  public List<Reservation> findByCarId(final UUID carId) {
    Objects.requireNonNull(carId, "carId must not be null");

    final var reservationsForCar = reservationsGroupedByCarId.getOrDefault(carId, List.of());

    return List.copyOf(reservationsForCar);
  }
}

