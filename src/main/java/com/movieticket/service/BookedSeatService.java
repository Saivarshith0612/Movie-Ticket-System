package com.movieticket.service;

import com.movieticket.dao.BookedSeatDAO;
import com.movieticket.exception.ResourceNotFoundException;
import com.movieticket.exception.ValidationException;
import com.movieticket.model.BookedSeat;

import java.util.List;
import java.util.logging.Logger;

public class BookedSeatService {

    private static final Logger logger =
            Logger.getLogger(BookedSeatService.class.getName());

    private final BookedSeatDAO bookedSeatDAO;

    public BookedSeatService(BookedSeatDAO bookedSeatDAO) {
        this.bookedSeatDAO = bookedSeatDAO;
    }

    // CREATE
    public void addBookedSeat(BookedSeat bookedSeat) {

        if (bookedSeat == null) {
            throw new ValidationException("Booked seat cannot be null.");
        }

        if (bookedSeat.getSeatId() <= 0) {
            throw new ValidationException("Seat ID must be greater than zero.");
        }

        if (bookedSeat.getBookingId() <= 0) {
            throw new ValidationException("Booking ID must be greater than zero.");
        }

        bookedSeatDAO.addBookedSeat(bookedSeat);

        logger.info("Booked seat added successfully.");
    }

    // READ - Get booked seat by ID
    public BookedSeat getBookedSeatById(int bookedSeatId) {

        if (bookedSeatId <= 0) {
            throw new ValidationException(
                    "Booked seat ID must be greater than zero."
            );
        }

        BookedSeat bookedSeat =
                bookedSeatDAO.getBookedSeatById(bookedSeatId);

        if (bookedSeat == null) {
            throw new ResourceNotFoundException(
                    "Booked seat not found with ID: " + bookedSeatId
            );
        }

        logger.info("Booked seat retrieved successfully.");

        return bookedSeat;
    }


    // READ - Get all booked seats
    public List<BookedSeat> getAllBookedSeats() {

        List<BookedSeat> bookedSeats =
                bookedSeatDAO.getAllBookedSeats();

        logger.info(
                "Retrieved "
                        + bookedSeats.size()
                        + " booked seat(s)."
        );

        return bookedSeats;
    }

    // UPDATE
    public void updateBookedSeat(BookedSeat bookedSeat) {

        if (bookedSeat == null) {
            throw new ValidationException("Booked seat cannot be null.");
        }

        if (bookedSeat.getBookedSeatId() <= 0) {
            throw new ValidationException(
                    "Booked seat ID must be greater than zero."
            );
        }

        if (bookedSeat.getSeatId() <= 0) {
            throw new ValidationException("Seat ID must be greater than zero.");
        }

        if (bookedSeat.getBookingId() <= 0) {
            throw new ValidationException(
                    "Booking ID must be greater than zero."
            );
        }

        bookedSeatDAO.updateBookedSeat(bookedSeat);

        logger.info("Booked seat updated successfully.");
    }


    // DELETE
    public void deleteBookedSeat(int bookedSeatId) {

        if (bookedSeatId <= 0) {
            throw new ValidationException(
                    "Booked seat ID must be greater than zero."
            );
        }

        bookedSeatDAO.deleteBookedSeat(bookedSeatId);

        logger.info("Booked seat deleted successfully.");
    }
}