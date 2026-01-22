package org.kzwolski.car.rental.application.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kzwolski.car.rental.application.dto.ReservationRequest;
import org.kzwolski.car.rental.domain.exception.InvalidReservationException;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.kzwolski.car.rental.application.validation.ReservationRequestValidatorTest.TestData.*;
import static org.kzwolski.car.rental.domain.model.CarType.*;

class ReservationRequestValidatorTest {

    private ReservationRequestValidator validator;

    @BeforeEach
    void setUp() {
        this.validator = new ReservationRequestValidator();
    }

    @Test
    void validateRequest_doesNotThrow_whenRequestIsValid() {
        // given
        ReservationRequest validRequest =
                new ReservationRequest(
                        SEDAN,
                        REQUESTED_START_DATE_TIME,
                        TWO_DAYS
                );

        // when
        assertDoesNotThrow(() -> validator.validateRequest(validRequest));

        // then
        // no exception thrown
    }

    @Test
    void validateRequest_throwsInvalidReservationException_whenCarTypeIsNull() {
        // given
        ReservationRequest requestWithNullCarType =
                new ReservationRequest(
                        null,
                        REQUESTED_START_DATE_TIME,
                        TWO_DAYS
                );

        // when
        InvalidReservationException exception = assertThrows(
                InvalidReservationException.class,
                () -> validator.validateRequest(requestWithNullCarType)
        );

        // then
        assertNotNull(exception.getMessage());
    }

    @Test
    void validateRequest_throwsInvalidReservationException_whenStartDateTimeIsNull() {
        // given
        ReservationRequest requestWithNullStartDateTime =
                new ReservationRequest(
                        SEDAN,
                        null,
                        TWO_DAYS
                );

        // when
        InvalidReservationException exception = assertThrows(
                InvalidReservationException.class,
                () -> validator.validateRequest(requestWithNullStartDateTime)
        );

        // then
        assertNotNull(exception.getMessage());
    }

    @Test
    void validateRequest_throwsInvalidReservationException_whenNumberOfDaysIsZero() {
        // given
        ReservationRequest requestWithZeroDays =
                new ReservationRequest(
                        SEDAN,
                        REQUESTED_START_DATE_TIME,
                        ZERO_DAYS
                );

        // when
        InvalidReservationException exception = assertThrows(
                InvalidReservationException.class,
                () -> validator.validateRequest(requestWithZeroDays)
        );

        // then
        assertNotNull(exception.getMessage());
    }

    @Test
    void validateRequest_throwsInvalidReservationException_whenNumberOfDaysIsNegative() {
        // given
        ReservationRequest requestWithNegativeDays =
                new ReservationRequest(
                        SEDAN,
                        REQUESTED_START_DATE_TIME,
                        NEGATIVE_DAYS
                );

        // when
        InvalidReservationException exception = assertThrows(
                InvalidReservationException.class,
                () -> validator.validateRequest(requestWithNegativeDays)
        );

        // then
        assertNotNull(exception.getMessage());
    }

    static final class TestData {
        private TestData() {}

        static final LocalDateTime REQUESTED_START_DATE_TIME =
                LocalDateTime.of(2024, 1, 1, 10, 0);

        static final int NEGATIVE_DAYS = -1;

        static final int ZERO_DAYS = 0;
        static final int TWO_DAYS = 2;
    }
}