package com.movieticket.service;

import com.movieticket.dao.ShowDAO;
import com.movieticket.exception.ResourceNotFoundException;
import com.movieticket.exception.ValidationException;
import com.movieticket.model.Show;

import java.util.List;
import java.util.logging.Logger;

public class ShowService {

    private static final Logger logger =
            Logger.getLogger(ShowService.class.getName());

    private final ShowDAO showDAO;

    public ShowService(ShowDAO showDAO) {
        this.showDAO = showDAO;
    }

    // CREATE
    public void addShow(Show show) {

        if (show == null) {
            throw new ValidationException("Show cannot be null.");
        }

        if (show.getTheatreId() <= 0) {
            throw new ValidationException("Theatre ID must be greater than zero.");
        }

        if (show.getMovieId() <= 0) {
            throw new ValidationException("Movie ID must be greater than zero.");
        }

        if (show.getShowDate() == null) {
            throw new ValidationException("Show date is required.");
        }

        if (show.getStartTime() == null) {
            throw new ValidationException("Start time is required.");
        }

        if (show.getEndTime() == null) {
            throw new ValidationException("End time is required.");
        }

        if (!show.getEndTime().isAfter(show.getStartTime())) {
            throw new ValidationException(
                    "End time must be after start time."
            );
        }

        showDAO.addShow(show);

        logger.info("Show added successfully.");
    }

    // READ - Get show by ID
    public Show getShowById(int showId) {

        if (showId <= 0) {
            throw new ValidationException("Show ID must be greater than zero.");
        }

        Show show = showDAO.getShowById(showId);

        if (show == null) {
            throw new ResourceNotFoundException(
                    "Show not found with ID: " + showId
            );
        }

        logger.info("Show retrieved successfully.");

        return show;
    }

    // READ - Get all shows
    public List<Show> getAllShows() {

        List<Show> shows = showDAO.getAllShows();

        logger.info(
                "Retrieved " + shows.size() + " show(s)."
        );

        return shows;
    }

    // UPDATE
    public void updateShow(Show show) {

        if (show == null) {
            throw new ValidationException("Show cannot be null.");
        }

        if (show.getShowId() <= 0) {
            throw new ValidationException("Show ID must be greater than zero.");
        }

        if (show.getTheatreId() <= 0) {
            throw new ValidationException("Theatre ID must be greater than zero.");
        }

        if (show.getMovieId() <= 0) {
            throw new ValidationException("Movie ID must be greater than zero.");
        }

        if (show.getShowDate() == null) {
            throw new ValidationException("Show date is required.");
        }

        if (show.getStartTime() == null || show.getEndTime() == null) {
            throw new ValidationException("Show start and end times are required.");
        }

        if (!show.getEndTime().isAfter(show.getStartTime())) {
            throw new ValidationException(
                    "End time must be after start time."
            );
        }

        showDAO.updateShow(show);

        logger.info("Show updated successfully.");
    }


    // DELETE
    public void deleteShow(int showId) {

        if (showId <= 0) {
            throw new ValidationException("Show ID must be greater than zero.");
        }

        showDAO.deleteShow(showId);

        logger.info("Show deleted successfully.");
    }
    public List<Show> findShowsByMovieId(int movieId) {

        if (movieId <= 0) {
            throw new ValidationException(
                    "Movie ID must be greater than zero."
            );
        }

        return showDAO.findShowsByMovieId(movieId);
    }
}