package com.movieticket.exception;

public class ResourceNotFoundException extends MovieTicketException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}