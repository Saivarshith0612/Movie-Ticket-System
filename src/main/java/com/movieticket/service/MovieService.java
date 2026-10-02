package com.movieticket.service;

import com.movieticket.dao.MovieDAO;
import com.movieticket.exception.ResourceNotFoundException;
import com.movieticket.exception.ValidationException;
import com.movieticket.model.Movie;

import java.util.List;
import java.util.logging.Logger;

public class MovieService {

    private static final Logger logger =
            Logger.getLogger(MovieService.class.getName());

    private final MovieDAO movieDAO;

    public MovieService(MovieDAO movieDAO) {
        this.movieDAO = movieDAO;
    }

    public void addMovie(Movie movie) {

        if (movie == null) {
            throw new ValidationException("Movie cannot be null.");
        }

        if (movie.getTitle() == null || movie.getTitle().isBlank()) {
            throw new ValidationException("Movie title is required.");
        }

        if (movie.getLanguage() == null || movie.getLanguage().isBlank()) {
            throw new ValidationException("Movie language is required.");
        }

        if (movie.getGenre() == null || movie.getGenre().isBlank()) {
            throw new ValidationException("Movie genre is required.");
        }

        if (movie.getDuration() <= 0) {
            throw new ValidationException("Movie duration must be greater than zero.");
        }

        movieDAO.addMovie(movie);

        logger.info("Movie added successfully.");
    }

    public Movie getMovieById(int movieId) {

        if (movieId <= 0) {
            throw new ValidationException("Movie ID must be greater than zero.");
        }

        Movie movie = movieDAO.getMovieById(movieId);

        if (movie == null) {
            throw new ResourceNotFoundException(
                    "Movie not found with ID: " + movieId
            );
        }

        logger.info("Movie retrieved successfully.");

        return movie;
    }

    public List<Movie> getAllMovies() {

        List<Movie> movies = movieDAO.getAllMovies();

        logger.info("Retrieved " + movies.size() + " movie(s).");

        return movies;
    }

    public List<Movie> findMoviesByTitle(String title) {

        if (title == null || title.isBlank()) {
            throw new ValidationException(
                    "Movie title cannot be empty."
            );
        }

        return movieDAO.findMoviesByTitle(title.trim());
    }

    public void updateMovie(Movie movie) {

        if (movie == null) {
            throw new ValidationException("Movie cannot be null.");
        }

        if (movie.getMovieId() <= 0) {
            throw new ValidationException("Movie ID must be greater than zero.");
        }

        if (movie.getTitle() == null || movie.getTitle().isBlank()) {
            throw new ValidationException("Movie title is required.");
        }

        if (movie.getDuration() <= 0) {
            throw new ValidationException("Movie duration must be greater than zero.");
        }

        movieDAO.updateMovie(movie);

        logger.info("Movie updated successfully.");
    }

    public void deleteMovie(int movieId) {

        if (movieId <= 0) {
            throw new ValidationException("Movie ID must be greater than zero.");
        }

        movieDAO.deleteMovie(movieId);

        logger.info("Movie deleted successfully.");
    }
}