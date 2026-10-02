package com.movieticket.dao;

import com.movieticket.model.Payment;
import com.movieticket.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class PaymentDAO {

    // SQL Queries
    private static final String INSERT_PAYMENT = """
            INSERT INTO payments
            (booking_id, amount, payment_method, payment_status, payment_date)
            VALUES (?, ?, ?, ?, ?)
            """;

    private static final String SELECT_PAYMENT_BY_ID = """
            SELECT payment_id, booking_id, amount, payment_method,
                   payment_status, payment_date
            FROM payments
            WHERE payment_id = ?
            """;

    private static final String SELECT_ALL_PAYMENTS = """
            SELECT payment_id, booking_id, amount, payment_method,
                   payment_status, payment_date
            FROM payments
            """;

    private static final String UPDATE_PAYMENT = """
            UPDATE payments
            SET booking_id = ?, amount = ?, payment_method = ?,
                payment_status = ?, payment_date = ?
            WHERE payment_id = ?
            """;

    private static final String DELETE_PAYMENT = """
            DELETE FROM payments
            WHERE payment_id = ?
            """;

    // Logger
    private static final Logger logger =
            Logger.getLogger(PaymentDAO.class.getName());

    // CREATE
    public void addPayment(Payment payment) {

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(INSERT_PAYMENT)
        ) {

            statement.setInt(1, payment.getBookingId());
            statement.setBigDecimal(2, payment.getAmount());
            statement.setString(3, payment.getPaymentMethod());
            statement.setString(4, payment.getPaymentStatus());
            statement.setTimestamp(
                    5,
                    Timestamp.valueOf(payment.getPaymentDate())
            );

            statement.executeUpdate();


        } catch (SQLException e) {
            logger.severe(
                    "Error while adding payment: "
                            + e.getMessage()
            );
        }
    }

    // READ - Get payment by ID
    public Payment getPaymentById(int paymentId) {

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(
                                SELECT_PAYMENT_BY_ID
                        )
        ) {

            statement.setInt(1, paymentId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {

                    Payment payment = new Payment();

                    payment.setPaymentId(
                            resultSet.getInt("payment_id")
                    );

                    payment.setBookingId(
                            resultSet.getInt("booking_id")
                    );

                    payment.setAmount(
                            resultSet.getBigDecimal("amount")
                    );

                    payment.setPaymentMethod(
                            resultSet.getString("payment_method")
                    );

                    payment.setPaymentStatus(
                            resultSet.getString("payment_status")
                    );

                    payment.setPaymentDate(
                            resultSet.getTimestamp("payment_date")
                                    .toLocalDateTime()
                    );

                    return payment;
                }
            }

        } catch (SQLException e) {
            logger.severe(
                    "Error while retrieving payment: "
                            + e.getMessage()
            );
        }

        return null;
    }

    // READ - Get all payments
    public List<Payment> getAllPayments() {

        List<Payment> payments = new ArrayList<>();

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(
                                SELECT_ALL_PAYMENTS
                        );
                ResultSet resultSet = statement.executeQuery()
        ) {

            while (resultSet.next()) {

                Payment payment = new Payment();

                payment.setPaymentId(
                        resultSet.getInt("payment_id")
                );

                payment.setBookingId(
                        resultSet.getInt("booking_id")
                );

                payment.setAmount(
                        resultSet.getBigDecimal("amount")
                );

                payment.setPaymentMethod(
                        resultSet.getString("payment_method")
                );

                payment.setPaymentStatus(
                        resultSet.getString("payment_status")
                );

                payment.setPaymentDate(
                        resultSet.getTimestamp("payment_date")
                                .toLocalDateTime()
                );

                payments.add(payment);
            }

        } catch (SQLException e) {
            logger.severe(
                    "Error while retrieving payments: "
                            + e.getMessage()
            );
        }

        return payments;
    }

    // UPDATE
    public void updatePayment(Payment payment) {

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(
                                UPDATE_PAYMENT
                        )
        ) {

            statement.setInt(1, payment.getBookingId());
            statement.setBigDecimal(2, payment.getAmount());
            statement.setString(3, payment.getPaymentMethod());
            statement.setString(4, payment.getPaymentStatus());
            statement.setTimestamp(
                    5,
                    Timestamp.valueOf(payment.getPaymentDate())
            );
            statement.setInt(6, payment.getPaymentId());

            statement.executeUpdate();

            logger.info("Payment updated successfully!");

        } catch (SQLException e) {
            logger.severe(
                    "Error while updating payment: "
                            + e.getMessage()
            );
        }
    }

    // DELETE
    public void deletePayment(int paymentId) {

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(
                                DELETE_PAYMENT
                        )
        ) {

            statement.setInt(1, paymentId);

            statement.executeUpdate();

            logger.info("Payment deleted successfully!");

        } catch (SQLException e) {
            logger.severe(
                    "Error while deleting payment: "
                            + e.getMessage()
            );
        }
    }
}