package com.movieticket.dao;

import com.movieticket.model.BookedSeat;
import com.movieticket.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class BookedSeatDAO {

    // SQL Queries
    private static final String INSERT_BOOKED_SEAT = """
            INSERT INTO booked_seats
            (seat_id, booking_id)
            VALUES (?, ?)
            """;

    private static final String SELECT_BOOKED_SEAT_BY_ID = """
            SELECT booked_seat_id, seat_id, booking_id
            FROM booked_seats
            WHERE booked_seat_id = ?
            """;

    private static final String SELECT_ALL_BOOKED_SEATS = """
            SELECT booked_seat_id, seat_id, booking_id
            FROM booked_seats
            """;

    private static final String UPDATE_BOOKED_SEAT = """
            UPDATE booked_seats
            SET seat_id = ?, booking_id = ?
            WHERE booked_seat_id = ?
            """;

    private static final String DELETE_BOOKED_SEAT = """
            DELETE FROM booked_seats
            WHERE booked_seat_id = ?
            """;

    // Logger
    private static final Logger logger =
            Logger.getLogger(BookedSeatDAO.class.getName());

    // CREATE
    public void addBookedSeat(BookedSeat bookedSeat) {

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(INSERT_BOOKED_SEAT)
        ) {

            statement.setInt(1, bookedSeat.getSeatId());
            statement.setInt(2, bookedSeat.getBookingId());

            statement.executeUpdate();



        } catch (SQLException e) {
            logger.severe(
                    "Error while adding booked seat: "
                            + e.getMessage()
            );
        }
    }

    // READ - Get booked seat by ID
    public BookedSeat getBookedSeatById(int bookedSeatId) {

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(
                                SELECT_BOOKED_SEAT_BY_ID
                        )
        ) {

            statement.setInt(1, bookedSeatId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {

                    BookedSeat bookedSeat = new BookedSeat();

                    bookedSeat.setBookedSeatId(
                            resultSet.getInt("booked_seat_id")
                    );

                    bookedSeat.setSeatId(
                            resultSet.getInt("seat_id")
                    );

                    bookedSeat.setBookingId(
                            resultSet.getInt("booking_id")
                    );

                    return bookedSeat;
                }
            }

        } catch (SQLException e) {
            logger.severe(
                    "Error while retrieving booked seat: "
                            + e.getMessage()
            );
        }

        return null;
    }

    // READ - Get all booked seats
    public List<BookedSeat> getAllBookedSeats() {

        List<BookedSeat> bookedSeats = new ArrayList<>();

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(
                                SELECT_ALL_BOOKED_SEATS
                        );
                ResultSet resultSet = statement.executeQuery()
        ) {

            while (resultSet.next()) {

                BookedSeat bookedSeat = new BookedSeat();

                bookedSeat.setBookedSeatId(
                        resultSet.getInt("booked_seat_id")
                );

                bookedSeat.setSeatId(
                        resultSet.getInt("seat_id")
                );

                bookedSeat.setBookingId(
                        resultSet.getInt("booking_id")
                );

                bookedSeats.add(bookedSeat);
            }

        } catch (SQLException e) {
            logger.severe(
                    "Error while retrieving booked seats: "
                            + e.getMessage()
            );
        }

        return bookedSeats;
    }

    // UPDATE
    public void updateBookedSeat(BookedSeat bookedSeat) {

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(
                                UPDATE_BOOKED_SEAT
                        )
        ) {

            statement.setInt(1, bookedSeat.getSeatId());
            statement.setInt(2, bookedSeat.getBookingId());
            statement.setInt(3, bookedSeat.getBookedSeatId());

            statement.executeUpdate();

            logger.info("Booked seat updated successfully!");

        } catch (SQLException e) {
            logger.severe(
                    "Error while updating booked seat: "
                            + e.getMessage()
            );
        }
    }

    // DELETE
    public void deleteBookedSeat(int bookedSeatId) {

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(
                                DELETE_BOOKED_SEAT
                        )
        ) {

            statement.setInt(1, bookedSeatId);

            statement.executeUpdate();

            logger.info("Booked seat deleted successfully!");

        } catch (SQLException e) {
            logger.severe(
                    "Error while deleting booked seat: "
                            + e.getMessage()
            );
        }
    }
}
