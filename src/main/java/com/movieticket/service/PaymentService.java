package com.movieticket.service;

import com.movieticket.dao.PaymentDAO;
import com.movieticket.exception.ResourceNotFoundException;
import com.movieticket.exception.ValidationException;
import com.movieticket.model.Payment;

import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Logger;

public class PaymentService {

    private static final Logger logger =
            Logger.getLogger(PaymentService.class.getName());

    private final PaymentDAO paymentDAO;

    public PaymentService(PaymentDAO paymentDAO) {
        this.paymentDAO = paymentDAO;
    }

    // CREATE
    public void addPayment(Payment payment) {

        if (payment == null) {
            throw new ValidationException("Payment cannot be null.");
        }

        if (payment.getBookingId() <= 0) {
            throw new ValidationException(
                    "Booking ID must be greater than zero."
            );
        }

        if (payment.getAmount() == null
                || payment.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException(
                    "Payment amount must be greater than zero."
            );
        }

        if (payment.getPaymentMethod() == null
                || payment.getPaymentMethod().isBlank()) {
            throw new ValidationException(
                    "Payment method is required."
            );
        }

        if (payment.getPaymentStatus() == null
                || payment.getPaymentStatus().isBlank()) {
            throw new ValidationException(
                    "Payment status is required."
            );
        }

        if (payment.getPaymentDate() == null) {
            throw new ValidationException(
                    "Payment date is required."
            );
        }

        paymentDAO.addPayment(payment);

        logger.info("Payment added successfully.");
    }


    // READ - Get payment by ID
    public Payment getPaymentById(int paymentId) {

        if (paymentId <= 0) {
            throw new ValidationException(
                    "Payment ID must be greater than zero."
            );
        }

        Payment payment = paymentDAO.getPaymentById(paymentId);

        if (payment == null) {
            throw new ResourceNotFoundException(
                    "Payment not found with ID: " + paymentId
            );
        }

        logger.info("Payment retrieved successfully.");

        return payment;
    }

    // READ - Get all payments
    public List<Payment> getAllPayments() {

        List<Payment> payments =
                paymentDAO.getAllPayments();

        logger.info(
                "Retrieved "
                        + payments.size()
                        + " payment(s)."
        );

        return payments;
    }

    // UPDATE
    public void updatePayment(Payment payment) {

        if (payment == null) {
            throw new ValidationException("Payment cannot be null.");
        }

        if (payment.getPaymentId() <= 0) {
            throw new ValidationException(
                    "Payment ID must be greater than zero."
            );
        }

        if (payment.getBookingId() <= 0) {
            throw new ValidationException(
                    "Booking ID must be greater than zero."
            );
        }

        if (payment.getAmount() == null
                || payment.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException(
                    "Payment amount must be greater than zero."
            );
        }

        if (payment.getPaymentMethod() == null
                || payment.getPaymentMethod().isBlank()) {
            throw new ValidationException(
                    "Payment method is required."
            );
        }

        if (payment.getPaymentStatus() == null
                || payment.getPaymentStatus().isBlank()) {
            throw new ValidationException(
                    "Payment status is required."
            );
        }

        if (payment.getPaymentDate() == null) {
            throw new ValidationException(
                    "Payment date is required."
            );
        }

        paymentDAO.updatePayment(payment);

        logger.info("Payment updated successfully.");
    }


    // DELETE
    public void deletePayment(int paymentId) {

        if (paymentId <= 0) {
            throw new ValidationException(
                    "Payment ID must be greater than zero."
            );
        }

        paymentDAO.deletePayment(paymentId);

        logger.info("Payment deleted successfully.");
    }
}