package com.movieticket.service;

import com.movieticket.dao.PaymentDAO;
import com.movieticket.model.Payment;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import com.movieticket.exception.ResourceNotFoundException;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentDAO paymentDAO;

    @InjectMocks
    private PaymentService paymentService;

    @Test
    void shouldAddPaymentSuccessfully() {

        Payment payment = new Payment(
                1,
                new BigDecimal("500.00"),
                "CARD",
                "SUCCESS",
                LocalDateTime.of(2026, 10, 1, 11, 0)
        );

        paymentService.addPayment(payment);

        verify(paymentDAO).addPayment(payment);
    }

    @Test
    void shouldGetPaymentByIdSuccessfully() {

        Payment payment = new Payment(
                1,
                new BigDecimal("500.00"),
                "CARD",
                "SUCCESS",
                LocalDateTime.of(2026, 10, 1, 11, 0)
        );

        payment.setPaymentId(1);

        when(paymentDAO.getPaymentById(1))
                .thenReturn(payment);

        Payment result =
                paymentService.getPaymentById(1);

        assertNotNull(result);
        assertEquals(1, result.getPaymentId());
        assertEquals(1, result.getBookingId());
        assertEquals(
                new BigDecimal("500.00"),
                result.getAmount()
        );
        assertEquals(
                "CARD",
                result.getPaymentMethod()
        );
        assertEquals(
                "SUCCESS",
                result.getPaymentStatus()
        );
        assertEquals(
                LocalDateTime.of(2026, 10, 1, 11, 0),
                result.getPaymentDate()
        );

        verify(paymentDAO).getPaymentById(1);
    }

    @Test
    void shouldThrowExceptionWhenPaymentDoesNotExist() {
        when(paymentDAO.getPaymentById(999)).thenReturn(null);

        assertThrows(
                ResourceNotFoundException.class,
                () -> paymentService.getPaymentById(999)
        );

        verify(paymentDAO).getPaymentById(999);
    }

    @Test
    void shouldGetAllPaymentsSuccessfully() {

        Payment payment1 = new Payment(
                1,
                new BigDecimal("500.00"),
                "CARD",
                "SUCCESS",
                LocalDateTime.of(2026, 10, 1, 11, 0)
        );

        Payment payment2 = new Payment(
                2,
                new BigDecimal("750.00"),
                "UPI",
                "SUCCESS",
                LocalDateTime.of(2026, 10, 2, 18, 30)
        );

        List<Payment> payments =
                Arrays.asList(payment1, payment2);

        when(paymentDAO.getAllPayments())
                .thenReturn(payments);

        List<Payment> result =
                paymentService.getAllPayments();

        assertNotNull(result);
        assertEquals(2, result.size());

        verify(paymentDAO).getAllPayments();
    }

    @Test
    void shouldUpdatePaymentSuccessfully() {

        Payment payment = new Payment(
                1,
                new BigDecimal("600.00"),
                "UPI",
                "SUCCESS",
                LocalDateTime.of(2026, 10, 1, 12, 0)
        );

        payment.setPaymentId(1);

        paymentService.updatePayment(payment);

        verify(paymentDAO).updatePayment(payment);
    }

    @Test
    void shouldDeletePaymentSuccessfully() {

        paymentService.deletePayment(1);

        verify(paymentDAO).deletePayment(1);
    }
}