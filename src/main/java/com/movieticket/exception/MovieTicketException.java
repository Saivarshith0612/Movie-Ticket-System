package com.movieticket.exception;

public class MovieTicketException extends RuntimeException {

    public MovieTicketException(String message) {
        super(message);
    }

    public MovieTicketException(String message, Throwable cause) {
        super(message, cause);
    }
}
