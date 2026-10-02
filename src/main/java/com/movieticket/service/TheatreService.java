package com.movieticket.service;

import com.movieticket.dao.TheatreDAO;
import com.movieticket.exception.ResourceNotFoundException;
import com.movieticket.exception.ValidationException;
import com.movieticket.model.Theatre;

import java.util.List;
import java.util.logging.Logger;

public class TheatreService {

    private static final Logger logger =
            Logger.getLogger(TheatreService.class.getName());

    private final TheatreDAO theatreDAO;

    public TheatreService(TheatreDAO theatreDAO) {
        this.theatreDAO = theatreDAO;
    }

    // CREATE
    public void addTheatre(Theatre theatre) {

        if (theatre == null) {
            throw new ValidationException("Theatre cannot be null.");
        }

        if (theatre.getName() == null || theatre.getName().isBlank()) {
            throw new ValidationException("Theatre name is required.");
        }

        if (theatre.getCity() == null || theatre.getCity().isBlank()) {
            throw new ValidationException("Theatre city is required.");
        }

        if (theatre.getAddress() == null || theatre.getAddress().isBlank()) {
            throw new ValidationException("Theatre address is required.");
        }

        if (theatre.getTotalSeats() <= 0) {
            throw new ValidationException("Total seats must be greater than zero.");
        }

        theatreDAO.addTheatre(theatre);

        logger.info("Theatre added successfully.");
    }

    // READ - Get theatre by ID
    public Theatre getTheatreById(int theatreId) {

        if (theatreId <= 0) {
            throw new ValidationException("Theatre ID must be greater than zero.");
        }

        Theatre theatre = theatreDAO.getTheatreById(theatreId);

        if (theatre == null) {
            throw new ResourceNotFoundException(
                    "Theatre not found with ID: " + theatreId
            );
        }

        logger.info("Theatre retrieved successfully.");

        return theatre;
    }


    // READ - Get all theatres
    public List<Theatre> getAllTheatres() {

        List<Theatre> theatres = theatreDAO.getAllTheatres();

        logger.info(
                "Retrieved " + theatres.size() + " theatre(s)."
        );

        return theatres;
    }

    // UPDATE
    public void updateTheatre(Theatre theatre) {

        if (theatre == null) {
            throw new ValidationException("Theatre cannot be null.");
        }

        if (theatre.getTheatreId() <= 0) {
            throw new ValidationException("Theatre ID must be greater than zero.");
        }

        if (theatre.getName() == null || theatre.getName().isBlank()) {
            throw new ValidationException("Theatre name is required.");
        }

        if (theatre.getCity() == null || theatre.getCity().isBlank()) {
            throw new ValidationException("Theatre city is required.");
        }

        if (theatre.getTotalSeats() <= 0) {
            throw new ValidationException("Total seats must be greater than zero.");
        }

        theatreDAO.updateTheatre(theatre);

        logger.info("Theatre updated successfully.");
    }

    // DELETE
    public void deleteTheatre(int theatreId) {

        if (theatreId <= 0) {
            throw new ValidationException("Theatre ID must be greater than zero.");
        }

        theatreDAO.deleteTheatre(theatreId);

        logger.info("Theatre deleted successfully.");
    }

    public List<Theatre> findTheatresByMovieId(int movieId) {

        if (movieId <= 0) {
            throw new ValidationException(
                    "Movie ID must be greater than zero."
            );
        }

        return theatreDAO.findTheatresByMovieId(movieId);
    }
}