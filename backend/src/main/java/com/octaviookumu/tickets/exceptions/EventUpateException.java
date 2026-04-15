package com.octaviookumu.tickets.exceptions;

public class EventUpateException extends EventTIcketException {
    public EventUpateException() {
    }

    public EventUpateException(String message) {
        super(message);
    }

    public EventUpateException(String message, Throwable cause) {
        super(message, cause);
    }

    public EventUpateException(Throwable cause) {
        super(cause);
    }

    public EventUpateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
