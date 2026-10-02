package com.movieticket.service;

import com.movieticket.dao.BookingDAO;
import com.movieticket.exception.ResourceNotFoundException;
import com.movieticket.model.Booking;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingDAO bookingDAO;

    @InjectMocks
    private BookingService bookingService;

    @Test
    void shouldAddBookingSuccessfully() {

        Booking booking = new Booking(
                1,
                1,
                LocalDateTime.of(2026, 10, 1, 10, 30),
                new BigDecimal("500.00"),
                "CONFIRMED"
        );

        // BookingDAO now returns the generated booking ID
        when(bookingDAO.addBooking(booking))
                .thenReturn(1);

        int bookingId = bookingService.addBooking(booking);

        assertEquals(1, bookingId);

        verify(bookingDAO).addBooking(booking);
    }

    @Test
    void shouldGetBookingByIdSuccessfully() {

        Booking booking = new Booking(
                1,
                1,
                LocalDateTime.of(2026, 10, 1, 10, 30),
                new BigDecimal("500.00"),
                "CONFIRMED"
        );

        booking.setBookingId(1);

        when(bookingDAO.getBookingById(1))
                .thenReturn(booking);

        Booking result = bookingService.getBookingById(1);

        assertNotNull(result);
        assertEquals(1, result.getBookingId());
        assertEquals(1, result.getShowId());
        assertEquals(1, result.getUserId());

        assertEquals(
                LocalDateTime.of(2026, 10, 1, 10, 30),
                result.getBookingDate()
        );

        assertEquals(
                new BigDecimal("500.00"),
                result.getTotalAmount()
        );

        assertEquals(
                "CONFIRMED",
                result.getBookingStatus()
        );

        verify(bookingDAO).getBookingById(1);
    }

    @Test
    void shouldThrowExceptionWhenBookingDoesNotExist() {

        when(bookingDAO.getBookingById(999))
                .thenReturn(null);

        assertThrows(
                ResourceNotFoundException.class,
                () -> bookingService.getBookingById(999)
        );

        verify(bookingDAO).getBookingById(999);
    }

    @Test
    void shouldGetAllBookingsSuccessfully() {

        Booking booking1 = new Booking(
                1,
                1,
                LocalDateTime.of(2026, 10, 1, 10, 30),
                new BigDecimal("500.00"),
                "CONFIRMED"
        );

        Booking booking2 = new Booking(
                2,
                2,
                LocalDateTime.of(2026, 10, 2, 18, 0),
                new BigDecimal("750.00"),
                "CONFIRMED"
        );

        List<Booking> bookings =
                Arrays.asList(booking1, booking2);

        when(bookingDAO.getAllBookings())
                .thenReturn(bookings);

        List<Booking> result =
                bookingService.getAllBookings();

        assertNotNull(result);
        assertEquals(2, result.size());

        verify(bookingDAO).getAllBookings();
    }

    @Test
    void shouldUpdateBookingSuccessfully() {

        Booking booking = new Booking(
                1,
                1,
                LocalDateTime.of(2026, 10, 1, 11, 0),
                new BigDecimal("600.00"),
                "CONFIRMED"
        );

        booking.setBookingId(1);

        bookingService.updateBooking(booking);

        verify(bookingDAO).updateBooking(booking);
    }

    @Test
    void shouldDeleteBookingSuccessfully() {

        bookingService.deleteBooking(1);

        verify(bookingDAO).deleteBooking(1);
    }
}
