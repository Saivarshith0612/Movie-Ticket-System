package com.movieticket.service;

import com.movieticket.dao.ShowDAO;
import com.movieticket.model.Show;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import com.movieticket.exception.ResourceNotFoundException;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ShowServiceTest {

    @Mock
    private ShowDAO showDAO;

    @InjectMocks
    private ShowService showService;

    @Test
    void shouldAddShowSuccessfully() {

        Show show = new Show(
                1,
                1,
                LocalDate.of(2026, 10, 1),
                LocalTime.of(10, 0),
                LocalTime.of(13, 0)
        );

        showService.addShow(show);

        verify(showDAO).addShow(show);
    }

    @Test
    void shouldGetShowByIdSuccessfully() {

        Show show = new Show(
                1,
                1,
                LocalDate.of(2026, 10, 1),
                LocalTime.of(10, 0),
                LocalTime.of(13, 0)
        );

        show.setShowId(1);

        when(showDAO.getShowById(1))
                .thenReturn(show);

        Show result =
                showService.getShowById(1);

        assertNotNull(result);
        assertEquals(1, result.getShowId());
        assertEquals(1, result.getTheatreId());
        assertEquals(1, result.getMovieId());
        assertEquals(
                LocalDate.of(2026, 10, 1),
                result.getShowDate()
        );
        assertEquals(
                LocalTime.of(10, 0),
                result.getStartTime()
        );
        assertEquals(
                LocalTime.of(13, 0),
                result.getEndTime()
        );

        verify(showDAO).getShowById(1);
    }

    @Test
    void shouldThrowExceptionWhenShowDoesNotExist() {

        when(showDAO.getShowById(999))
                .thenReturn(null);

        assertThrows(
                ResourceNotFoundException.class,
                () -> showService.getShowById(999)
        );

        verify(showDAO).getShowById(999);
    }

    @Test
    void shouldGetAllShowsSuccessfully() {

        Show show1 = new Show(
                1,
                1,
                LocalDate.of(2026, 10, 1),
                LocalTime.of(10, 0),
                LocalTime.of(13, 0)
        );

        Show show2 = new Show(
                2,
                2,
                LocalDate.of(2026, 10, 2),
                LocalTime.of(18, 0),
                LocalTime.of(21, 0)
        );

        List<Show> shows =
                Arrays.asList(show1, show2);

        when(showDAO.getAllShows())
                .thenReturn(shows);

        List<Show> result =
                showService.getAllShows();

        assertNotNull(result);
        assertEquals(2, result.size());

        verify(showDAO).getAllShows();
    }

    @Test
    void shouldUpdateShowSuccessfully() {

        Show show = new Show(
                1,
                1,
                LocalDate.of(2026, 10, 1),
                LocalTime.of(11, 0),
                LocalTime.of(14, 0)
        );

        show.setShowId(1);

        showService.updateShow(show);

        verify(showDAO).updateShow(show);
    }

    @Test
    void shouldDeleteShowSuccessfully() {

        showService.deleteShow(1);

        verify(showDAO).deleteShow(1);
    }
}
