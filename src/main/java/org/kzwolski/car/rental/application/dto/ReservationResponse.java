package org.kzwolski.car.rental.application.dto;

import org.kzwolski.car.rental.domain.model.CarType;

import java.time.LocalDateTime;
import java.util.UUID;

public record ReservationResponse(
        UUID reservationId,
        UUID carId,
        CarType carType,
        LocalDateTime startDateTime,
        LocalDateTime endDateTime
) {
    @Override
    public String toString() {
        return """
               Reservation successful
               ----------------------
               Car type: %s
               Start:    %s
               End:      %s
               """.formatted(carType, startDateTime, endDateTime);
    }
}
