package com.movieticket.service;

import com.movieticket.dao.TheatreDAO;
import com.movieticket.model.Theatre;
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
class TheatreServiceTest {

    @Mock
    private TheatreDAO theatreDAO;

    @InjectMocks
    private TheatreService theatreService;

    @Test
    void shouldAddTheatreSuccessfully() {

        Theatre theatre = new Theatre(
                "PVR Cinemas",
                "Hyderabad",
                "Banjara Hills",
                200
        );

        theatreService.addTheatre(theatre);

        verify(theatreDAO).addTheatre(theatre);
    }

    @Test
    void shouldGetTheatreByIdSuccessfully() {

        Theatre theatre = new Theatre(
                "PVR Cinemas",
                "Hyderabad",
                "Banjara Hills",
                200
        );

        theatre.setTheatreId(1);

        when(theatreDAO.getTheatreById(1))
                .thenReturn(theatre);

        Theatre result =
                theatreService.getTheatreById(1);

        assertNotNull(result);
        assertEquals(1, result.getTheatreId());
        assertEquals("PVR Cinemas", result.getName());
        assertEquals("Hyderabad", result.getCity());

        verify(theatreDAO).getTheatreById(1);
    }

    @Test
    void shouldThrowExceptionWhenTheatreDoesNotExist() {

        when(theatreDAO.getTheatreById(999))
                .thenReturn(null);

        assertThrows(
                ResourceNotFoundException.class,
                () -> theatreService.getTheatreById(999)
        );

        verify(theatreDAO).getTheatreById(999);
    }

    @Test
    void shouldGetAllTheatresSuccessfully() {

        Theatre theatre1 = new Theatre(
                "PVR Cinemas",
                "Hyderabad",
                "Banjara Hills",
                200
        );

        Theatre theatre2 = new Theatre(
                "INOX",
                "Hyderabad",
                "Gachibowli",
                150
        );

        List<Theatre> theatres =
                Arrays.asList(theatre1, theatre2);

        when(theatreDAO.getAllTheatres())
                .thenReturn(theatres);

        List<Theatre> result =
                theatreService.getAllTheatres();

        assertNotNull(result);
        assertEquals(2, result.size());

        verify(theatreDAO).getAllTheatres();
    }

    @Test
    void shouldUpdateTheatreSuccessfully() {

        Theatre theatre = new Theatre(
                "PVR Cinemas Updated",
                "Hyderabad",
                "Banjara Hills",
                250
        );

        theatre.setTheatreId(1);

        theatreService.updateTheatre(theatre);

        verify(theatreDAO).updateTheatre(theatre);
    }

    @Test
    void shouldDeleteTheatreSuccessfully() {

        theatreService.deleteTheatre(1);

        verify(theatreDAO).deleteTheatre(1);
    }
}
