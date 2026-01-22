package org.kzwolski.car.rental.application.dto;

import org.kzwolski.car.rental.domain.model.CarType;

import java.time.LocalDateTime;

public record ReservationRequest(
        CarType carType,
        LocalDateTime startDateTime,
        int numberOfDays
) {
}
