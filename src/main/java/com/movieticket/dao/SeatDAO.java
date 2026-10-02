package com.movieticket.dao;

import com.movieticket.model.Seat;
import com.movieticket.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import com.movieticket.exception.MovieTicketException;

public class SeatDAO {
    private static final String FIND_AVAILABLE_SEATS_BY_SHOW_ID =
            """
            SELECT s.seat_id, s.theatre_id, s.seat_number,
                   s.seat_type, s.price
            FROM seats s
            WHERE s.theatre_id = ?
            AND s.seat_id NOT IN (
                SELECT bs.seat_id
                FROM booked_seats bs
                JOIN bookings b ON bs.booking_id = b.booking_id
                WHERE b.show_id = ?
            )
            """;

    private static final String FIND_BY_THEATRE_ID =
            "SELECT seat_id, theatre_id, seat_number, " +
                    "seat_type, price " +
                    "FROM seats WHERE theatre_id = ?";

    // SQL Queries
    private static final String INSERT_SEAT = """
            INSERT INTO seats
            (theatre_id, seat_number, seat_type, price)
            VALUES (?, ?, ?, ?)
            """;

    private static final String SELECT_SEAT_BY_ID = """
            SELECT seat_id, theatre_id, seat_number, seat_type, price
            FROM seats
            WHERE seat_id = ?
            """;

    private static final String SELECT_ALL_SEATS = """
            SELECT seat_id, theatre_id, seat_number, seat_type, price
            FROM seats
            """;

    private static final String UPDATE_SEAT = """
            UPDATE seats
            SET theatre_id = ?, seat_number = ?, seat_type = ?, price = ?
            WHERE seat_id = ?
            """;

    private static final String DELETE_SEAT = """
            DELETE FROM seats
            WHERE seat_id = ?
            """;

    // Logger
    private static final Logger logger =
            Logger.getLogger(SeatDAO.class.getName());

    // CREATE
    public void addSeat(Seat seat) {

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(INSERT_SEAT)
        ) {

            statement.setInt(1, seat.getTheatreId());
            statement.setString(2, seat.getSeatNumber());
            statement.setString(3, seat.getSeatType());
            statement.setBigDecimal(4, seat.getPrice());

            statement.executeUpdate();

            logger.info("Seat added successfully!");

        } catch (SQLException e) {
            logger.severe(
                    "Error while adding seat: " + e.getMessage()
            );
        }
    }

    // READ - Get seat by ID
    public Seat getSeatById(int seatId) {

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(SELECT_SEAT_BY_ID)
        ) {

            statement.setInt(1, seatId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {

                    Seat seat = new Seat();

                    seat.setSeatId(
                            resultSet.getInt("seat_id")
                    );

                    seat.setTheatreId(
                            resultSet.getInt("theatre_id")
                    );

                    seat.setSeatNumber(
                            resultSet.getString("seat_number")
                    );

                    seat.setSeatType(
                            resultSet.getString("seat_type")
                    );

                    seat.setPrice(
                            resultSet.getBigDecimal("price")
                    );

                    return seat;
                }
            }

        } catch (SQLException e) {
            logger.severe(
                    "Error while retrieving seat: "
                            + e.getMessage()
            );
        }

        return null;
    }

    // READ - Get all seats
    public List<Seat> getAllSeats() {

        List<Seat> seats = new ArrayList<>();

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(SELECT_ALL_SEATS);
                ResultSet resultSet = statement.executeQuery()
        ) {

            while (resultSet.next()) {

                Seat seat = new Seat();

                seat.setSeatId(
                        resultSet.getInt("seat_id")
                );

                seat.setTheatreId(
                        resultSet.getInt("theatre_id")
                );

                seat.setSeatNumber(
                        resultSet.getString("seat_number")
                );

                seat.setSeatType(
                        resultSet.getString("seat_type")
                );

                seat.setPrice(
                        resultSet.getBigDecimal("price")
                );

                seats.add(seat);
            }

        } catch (SQLException e) {
            logger.severe(
                    "Error while retrieving seats: "
                            + e.getMessage()
            );
        }

        return seats;
    }

    // UPDATE
    public void updateSeat(Seat seat) {

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(UPDATE_SEAT)
        ) {

            statement.setInt(1, seat.getTheatreId());
            statement.setString(2, seat.getSeatNumber());
            statement.setString(3, seat.getSeatType());
            statement.setBigDecimal(4, seat.getPrice());
            statement.setInt(5, seat.getSeatId());

            statement.executeUpdate();

            logger.info("Seat updated successfully!");

        } catch (SQLException e) {
            logger.severe(
                    "Error while updating seat: "
                            + e.getMessage()
            );
        }
    }

    // DELETE
    public void deleteSeat(int seatId) {

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(DELETE_SEAT)
        ) {

            statement.setInt(1, seatId);

            statement.executeUpdate();

            logger.info("Seat deleted successfully!");

        } catch (SQLException e) {
            logger.severe(
                    "Error while deleting seat: "
                            + e.getMessage()
            );
        }
    }
    public List<Seat> findSeatsByTheatreId(int theatreId) {

        List<Seat> seats = new ArrayList<>();

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement preparedStatement =
                        connection.prepareStatement(FIND_BY_THEATRE_ID)
        ) {

            preparedStatement.setInt(1, theatreId);

            ResultSet resultSet =
                    preparedStatement.executeQuery();

            while (resultSet.next()) {

                Seat seat = new Seat();

                seat.setSeatId(
                        resultSet.getInt("seat_id")
                );

                seat.setTheatreId(
                        resultSet.getInt("theatre_id")
                );

                seat.setSeatNumber(
                        resultSet.getString("seat_number")
                );

                seat.setSeatType(
                        resultSet.getString("seat_type")
                );

                seat.setPrice(
                        resultSet.getBigDecimal("price")
                );

                seats.add(seat);
            }

        } catch (SQLException e) {

            logger.severe(
                    "Error finding seats by theatre ID: "
                            + e.getMessage()
            );

            throw new MovieTicketException(
                    "Unable to find seats for the theatre.",
                    e
            );
        }

        return seats;
    }
    public List<Seat> findAvailableSeatsByShowId(
            int theatreId,
            int showId) {

        List<Seat> seats = new ArrayList<>();

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(
                                FIND_AVAILABLE_SEATS_BY_SHOW_ID
                        )
        ) {
            statement.setInt(1, theatreId);
            statement.setInt(2, showId);

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {

                    Seat seat = new Seat();

                    seat.setSeatId(
                            resultSet.getInt("seat_id")
                    );

                    seat.setTheatreId(
                            resultSet.getInt("theatre_id")
                    );

                    seat.setSeatNumber(
                            resultSet.getString("seat_number")
                    );

                    seat.setSeatType(
                            resultSet.getString("seat_type")
                    );

                    seat.setPrice(
                            resultSet.getBigDecimal("price")
                    );

                    seats.add(seat);
                }
            }

        } catch (SQLException e) {

            logger.severe(
                    "Error while retrieving available seats: "
                            + e.getMessage()
            );
        }

        return seats;
    }
}