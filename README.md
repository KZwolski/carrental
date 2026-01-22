# Car Rental System – Technical Assessment

## Overview
This project is a simple simulation of a car rental system implemented in Java.  
Its purpose is to demonstrate clean architecture, separation of concerns and testable business logic,
rather than to provide a full production-ready application.

The system allows users to reserve a car of a given type for a specified date and duration,
while respecting limited availability and overlapping reservations.

---

## Architecture
The project follows a layered architecture inspired by **DDD / Clean Architecture** principles.
The core idea is to keep business logic independent from infrastructure and presentation details.

domain
- model
 - exception

application
 - dto
 - service
 - validation 
 - repository interfaces

infrastructure
 - in-memory repository implementations


---

## Module description

### Domain
The `domain` module contains the core business model and rules:

This layer is independent of infrastructure and frameworks and represents the business logic only.

---

### Application
The `application` module contains use cases and orchestration logic:

This layer coordinates domain objects and defines contracts for infrastructure.

---

### Infrastructure
The `infrastructure` module contains in-memory implementations of repository interfaces:

These implementations are intentionally simple and optimized for demo and testing purposes.
In a real-world system, these repositories would be backed by a database.

---

## Business rules
- A car can be reserved only if it is available for the entire requested time range
- Back-to-back reservations are allowed
- The number of cars of each type is limited
- Invalid requests are rejected by validation logic

---

## Testing
The project includes unit tests written with **JUnit 5**, following the `given / when / then` structure:
- `TimeRangeTest` – validates overlap and boundary conditions
- `CarRentalServiceTest` – verifies core reservation scenarios
- `ReservationRequestValidatorTest` – ensures invalid input is rejected

Tests focus on business rules and use case behavior rather than implementation details.

---

## Demo
A simple `main` method is provided to demonstrate the system end-to-end:
- successful reservation
- back-to-back reservation
- reservation failure when no cars are available

The demo is intended only for presentation purposes and is not a full application.
