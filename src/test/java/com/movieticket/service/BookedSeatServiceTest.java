package com.movieticket.service;

import com.movieticket.dao.BookedSeatDAO;
import com.movieticket.model.BookedSeat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import com.movieticket.exception.ResourceNotFoundException;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookedSeatServiceTest {

    @Mock
    private BookedSeatDAO bookedSeatDAO;

    @InjectMocks
    private BookedSeatService bookedSeatService;

    @Test
    void shouldAddBookedSeatSuccessfully() {

        BookedSeat bookedSeat =
                new BookedSeat(1, 1);

        bookedSeatService.addBookedSeat(bookedSeat);

        verify(bookedSeatDAO).addBookedSeat(bookedSeat);
    }

    @Test
    void shouldGetBookedSeatByIdSuccessfully() {

        BookedSeat bookedSeat =
                new BookedSeat(1, 1);

        bookedSeat.setBookedSeatId(1);

        when(bookedSeatDAO.getBookedSeatById(1))
                .thenReturn(bookedSeat);

        BookedSeat result =
                bookedSeatService.getBookedSeatById(1);

        assertNotNull(result);
        assertEquals(1, result.getBookedSeatId());
        assertEquals(1, result.getSeatId());
        assertEquals(1, result.getBookingId());

        verify(bookedSeatDAO).getBookedSeatById(1);
    }

    @Test
    void shouldThrowExceptionWhenBookedSeatDoesNotExist() {
        when(bookedSeatDAO.getBookedSeatById(999)).thenReturn(null);

        assertThrows(
                ResourceNotFoundException.class,
                () -> bookedSeatService.getBookedSeatById(999)
        );

        verify(bookedSeatDAO).getBookedSeatById(999);
    }
    @Test
    void shouldGetAllBookedSeatsSuccessfully() {

        BookedSeat bookedSeat1 =
                new BookedSeat(1, 1);

        BookedSeat bookedSeat2 =
                new BookedSeat(2, 1);

        List<BookedSeat> bookedSeats =
                Arrays.asList(bookedSeat1, bookedSeat2);

        when(bookedSeatDAO.getAllBookedSeats())
                .thenReturn(bookedSeats);

        List<BookedSeat> result =
                bookedSeatService.getAllBookedSeats();

        assertNotNull(result);
        assertEquals(2, result.size());

        verify(bookedSeatDAO).getAllBookedSeats();
    }

    @Test
    void shouldUpdateBookedSeatSuccessfully() {

        BookedSeat bookedSeat =
                new BookedSeat(2, 1);

        bookedSeat.setBookedSeatId(1);

        bookedSeatService.updateBookedSeat(bookedSeat);

        verify(bookedSeatDAO).updateBookedSeat(bookedSeat);
    }

    @Test
    void shouldDeleteBookedSeatSuccessfully() {

        bookedSeatService.deleteBookedSeat(1);

        verify(bookedSeatDAO).deleteBookedSeat(1);
    }
}