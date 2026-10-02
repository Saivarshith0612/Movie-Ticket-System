package com.movieticket.service;

import com.movieticket.dao.SeatDAO;
import com.movieticket.exception.ResourceNotFoundException;
import com.movieticket.exception.ValidationException;
import com.movieticket.model.Seat;

import java.util.List;
import java.util.logging.Logger;

public class SeatService {

    private static final Logger logger =
            Logger.getLogger(SeatService.class.getName());

    private final SeatDAO seatDAO;

    public SeatService(SeatDAO seatDAO) {
        this.seatDAO = seatDAO;
    }

    // CREATE
    public void addSeat(Seat seat) {

        if (seat == null) {
            throw new ValidationException("Seat cannot be null.");
        }

        if (seat.getTheatreId() <= 0) {
            throw new ValidationException("Theatre ID must be greater than zero.");
        }

        if (seat.getSeatNumber() == null || seat.getSeatNumber().isBlank()) {
            throw new ValidationException("Seat number is required.");
        }

        if (seat.getSeatType() == null || seat.getSeatType().isBlank()) {
            throw new ValidationException("Seat type is required.");
        }

        if (seat.getPrice() == null || seat.getPrice().signum() <= 0) {
            throw new ValidationException("Seat price must be greater than zero.");
        }

        seatDAO.addSeat(seat);

        logger.info("Seat added successfully.");
    }


    // READ - Get seat by ID
    public Seat getSeatById(int seatId) {

        if (seatId <= 0) {
            throw new ValidationException("Seat ID must be greater than zero.");
        }

        Seat seat = seatDAO.getSeatById(seatId);

        if (seat == null) {
            throw new ResourceNotFoundException(
                    "Seat not found with ID: " + seatId
            );
        }

        logger.info("Seat retrieved successfully.");

        return seat;
    }


    // READ - Get all seats
    public List<Seat> getAllSeats() {

        List<Seat> seats = seatDAO.getAllSeats();

        logger.info(
                "Retrieved " + seats.size() + " seat(s)."
        );

        return seats;
    }

    // UPDATE
    public void updateSeat(Seat seat) {

        if (seat == null) {
            throw new ValidationException("Seat cannot be null.");
        }

        if (seat.getSeatId() <= 0) {
            throw new ValidationException("Seat ID must be greater than zero.");
        }

        if (seat.getTheatreId() <= 0) {
            throw new ValidationException("Theatre ID must be greater than zero.");
        }

        if (seat.getSeatNumber() == null || seat.getSeatNumber().isBlank()) {
            throw new ValidationException("Seat number is required.");
        }

        if (seat.getSeatType() == null || seat.getSeatType().isBlank()) {
            throw new ValidationException("Seat type is required.");
        }

        if (seat.getPrice() == null || seat.getPrice().signum() <= 0) {
            throw new ValidationException("Seat price must be greater than zero.");
        }

        seatDAO.updateSeat(seat);

        logger.info("Seat updated successfully.");
    }


    // DELETE
    public void deleteSeat(int seatId) {

        if (seatId <= 0) {
            throw new ValidationException("Seat ID must be greater than zero.");
        }

        seatDAO.deleteSeat(seatId);

        logger.info("Seat deleted successfully.");
    }
    public List<Seat> findSeatsByTheatreId(int theatreId) {

        if (theatreId <= 0) {
            throw new ValidationException(
                    "Theatre ID must be greater than zero."
            );
        }

        return seatDAO.findSeatsByTheatreId(theatreId);
    }
    public List<Seat> findAvailableSeatsByShowId(
            int theatreId,
            int showId) {

        if (theatreId <= 0) {
            throw new ValidationException(
                    "Theatre ID must be greater than zero."
            );
        }

        if (showId <= 0) {
            throw new ValidationException(
                    "Show ID must be greater than zero."
            );
        }

        return seatDAO.findAvailableSeatsByShowId(
                theatreId,
                showId
        );
    }
}