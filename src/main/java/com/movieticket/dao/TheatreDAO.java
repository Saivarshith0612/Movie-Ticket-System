package com.movieticket.dao;

import com.movieticket.model.Theatre;
import com.movieticket.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import com.movieticket.exception.MovieTicketException;

public class TheatreDAO {

    private static final String FIND_BY_MOVIE_ID =
            "SELECT DISTINCT t.theatre_id, t.name, t.city, " +
                    "t.address, t.total_seats " +
                    "FROM theatres t " +
                    "JOIN `shows` s ON t.theatre_id = s.theatre_id " +
                    "WHERE s.movie_id = ?";

    // SQL Queries
    private static final String INSERT_THEATRE = """
            INSERT INTO theatres
            (name, city, address, total_seats)
            VALUES (?, ?, ?, ?)
            """;

    private static final String SELECT_THEATRE_BY_ID = """
            SELECT theatre_id, name, city, address, total_seats
            FROM theatres
            WHERE theatre_id = ?
            """;

    private static final String SELECT_ALL_THEATRES = """
            SELECT theatre_id, name, city, address, total_seats
            FROM theatres
            """;

    private static final String UPDATE_THEATRE = """
            UPDATE theatres
            SET name = ?, city = ?, address = ?, total_seats = ?
            WHERE theatre_id = ?
            """;

    private static final String DELETE_THEATRE = """
            DELETE FROM theatres
            WHERE theatre_id = ?
            """;

    // Logger
    private static final Logger logger =
            Logger.getLogger(TheatreDAO.class.getName());

    // CREATE
    public void addTheatre(Theatre theatre) {

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(INSERT_THEATRE)
        ) {

            statement.setString(1, theatre.getName());
            statement.setString(2, theatre.getCity());
            statement.setString(3, theatre.getAddress());
            statement.setInt(4, theatre.getTotalSeats());

            statement.executeUpdate();

            logger.info("Theatre added successfully!");

        } catch (SQLException e) {
            logger.severe(
                    "Error while adding theatre: " + e.getMessage()
            );
        }
    }

    // READ - Get theatre by ID
    public Theatre getTheatreById(int theatreId) {

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(SELECT_THEATRE_BY_ID)
        ) {

            statement.setInt(1, theatreId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {

                    Theatre theatre = new Theatre();

                    theatre.setTheatreId(
                            resultSet.getInt("theatre_id")
                    );

                    theatre.setName(
                            resultSet.getString("name")
                    );

                    theatre.setCity(
                            resultSet.getString("city")
                    );

                    theatre.setAddress(
                            resultSet.getString("address")
                    );

                    theatre.setTotalSeats(
                            resultSet.getInt("total_seats")
                    );

                    return theatre;
                }
            }

        } catch (SQLException e) {
            logger.severe(
                    "Error while retrieving theatre: "
                            + e.getMessage()
            );
        }

        return null;
    }

    // READ - Get all theatres
    public List<Theatre> getAllTheatres() {

        List<Theatre> theatres = new ArrayList<>();

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(SELECT_ALL_THEATRES);
                ResultSet resultSet = statement.executeQuery()
        ) {

            while (resultSet.next()) {

                Theatre theatre = new Theatre();

                theatre.setTheatreId(
                        resultSet.getInt("theatre_id")
                );

                theatre.setName(
                        resultSet.getString("name")
                );

                theatre.setCity(
                        resultSet.getString("city")
                );

                theatre.setAddress(
                        resultSet.getString("address")
                );

                theatre.setTotalSeats(
                        resultSet.getInt("total_seats")
                );

                theatres.add(theatre);
            }

        } catch (SQLException e) {
            logger.severe(
                    "Error while retrieving theatres: "
                            + e.getMessage()
            );
        }

        return theatres;
    }

    // UPDATE
    public void updateTheatre(Theatre theatre) {

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(UPDATE_THEATRE)
        ) {

            statement.setString(1, theatre.getName());
            statement.setString(2, theatre.getCity());
            statement.setString(3, theatre.getAddress());
            statement.setInt(4, theatre.getTotalSeats());
            statement.setInt(5, theatre.getTheatreId());

            statement.executeUpdate();

            logger.info("Theatre updated successfully!");

        } catch (SQLException e) {
            logger.severe(
                    "Error while updating theatre: "
                            + e.getMessage()
            );
        }
    }

    // DELETE
    public void deleteTheatre(int theatreId) {

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(DELETE_THEATRE)
        ) {

            statement.setInt(1, theatreId);

            statement.executeUpdate();

            logger.info("Theatre deleted successfully!");

        } catch (SQLException e) {
            logger.severe(
                    "Error while deleting theatre: "
                            + e.getMessage()
            );
        }
    }
    public List<Theatre> findTheatresByMovieId(int movieId) {

        List<Theatre> theatres = new ArrayList<>();

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement preparedStatement =
                        connection.prepareStatement(FIND_BY_MOVIE_ID)
        ) {

            preparedStatement.setInt(1, movieId);

            ResultSet resultSet =
                    preparedStatement.executeQuery();

            while (resultSet.next()) {

                Theatre theatre = new Theatre();

                theatre.setTheatreId(
                        resultSet.getInt("theatre_id")
                );

                theatre.setName(
                        resultSet.getString("name")
                );

                theatre.setCity(
                        resultSet.getString("city")
                );

                theatre.setAddress(
                        resultSet.getString("address")
                );

                theatre.setTotalSeats(
                        resultSet.getInt("total_seats")
                );

                theatres.add(theatre);
            }

        } catch (SQLException e) {

            logger.severe(
                    "Error finding theatres by movie ID: "
                            + e.getMessage()
            );

            throw new MovieTicketException(
                    "Unable to find theatres for the movie.",
                    e
            );
        }

        return theatres;
    }
}
