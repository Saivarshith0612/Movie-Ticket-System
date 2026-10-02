package com.movieticket.dao;

import com.movieticket.exception.MovieTicketException;
import com.movieticket.model.Show;
import com.movieticket.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.logging.Logger;

public class ShowDAO {

    private static final String FIND_BY_MOVIE_ID =
            "SELECT show_id, theatre_id, movie_id, show_date, " +
                    "start_time, end_time " +
                    "FROM `shows` WHERE movie_id = ?";

    // SQL Queries
    private static final String INSERT_SHOW = """
            INSERT INTO shows
            (theatre_id, movie_id, show_date, start_time, end_time)
            VALUES (?, ?, ?, ?, ?)
            """;

    private static final String SELECT_SHOW_BY_ID = """
            SELECT show_id, theatre_id, movie_id, show_date, start_time, end_time
            FROM shows
            WHERE show_id = ?
            """;

    private static final String SELECT_ALL_SHOWS = """
            SELECT show_id, theatre_id, movie_id, show_date, start_time, end_time
            FROM shows
            """;

    private static final String UPDATE_SHOW = """
            UPDATE shows
            SET theatre_id = ?, movie_id = ?, show_date = ?, start_time = ?, end_time = ?
            WHERE show_id = ?
            """;

    private static final String DELETE_SHOW = """
            DELETE FROM shows
            WHERE show_id = ?
            """;

    // Logger
    private static final Logger logger =
            Logger.getLogger(ShowDAO.class.getName());

    // CREATE
    public void addShow(Show show) {

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(INSERT_SHOW)
        ) {

            statement.setInt(1, show.getTheatreId());
            statement.setInt(2, show.getMovieId());

            statement.setDate(
                    3,
                    java.sql.Date.valueOf(show.getShowDate())
            );

            statement.setTime(
                    4,
                    java.sql.Time.valueOf(show.getStartTime())
            );

            statement.setTime(
                    5,
                    java.sql.Time.valueOf(show.getEndTime())
            );

            statement.executeUpdate();

            logger.info("Show added successfully!");

        } catch (SQLException e) {
            logger.severe(
                    "Error while adding show: " + e.getMessage()
            );
        }
    }

    // READ - Get show by ID
    public Show getShowById(int showId) {

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(SELECT_SHOW_BY_ID)
        ) {

            statement.setInt(1, showId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {

                    Show show = new Show();

                    show.setShowId(
                            resultSet.getInt("show_id")
                    );

                    show.setTheatreId(
                            resultSet.getInt("theatre_id")
                    );

                    show.setMovieId(
                            resultSet.getInt("movie_id")
                    );

                    show.setShowDate(
                            resultSet.getDate("show_date")
                                    .toLocalDate()
                    );

                    show.setStartTime(
                            resultSet.getTime("start_time")
                                    .toLocalTime()
                    );

                    show.setEndTime(
                            resultSet.getTime("end_time")
                                    .toLocalTime()
                    );

                    return show;
                }
            }

        } catch (SQLException e) {
            logger.severe(
                    "Error while retrieving show: "
                            + e.getMessage()
            );
        }

        return null;
    }

    // READ - Get all shows
    public List<Show> getAllShows() {

        List<Show> shows = new ArrayList<>();

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(SELECT_ALL_SHOWS);
                ResultSet resultSet = statement.executeQuery()
        ) {

            while (resultSet.next()) {

                Show show = new Show();

                show.setShowId(
                        resultSet.getInt("show_id")
                );

                show.setTheatreId(
                        resultSet.getInt("theatre_id")
                );

                show.setMovieId(
                        resultSet.getInt("movie_id")
                );

                show.setShowDate(
                        resultSet.getDate("show_date")
                                .toLocalDate()
                );

                show.setStartTime(
                        resultSet.getTime("start_time")
                                .toLocalTime()
                );

                show.setEndTime(
                        resultSet.getTime("end_time")
                                .toLocalTime()
                );

                shows.add(show);
            }

        } catch (SQLException e) {
            logger.severe(
                    "Error while retrieving shows: "
                            + e.getMessage()
            );
        }

        return shows;
    }

    // UPDATE
    public void updateShow(Show show) {

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(UPDATE_SHOW)
        ) {

            statement.setInt(1, show.getTheatreId());
            statement.setInt(2, show.getMovieId());

            statement.setDate(
                    3,
                    java.sql.Date.valueOf(show.getShowDate())
            );

            statement.setTime(
                    4,
                    java.sql.Time.valueOf(show.getStartTime())
            );

            statement.setTime(
                    5,
                    java.sql.Time.valueOf(show.getEndTime())
            );

            statement.setInt(6, show.getShowId());

            statement.executeUpdate();

            logger.info("Show updated successfully!");

        } catch (SQLException e) {
            logger.severe(
                    "Error while updating show: "
                            + e.getMessage()
            );
        }
    }

    // DELETE
    public void deleteShow(int showId) {

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(DELETE_SHOW)
        ) {

            statement.setInt(1, showId);

            statement.executeUpdate();

            logger.info("Show deleted successfully!");

        } catch (SQLException e) {
            logger.severe(
                    "Error while deleting show: "
                            + e.getMessage()
            );
        }
    }
    public List<Show> findShowsByMovieId(int movieId) {

        List<Show> shows = new ArrayList<>();

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement preparedStatement =
                        connection.prepareStatement(FIND_BY_MOVIE_ID)
        ) {

            preparedStatement.setInt(1, movieId);

            ResultSet resultSet =
                    preparedStatement.executeQuery();

            while (resultSet.next()) {

                Show show = new Show();

                show.setShowId(
                        resultSet.getInt("show_id")
                );

                show.setTheatreId(
                        resultSet.getInt("theatre_id")
                );

                show.setMovieId(
                        resultSet.getInt("movie_id")
                );

                Date showDate =
                        resultSet.getDate("show_date");

                if (showDate != null) {
                    show.setShowDate(
                            showDate.toLocalDate()
                    );
                }

                Time startTime =
                        resultSet.getTime("start_time");

                if (startTime != null) {
                    show.setStartTime(
                            startTime.toLocalTime()
                    );
                }

                Time endTime =
                        resultSet.getTime("end_time");

                if (endTime != null) {
                    show.setEndTime(
                            endTime.toLocalTime()
                    );
                }

                shows.add(show);
            }

        } catch (SQLException e) {

            logger.severe(
                    "Error finding shows by movie ID: "
                            + e.getMessage()
            );

            throw new MovieTicketException(
                    "Unable to find shows for the movie.",
                    e
            );
        }

        return shows;
    }
}
