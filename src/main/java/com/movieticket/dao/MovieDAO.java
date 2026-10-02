package com.movieticket.dao;


import com.movieticket.model.Movie;
import com.movieticket.util.DBConnection;
import com.movieticket.exception.MovieTicketException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Date;
import java.util.logging.Logger;

public class MovieDAO {
    private static final String FIND_BY_TITLE =
            "SELECT movie_id, title, language, genre, duration, release_date " +
                    "FROM movies WHERE title LIKE ?";

    // SQL Queries
    private static final String INSERT_MOVIE = """
            INSERT INTO movies
            (title, language, genre, duration, release_date)
            VALUES (?, ?, ?, ?, ?)
            """;

    private static final String SELECT_MOVIE_BY_ID = """
            SELECT movie_id, title, language, genre, duration, release_date
            FROM movies
            WHERE movie_id = ?
            """;

    private static final String SELECT_ALL_MOVIES = """
            SELECT movie_id, title, language, genre, duration, release_date
            FROM movies
            """;

    private static final String UPDATE_MOVIE = """
            UPDATE movies
            SET title = ?, language = ?, genre = ?, duration = ?, release_date = ?
            WHERE movie_id = ?
            """;

    private static final String DELETE_MOVIE = """
            DELETE FROM movies
            WHERE movie_id = ?
            """;

    // Logger
    private static final Logger logger =
            Logger.getLogger(MovieDAO.class.getName());

    // CREATE
    public void addMovie(Movie movie) {

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(INSERT_MOVIE)
        ) {

            statement.setString(1, movie.getTitle());
            statement.setString(2, movie.getLanguage());
            statement.setString(3, movie.getGenre());
            statement.setInt(4, movie.getDuration());

            statement.setDate(
                    5,
                    movie.getReleaseDate() != null
                            ? java.sql.Date.valueOf(movie.getReleaseDate())
                            : null
            );

            statement.executeUpdate();

            logger.info("Movie added successfully!");

        } catch (SQLException e) {
            logger.severe("Error while adding movie: " + e.getMessage());
        }
    }

    // READ - Get movie by ID
    public Movie getMovieById(int movieId) {

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(SELECT_MOVIE_BY_ID)
        ) {

            statement.setInt(1, movieId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {

                    Movie movie = new Movie();

                    movie.setMovieId(
                            resultSet.getInt("movie_id")
                    );

                    movie.setTitle(
                            resultSet.getString("title")
                    );

                    movie.setLanguage(
                            resultSet.getString("language")
                    );

                    movie.setGenre(
                            resultSet.getString("genre")
                    );

                    movie.setDuration(
                            resultSet.getInt("duration")
                    );

                    if (resultSet.getDate("release_date") != null) {
                        movie.setReleaseDate(
                                resultSet.getDate("release_date")
                                        .toLocalDate()
                        );
                    }

                    return movie;
                }
            }

        } catch (SQLException e) {
            logger.severe(
                    "Error while retrieving movie: " + e.getMessage()
            );
        }

        return null;
    }

    // READ - Get all movies
    public List<Movie> getAllMovies() {

        List<Movie> movies = new ArrayList<>();

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(SELECT_ALL_MOVIES);
                ResultSet resultSet = statement.executeQuery()
        ) {

            while (resultSet.next()) {

                Movie movie = new Movie();

                movie.setMovieId(
                        resultSet.getInt("movie_id")
                );

                movie.setTitle(
                        resultSet.getString("title")
                );

                movie.setLanguage(
                        resultSet.getString("language")
                );

                movie.setGenre(
                        resultSet.getString("genre")
                );

                movie.setDuration(
                        resultSet.getInt("duration")
                );

                if (resultSet.getDate("release_date") != null) {
                    movie.setReleaseDate(
                            resultSet.getDate("release_date")
                                    .toLocalDate()
                    );
                }

                movies.add(movie);
            }

        } catch (SQLException e) {
            logger.severe(
                    "Error while retrieving movies: " + e.getMessage()
            );
        }

        return movies;
    }

    // UPDATE
    public void updateMovie(Movie movie) {

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(UPDATE_MOVIE)
        ) {

            statement.setString(1, movie.getTitle());
            statement.setString(2, movie.getLanguage());
            statement.setString(3, movie.getGenre());
            statement.setInt(4, movie.getDuration());

            statement.setDate(
                    5,
                    movie.getReleaseDate() != null
                            ? java.sql.Date.valueOf(movie.getReleaseDate())
                            : null
            );

            statement.setInt(6, movie.getMovieId());

            statement.executeUpdate();

            logger.info("Movie updated successfully!");

        } catch (SQLException e) {
            logger.severe(
                    "Error while updating movie: " + e.getMessage()
            );
        }
    }

    // DELETE
    public void deleteMovie(int movieId) {

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(DELETE_MOVIE)
        ) {

            statement.setInt(1, movieId);

            statement.executeUpdate();

            logger.info("Movie deleted successfully!");

        } catch (SQLException e) {
            logger.severe(
                    "Error while deleting movie: " + e.getMessage()
            );
        }
    }

    public List<Movie> findMoviesByTitle(String title) {

        List<Movie> movies = new ArrayList<>();

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement preparedStatement =
                        connection.prepareStatement(FIND_BY_TITLE)
        ) {

            preparedStatement.setString(1, "%" + title + "%");

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                Movie movie = new Movie();

                movie.setMovieId(resultSet.getInt("movie_id"));
                movie.setTitle(resultSet.getString("title"));
                movie.setLanguage(resultSet.getString("language"));
                movie.setGenre(resultSet.getString("genre"));
                movie.setDuration(resultSet.getInt("duration"));

                Date releaseDate =
                        resultSet.getDate("release_date");

                if (releaseDate != null) {
                    movie.setReleaseDate(
                            releaseDate.toLocalDate()
                    );
                }

                movies.add(movie);
            }

        } catch (SQLException e) {

            logger.severe(
                    "Error searching movies by title: "
                            + e.getMessage()
            );

            throw new MovieTicketException(
                    "Unable to search movies by title.",
                    e
            );
        }

        return movies;
    }
}