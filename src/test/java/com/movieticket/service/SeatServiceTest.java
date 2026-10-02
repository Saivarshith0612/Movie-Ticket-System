package com.movieticket.service;

import com.movieticket.dao.SeatDAO;
import com.movieticket.model.Seat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import com.movieticket.exception.ResourceNotFoundException;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SeatServiceTest {

    @Mock
    private SeatDAO seatDAO;

    @InjectMocks
    private SeatService seatService;

    @Test
    void shouldAddSeatSuccessfully() {

        Seat seat = new Seat(
                1,
                "A1",
                "REGULAR",
                new BigDecimal("150.00")
        );

        seatService.addSeat(seat);

        verify(seatDAO).addSeat(seat);
    }

    @Test
    void shouldGetSeatByIdSuccessfully() {

        Seat seat = new Seat(
                1,
                "A1",
                "REGULAR",
                new BigDecimal("150.00")
        );

        seat.setSeatId(1);

        when(seatDAO.getSeatById(1))
                .thenReturn(seat);

        Seat result =
                seatService.getSeatById(1);

        assertNotNull(result);
        assertEquals(1, result.getSeatId());
        assertEquals(1, result.getTheatreId());
        assertEquals("A1", result.getSeatNumber());
        assertEquals("REGULAR", result.getSeatType());
        assertEquals(
                new BigDecimal("150.00"),
                result.getPrice()
        );

        verify(seatDAO).getSeatById(1);
    }

    @Test
    void shouldThrowExceptionWhenSeatDoesNotExist() {

        when(seatDAO.getSeatById(999))
                .thenReturn(null);

        assertThrows(
                ResourceNotFoundException.class,
                () -> seatService.getSeatById(999)
        );

        verify(seatDAO).getSeatById(999);
    }
    @Test
    void shouldGetAllSeatsSuccessfully() {

        Seat seat1 = new Seat(
                1,
                "A1",
                "REGULAR",
                new BigDecimal("150.00")
        );

        Seat seat2 = new Seat(
                1,
                "A2",
                "PREMIUM",
                new BigDecimal("250.00")
        );

        List<Seat> seats =
                Arrays.asList(seat1, seat2);

        when(seatDAO.getAllSeats())
                .thenReturn(seats);

        List<Seat> result =
                seatService.getAllSeats();

        assertNotNull(result);
        assertEquals(2, result.size());

        verify(seatDAO).getAllSeats();
    }

    @Test
    void shouldUpdateSeatSuccessfully() {

        Seat seat = new Seat(
                1,
                "A1",
                "PREMIUM",
                new BigDecimal("250.00")
        );

        seat.setSeatId(1);

        seatService.updateSeat(seat);

        verify(seatDAO).updateSeat(seat);
    }

    @Test
    void shouldDeleteSeatSuccessfully() {

        seatService.deleteSeat(1);

        verify(seatDAO).deleteSeat(1);
    }
}
