package org.kzwolski.car.rental.domain.exception;

public class NoCarAvailableException extends RuntimeException {

    public NoCarAvailableException(String message) {
        super(message);
    }
}
