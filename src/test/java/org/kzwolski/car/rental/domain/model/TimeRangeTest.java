package org.kzwolski.car.rental.domain.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.kzwolski.car.rental.domain.model.TimeRangeTest.TestData.*;

class TimeRangeTest {

    @Test
    void overlaps_returnsTrue_whenTimeRangesIntersect() {
        // given
        TimeRange firstReservationWindow =
                new TimeRange(
                        TEN_AM,
                        TWELVE_PM
                );

        TimeRange secondReservationWindow =
                new TimeRange(
                        ELEVEN_AM,
                        ONE_PM
                );

        // when
        boolean overlaps = firstReservationWindow.overlaps(secondReservationWindow);

        // then
        assertTrue(overlaps);
    }

    @Test
    void overlaps_returnsFalse_whenTimeRangesOnlyTouchAtBoundary() {
        // given
        TimeRange morningReservationWindow =
                new TimeRange(
                        TEN_AM,
                        TWELVE_PM
                );

        TimeRange afternoonReservationWindow =
                new TimeRange(
                        TWELVE_PM,
                        TWO_PM
                );

        // when
        boolean overlaps = morningReservationWindow.overlaps(afternoonReservationWindow);

        // then
        assertFalse(overlaps);
    }

    @Test
    void overlaps_returnsTrue_whenOneTimeRangeFullyContainsAnother() {
        // given
        TimeRange fullDayReservationWindow =
                new TimeRange(
                        NINE_AM,
                        FIVE_PM
                );

        TimeRange lunchReservationWindow =
                new TimeRange(
                        TWELVE_PM,
                        ONE_PM
                );

        // when
        boolean overlaps = fullDayReservationWindow.overlaps(lunchReservationWindow);

        // then
        assertTrue(overlaps);
    }

    @Test
    void constructor_throwsIllegalArgumentException_whenStartEqualsEnd() {
        // given
        LocalDateTime sameMoment = TEN_AM;

        // when
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new TimeRange(sameMoment, sameMoment)
        );

        // then
        assertNotNull(exception.getMessage());
    }

    @Test
    void constructor_throwsIllegalArgumentException_whenStartIsAfterEnd() {
        // given
        LocalDateTime startDateTime = TWELVE_PM;
        LocalDateTime endDateTime = TEN_AM;

        // when
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new TimeRange(startDateTime, endDateTime)
        );

        // then
        assertNotNull(exception.getMessage());
    }

    @Test
    void overlaps_throwsNullPointerException_whenOtherTimeRangeIsNull() {
        // given
        TimeRange reservationWindow =
                new TimeRange(
                        TEN_AM,
                        TWELVE_PM
                );

        // when
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> reservationWindow.overlaps(null)
        );

        // then
        assertNotNull(exception.getMessage());
    }

    static final class TestData {
        static final LocalDateTime NINE_AM = LocalDateTime.of(2024, 1, 1, 9, 0);
        static final LocalDateTime TEN_AM = LocalDateTime.of(2024, 1, 1, 10, 0);
        static final LocalDateTime ELEVEN_AM = LocalDateTime.of(2024, 1, 1, 11, 0);
        static final LocalDateTime TWELVE_PM = LocalDateTime.of(2024, 1, 1, 12, 0);
        static final LocalDateTime ONE_PM = LocalDateTime.of(2024, 1, 1, 13, 0);
        static final LocalDateTime TWO_PM = LocalDateTime.of(2024, 1, 1, 14, 0);
        static final LocalDateTime FIVE_PM = LocalDateTime.of(2024, 1, 1, 17, 0);
    }
}

