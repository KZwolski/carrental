package org.kzwolski.car.rental.application.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kzwolski.car.rental.application.CarInventoryRepository;
import org.kzwolski.car.rental.application.ReservationRepository;
import org.kzwolski.car.rental.application.dto.ReservationRequest;
import org.kzwolski.car.rental.application.dto.ReservationResponse;
import org.kzwolski.car.rental.application.validation.ReservationRequestValidator;
import org.kzwolski.car.rental.domain.exception.InvalidReservationException;
import org.kzwolski.car.rental.domain.exception.NoCarAvailableException;
import org.kzwolski.car.rental.domain.model.Car;
import org.kzwolski.car.rental.domain.model.CarType;
import org.kzwolski.car.rental.infrastructure.CarInventoryRepositoryImpl;
import org.kzwolski.car.rental.infrastructure.ReservationRepositoryImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.kzwolski.car.rental.application.service.CarRentalServiceTest.TestData.*;
import static org.kzwolski.car.rental.domain.model.CarType.*;

class CarRentalServiceTest {

    private ReservationRepository reservationRepository;
    private ReservationRequestValidator requestValidator;

    @BeforeEach
    void setUp() {
        this.reservationRepository = new ReservationRepositoryImpl();
        this.requestValidator = new ReservationRequestValidator();
    }

    @Test
    void reserve_returnsReservation_whenCarOfRequestedTypeIsAvailable() {
        // given
        Car firstAvailableSedan = new Car(SEDAN);
        Car secondAvailableSedan = new Car(SEDAN);

        CarInventoryRepository inventoryRepository =
                inventoryWithCars(Map.of(SEDAN, List.of(firstAvailableSedan, secondAvailableSedan)));

        CarRentalService carRentalService =
                new CarRentalService(inventoryRepository, reservationRepository, requestValidator);

        ReservationRequest request =
                new ReservationRequest(
                        SEDAN,
                        REQUESTED_START_DATE_TIME,
                        TWO_DAYS
                );

        // when
        ReservationResponse response = carRentalService.reserve(request);

        // then
        assertNotNull(response);
        assertEquals(SEDAN, response.carType());
        assertEquals(REQUESTED_START_DATE_TIME, response.startDateTime());
        assertEquals(REQUESTED_START_DATE_TIME.plusDays(TWO_DAYS), response.endDateTime()
        );
    }

    @Test
    void reserve_throwsNoCarAvailableException_whenAllCarsOfRequestedTypeAreAlreadyReserved() {
        // given
        Car singleAvailableSedan = new Car(SEDAN);

        CarInventoryRepository inventoryRepository =
                inventoryWithCars(Map.of(SEDAN, List.of(singleAvailableSedan)));

        CarRentalService carRentalService =
                new CarRentalService(inventoryRepository, reservationRepository, requestValidator);

        ReservationRequest existingReservationRequest =
                new ReservationRequest(
                        SEDAN,
                        REQUESTED_START_DATE_TIME,
                        THREE_DAYS
                );

        // given – existing reservation occupies the only car
        carRentalService.reserve(existingReservationRequest);

        ReservationRequest overlappingReservationRequest =
                new ReservationRequest(
                        SEDAN,
                        REQUESTED_START_DATE_TIME.plusHours(ONE_HOUR),
                        ONE_DAY
                );

        // when
        NoCarAvailableException exception = assertThrows(
                NoCarAvailableException.class,
                () -> carRentalService.reserve(overlappingReservationRequest)
        );

        // then
        assertNotNull(exception.getMessage());
    }

    @Test
    void reserve_allowsBackToBackReservations_whenSecondReservationStartsAtEndOfFirst() {
        // given
        Car singleAvailableSedan = new Car(SEDAN);

        CarInventoryRepository inventoryRepository =
                inventoryWithCars(Map.of(SEDAN, List.of(singleAvailableSedan)));

        CarRentalService carRentalService =
                new CarRentalService(inventoryRepository, reservationRepository, requestValidator);

        ReservationRequest firstReservationRequest =
                new ReservationRequest(
                        SEDAN,
                        REQUESTED_START_DATE_TIME,
                        TWO_DAYS
                );

        ReservationRequest backToBackReservationRequest =
                new ReservationRequest(
                        SEDAN,
                        REQUESTED_START_DATE_TIME.plusDays(TWO_DAYS),
                        ONE_DAY
                );

        // when
        ReservationResponse firstResponse = carRentalService.reserve(firstReservationRequest);
        ReservationResponse secondResponse = carRentalService.reserve(backToBackReservationRequest);

        // then
        assertNotNull(firstResponse);
        assertNotNull(secondResponse);
        assertEquals(firstResponse.endDateTime(), secondResponse.startDateTime());
    }

    @Test
    void reserve_throwsInvalidReservationException_whenNumberOfDaysIsNotPositive() {
        // given
        Car availableSedan = new Car(SEDAN);

        CarInventoryRepository inventoryRepository =
                inventoryWithCars(Map.of(SEDAN, List.of(availableSedan)));

        CarRentalService carRentalService =
                new CarRentalService(inventoryRepository, reservationRepository, requestValidator);

        ReservationRequest invalidRequest =
                new ReservationRequest(
                        SEDAN,
                        REQUESTED_START_DATE_TIME,
                        ZERO_DAYS
                );

        // when
        InvalidReservationException exception = assertThrows(
                InvalidReservationException.class,
                () -> carRentalService.reserve(invalidRequest)
        );

        // then
        assertNotNull(exception.getMessage());
    }

    private static CarInventoryRepository inventoryWithCars(
            Map<CarType, List<Car>> carsByType) {
        return new CarInventoryRepositoryImpl(carsByType);
    }

    static final class TestData {
        static final LocalDateTime REQUESTED_START_DATE_TIME =
                LocalDateTime.of(2024, 1, 1, 10, 0);

        static final int ZERO_DAYS = 0;
        static final int ONE_DAY = 1;
        static final int TWO_DAYS = 2;
        static final int THREE_DAYS = 3;
        static final int ONE_HOUR = 1;
    }
}

