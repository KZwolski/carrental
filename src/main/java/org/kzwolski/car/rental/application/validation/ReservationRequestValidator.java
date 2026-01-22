package org.kzwolski.car.rental.application.validation;

import org.kzwolski.car.rental.application.dto.ReservationRequest;
import org.kzwolski.car.rental.domain.exception.InvalidReservationException;

public final class ReservationRequestValidator {

    public void validateRequest(final ReservationRequest request) {
        if (request == null) {
            throw new InvalidReservationException("request must not be null");
        }
        if (request.carType() == null) {
            throw new InvalidReservationException("carType must not be null");
        }
        if (request.startDateTime() == null) {
            throw new InvalidReservationException("startDateTime must not be null");
        }
        if (request.numberOfDays() <= 0) {
            throw new InvalidReservationException("numberOfDays must be greater than 0");
        }
    }
}
