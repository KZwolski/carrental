package org.kzwolski.car.rental.application;

import org.kzwolski.car.rental.domain.model.Reservation;

import java.util.List;
import java.util.UUID;

public interface ReservationRepository {
  void save(Reservation reservation);
  List<Reservation> findByCarId(UUID carId);
}
