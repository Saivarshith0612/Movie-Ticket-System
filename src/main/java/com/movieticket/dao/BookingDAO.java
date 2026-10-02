package com.movieticket.dao;

import com.movieticket.model.Booking;
import com.movieticket.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class BookingDAO {

    // SQL Queries
    private static final String INSERT_BOOKING = """
            INSERT INTO bookings
            (show_id, user_id, booking_date, total_amount, booking_status)
            VALUES (?, ?, ?, ?, ?)
            """;

    private static final String SELECT_BOOKING_BY_ID = """
            SELECT booking_id, show_id, user_id, booking_date,
                   total_amount, booking_status
            FROM bookings
            WHERE booking_id = ?
            """;

    private static final String SELECT_ALL_BOOKINGS = """
            SELECT booking_id, show_id, user_id, booking_date,
                   total_amount, booking_status
            FROM bookings
            """;

    private static final String UPDATE_BOOKING = """
            UPDATE bookings
            SET show_id = ?, user_id = ?, booking_date = ?,
                total_amount = ?, booking_status = ?
            WHERE booking_id = ?
            """;

    private static final String DELETE_BOOKING = """
            DELETE FROM bookings
            WHERE booking_id = ?
            """;

    // Logger
    private static final Logger logger =
            Logger.getLogger(BookingDAO.class.getName());

    // CREATE
    public int addBooking(Booking booking) {

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(
                                INSERT_BOOKING,
                                Statement.RETURN_GENERATED_KEYS
                        )
        ) {

            statement.setInt(
                    1,
                    booking.getShowId()
            );

            statement.setInt(
                    2,
                    booking.getUserId()
            );

            statement.setTimestamp(
                    3,
                    Timestamp.valueOf(
                            booking.getBookingDate()
                    )
            );

            statement.setBigDecimal(
                    4,
                    booking.getTotalAmount()
            );

            statement.setString(
                    5,
                    booking.getBookingStatus()
            );

            statement.executeUpdate();

            try (ResultSet resultSet =
                         statement.getGeneratedKeys()) {

                if (resultSet.next()) {

                    int bookingId =
                            resultSet.getInt(1);


                    return bookingId;
                }
            }

        } catch (SQLException e) {

            logger.severe(
                    "Error while adding booking: "
                            + e.getMessage()
            );
        }

        return 0;
    }

    // READ - Get booking by ID
    public Booking getBookingById(int bookingId) {

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(
                                SELECT_BOOKING_BY_ID
                        )
        ) {

            statement.setInt(
                    1,
                    bookingId
            );

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {

                    Booking booking =
                            new Booking();

                    booking.setBookingId(
                            resultSet.getInt("booking_id")
                    );

                    booking.setShowId(
                            resultSet.getInt("show_id")
                    );

                    booking.setUserId(
                            resultSet.getInt("user_id")
                    );

                    booking.setBookingDate(
                            resultSet
                                    .getTimestamp("booking_date")
                                    .toLocalDateTime()
                    );

                    booking.setTotalAmount(
                            resultSet.getBigDecimal(
                                    "total_amount"
                            )
                    );

                    booking.setBookingStatus(
                            resultSet.getString(
                                    "booking_status"
                            )
                    );

                    return booking;
                }
            }

        } catch (SQLException e) {

            logger.severe(
                    "Error while retrieving booking: "
                            + e.getMessage()
            );
        }

        return null;
    }

    // READ - Get all bookings
    public List<Booking> getAllBookings() {

        List<Booking> bookings =
                new ArrayList<>();

        try (
                Connection connection =
                        DBConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(
                                SELECT_ALL_BOOKINGS
                        );

                ResultSet resultSet =
                        statement.executeQuery()
        ) {

            while (resultSet.next()) {

                Booking booking =
                        new Booking();

                booking.setBookingId(
                        resultSet.getInt("booking_id")
                );

                booking.setShowId(
                        resultSet.getInt("show_id")
                );

                booking.setUserId(
                        resultSet.getInt("user_id")
                );

                booking.setBookingDate(
                        resultSet
                                .getTimestamp("booking_date")
                                .toLocalDateTime()
                );

                booking.setTotalAmount(
                        resultSet.getBigDecimal(
                                "total_amount"
                        )
                );

                booking.setBookingStatus(
                        resultSet.getString(
                                "booking_status"
                        )
                );

                bookings.add(booking);
            }

        } catch (SQLException e) {

            logger.severe(
                    "Error while retrieving bookings: "
                            + e.getMessage()
            );
        }

        return bookings;
    }

    // UPDATE
    public void updateBooking(Booking booking) {

        try (
                Connection connection =
                        DBConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(
                                UPDATE_BOOKING
                        )
        ) {

            statement.setInt(
                    1,
                    booking.getShowId()
            );

            statement.setInt(
                    2,
                    booking.getUserId()
            );

            statement.setTimestamp(
                    3,
                    Timestamp.valueOf(
                            booking.getBookingDate()
                    )
            );

            statement.setBigDecimal(
                    4,
                    booking.getTotalAmount()
            );

            statement.setString(
                    5,
                    booking.getBookingStatus()
            );

            statement.setInt(
                    6,
                    booking.getBookingId()
            );

            statement.executeUpdate();

            logger.info(
                    "Booking updated successfully!"
            );

        } catch (SQLException e) {

            logger.severe(
                    "Error while updating booking: "
                            + e.getMessage()
            );
        }
    }

    // DELETE
    public void deleteBooking(int bookingId) {

        try (
                Connection connection =
                        DBConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(
                                DELETE_BOOKING
                        )
        ) {

            statement.setInt(
                    1,
                    bookingId
            );

            statement.executeUpdate();

            logger.info(
                    "Booking deleted successfully!"
            );

        } catch (SQLException e) {

            logger.severe(
                    "Error while deleting booking: "
                            + e.getMessage()
            );
        }
    }
}