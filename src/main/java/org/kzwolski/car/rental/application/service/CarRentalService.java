package org.kzwolski.car.rental.application.service;

import org.kzwolski.car.rental.application.CarInventoryRepository;
import org.kzwolski.car.rental.application.ReservationRepository;
import org.kzwolski.car.rental.application.dto.ReservationRequest;
import org.kzwolski.car.rental.application.dto.ReservationResponse;
import org.kzwolski.car.rental.application.validation.ReservationRequestValidator;
import org.kzwolski.car.rental.domain.exception.NoCarAvailableException;
import org.kzwolski.car.rental.domain.model.Car;
import org.kzwolski.car.rental.domain.model.Reservation;
import org.kzwolski.car.rental.domain.model.TimeRange;

import java.util.Objects;

public class CarRentalService {

    private final CarInventoryRepository carInventoryRepository;
    private final ReservationRepository reservationRepository;
    private final ReservationRequestValidator validator;

    public CarRentalService(final CarInventoryRepository carInventoryRepository,
                            final ReservationRepository reservationRepository, final ReservationRequestValidator validator) {
        this.carInventoryRepository =
                Objects.requireNonNull(carInventoryRepository, "carInventoryRepository must not be null");
        this.reservationRepository =
                Objects.requireNonNull(reservationRepository, "reservationRepository must not be null");
        this.validator = Objects.requireNonNull(validator, "reservationRequestValidator must not be null");
    }

    public ReservationResponse reserve(final ReservationRequest request) {
        validator.validateRequest(request);

        final var endDateTime = request.startDateTime().plusDays(request.numberOfDays());
        final var requestedPeriod = new TimeRange(request.startDateTime(), endDateTime);

        final var carsOfRequestedType = carInventoryRepository.findByType(request.carType());

        for (Car car : carsOfRequestedType) {
            if (isCarAvailable(car, requestedPeriod)) {
                final var reservation = new Reservation(car.getId(), request.carType(), requestedPeriod);
                reservationRepository.save(reservation);

                return new ReservationResponse(
                        reservation.getId(),
                        reservation.getCarId(),
                        reservation.getCarType(),
                        reservation.getTimeRange().getStartDate(),
                        reservation.getTimeRange().getEndDate()
                );
            }
        }

        throw new NoCarAvailableException(
                "No available cars of type " + request.carType() + " for requested period");
    }

    private boolean isCarAvailable(final Car car,final TimeRange requestedPeriod) {
        final var existingReservations = reservationRepository.findByCarId(car.getId());

        for (Reservation existingReservation : existingReservations) {
            if (existingReservation.getTimeRange().overlaps(requestedPeriod)) {
                return false;
            }
        }
        return true;
    }
}
