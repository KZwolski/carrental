package org.kzwolski.car.rental.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class TimeRange {

  private final LocalDateTime startDate;

  private final LocalDateTime endDate;

  public TimeRange(LocalDateTime start, LocalDateTime end) {
    if (start == null || end == null) {
      throw new IllegalArgumentException("Start and end must not be null");
    }
    if (!start.isBefore(end)) {
      throw new IllegalArgumentException("Start must be before end");
    }
    this.startDate = start;
    this.endDate = end;
  }

  public boolean overlaps(TimeRange other) {
    Objects.requireNonNull(other, "other must not be null");
    return this.startDate.isBefore(other.endDate) && this.endDate.isAfter(other.startDate);
  }

  public LocalDateTime getStartDate() {
    return startDate;
  }

  public LocalDateTime getEndDate() {
    return endDate;
  }
}
