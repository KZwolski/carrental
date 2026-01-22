package org.kzwolski.car.rental;

import org.kzwolski.car.rental.application.dto.ReservationRequest;
import org.kzwolski.car.rental.application.service.CarRentalService;
import org.kzwolski.car.rental.application.validation.ReservationRequestValidator;
import org.kzwolski.car.rental.domain.exception.NoCarAvailableException;
import org.kzwolski.car.rental.domain.model.Car;
import org.kzwolski.car.rental.domain.model.CarType;
import org.kzwolski.car.rental.infrastructure.CarInventoryRepositoryImpl;
import org.kzwolski.car.rental.infrastructure.ReservationRepositoryImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        // --- infrastructure setup ---
        final var inventoryRepository =
                new CarInventoryRepositoryImpl(
                        Map.of(
                                CarType.SEDAN, List.of(
                                        new Car(CarType.SEDAN)
                                ),
                                CarType.SUV, List.of(
                                        new Car(CarType.SUV)
                                )
                        )
                );

        final var reservationRepository = new ReservationRepositoryImpl();
        final var validator = new ReservationRequestValidator();

        final var carRentalService =
                new CarRentalService(inventoryRepository, reservationRepository, validator);

        final var startDateTime = LocalDateTime.of(2024, 1, 1, 10, 0);

        // --- happy path ---
        System.out.println("=== FIRST RESERVATION ===");
        final var firstRequest =
                new ReservationRequest(CarType.SEDAN, startDateTime, 2);

        final var firstReservation =
                carRentalService.reserve(firstRequest);

        System.out.println(firstReservation);

        // --- back-to-back reservation ---
        System.out.println("\n=== BACK-TO-BACK RESERVATION ===");
        final var secondRequest =
                new ReservationRequest(
                        CarType.SEDAN,
                        firstReservation.endDateTime(),
                        1
                );

        final var secondReservation =
                carRentalService.reserve(secondRequest);

        System.out.println(secondReservation);

        // --- no availability ---
        System.out.println("\n=== RESERVATION WITH NO AVAILABLE CARS ===");
        try {
            final var failingRequest =
                    new ReservationRequest(
                            CarType.SEDAN,
                            startDateTime.plusHours(1),
                            1
                    );

            carRentalService.reserve(failingRequest);
        } catch (NoCarAvailableException ex) {
            System.out.println("Reservation failed: " + ex.getMessage());
        }
    }
}
