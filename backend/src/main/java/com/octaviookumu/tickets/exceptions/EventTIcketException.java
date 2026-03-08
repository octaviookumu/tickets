package com.octaviookumu.tickets.exceptions;

/**
 * Extending RunTimeException means we don't have to declare throws of the exception on any methods that throw this exception
 */
public class EventTIcketException extends RuntimeException {
    public EventTIcketException() {
    }

    public EventTIcketException(String message) {
        super(message);
    }

    public EventTIcketException(String message, Throwable cause) {
        super(message, cause);
    }

    public EventTIcketException(Throwable cause) {
        super(cause);
    }

    public EventTIcketException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
