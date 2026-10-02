package com.movieticket.service;

import com.movieticket.dao.BookingDAO;
import com.movieticket.exception.MovieTicketException;
import com.movieticket.exception.ResourceNotFoundException;
import com.movieticket.exception.ValidationException;
import com.movieticket.model.Booking;

import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Logger;

public class BookingService {

    private static final Logger logger =
            Logger.getLogger(
                    BookingService.class.getName()
            );

    private final BookingDAO bookingDAO;

    public BookingService(BookingDAO bookingDAO) {
        this.bookingDAO = bookingDAO;
    }

    // CREATE
    public int addBooking(Booking booking) {

        if (booking == null) {
            throw new ValidationException(
                    "Booking cannot be null."
            );
        }

        if (booking.getShowId() <= 0) {
            throw new ValidationException(
                    "Show ID must be greater than zero."
            );
        }

        if (booking.getUserId() <= 0) {
            throw new ValidationException(
                    "User ID must be greater than zero."
            );
        }

        if (booking.getBookingDate() == null) {
            throw new ValidationException(
                    "Booking date is required."
            );
        }

        if (booking.getTotalAmount() == null
                || booking.getTotalAmount()
                .compareTo(BigDecimal.ZERO) <= 0) {

            throw new ValidationException(
                    "Total amount must be greater than zero."
            );
        }

        if (booking.getBookingStatus() == null
                || booking.getBookingStatus().isBlank()) {

            throw new ValidationException(
                    "Booking status is required."
            );
        }

        int bookingId =
                bookingDAO.addBooking(booking);

        if (bookingId <= 0) {
            throw new MovieTicketException(
                    "Unable to create booking."
            );
        }

        logger.info(
                "Booking added successfully. Booking ID: "
                        + bookingId
        );

        return bookingId;
    }

    // READ - Get booking by ID
    public Booking getBookingById(int bookingId) {

        if (bookingId <= 0) {
            throw new ValidationException(
                    "Booking ID must be greater than zero."
            );
        }

        Booking booking =
                bookingDAO.getBookingById(bookingId);

        if (booking == null) {
            throw new ResourceNotFoundException(
                    "Booking not found with ID: "
                            + bookingId
            );
        }

        logger.info(
                "Booking retrieved successfully."
        );

        return booking;
    }

    // READ - Get all bookings
    public List<Booking> getAllBookings() {

        List<Booking> bookings =
                bookingDAO.getAllBookings();

        logger.info(
                "Retrieved "
                        + bookings.size()
                        + " booking(s)."
        );

        return bookings;
    }

    // UPDATE
    public void updateBooking(Booking booking) {

        if (booking == null) {
            throw new ValidationException(
                    "Booking cannot be null."
            );
        }

        if (booking.getBookingId() <= 0) {
            throw new ValidationException(
                    "Booking ID must be greater than zero."
            );
        }

        if (booking.getShowId() <= 0) {
            throw new ValidationException(
                    "Show ID must be greater than zero."
            );
        }

        if (booking.getUserId() <= 0) {
            throw new ValidationException(
                    "User ID must be greater than zero."
            );
        }

        if (booking.getBookingDate() == null) {
            throw new ValidationException(
                    "Booking date is required."
            );
        }

        if (booking.getTotalAmount() == null
                || booking.getTotalAmount()
                .compareTo(BigDecimal.ZERO) <= 0) {

            throw new ValidationException(
                    "Total amount must be greater than zero."
            );
        }

        if (booking.getBookingStatus() == null
                || booking.getBookingStatus().isBlank()) {

            throw new ValidationException(
                    "Booking status is required."
            );
        }

        bookingDAO.updateBooking(booking);

        logger.info(
                "Booking updated successfully."
        );
    }

    // DELETE
    public void deleteBooking(int bookingId) {

        if (bookingId <= 0) {
            throw new ValidationException(
                    "Booking ID must be greater than zero."
            );
        }

        bookingDAO.deleteBooking(bookingId);

        logger.info(
                "Booking deleted successfully."
        );
    }
}
