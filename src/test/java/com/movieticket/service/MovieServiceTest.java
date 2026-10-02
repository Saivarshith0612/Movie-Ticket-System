package com.movieticket.service;

import com.movieticket.dao.MovieDAO;
import com.movieticket.model.Movie;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import com.movieticket.exception.ResourceNotFoundException;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MovieServiceTest {

    @Mock
    private MovieDAO movieDAO;

    @InjectMocks
    private MovieService movieService;

    @Test
    void shouldAddMovieSuccessfully() {

        Movie movie = new Movie(
                "Avatar",
                "English",
                "Science Fiction",
                162,
                LocalDate.of(2009, 12, 18)
        );

        movieService.addMovie(movie);

        verify(movieDAO).addMovie(movie);
    }

    @Test
    void shouldGetMovieByIdSuccessfully() {

        Movie movie = new Movie(
                "Avatar",
                "English",
                "Science Fiction",
                162,
                LocalDate.of(2009, 12, 18)
        );

        movie.setMovieId(1);

        when(movieDAO.getMovieById(1)).thenReturn(movie);

        Movie result = movieService.getMovieById(1);

        assertNotNull(result);
        assertEquals(1, result.getMovieId());
        assertEquals("Avatar", result.getTitle());
        assertEquals("English", result.getLanguage());

        verify(movieDAO).getMovieById(1);
    }

    @Test
    void shouldThrowExceptionWhenMovieNotFound() {

        when(movieDAO.getMovieById(999)).thenReturn(null);

        assertThrows(
                ResourceNotFoundException.class,
                () -> movieService.getMovieById(999)
        );

        verify(movieDAO).getMovieById(999);
    }

    @Test
    void shouldGetAllMoviesSuccessfully() {

        Movie movie1 = new Movie(
                "Avatar",
                "English",
                "Science Fiction",
                162,
                LocalDate.of(2009, 12, 18)
        );

        Movie movie2 = new Movie(
                "Inception",
                "English",
                "Science Fiction",
                148,
                LocalDate.of(2010, 7, 16)
        );

        List<Movie> movies = Arrays.asList(movie1, movie2);

        when(movieDAO.getAllMovies()).thenReturn(movies);

        List<Movie> result = movieService.getAllMovies();

        assertNotNull(result);
        assertEquals(2, result.size());

        verify(movieDAO).getAllMovies();
    }

    @Test
    void shouldUpdateMovieSuccessfully() {

        Movie movie = new Movie(
                "Avatar Updated",
                "English",
                "Science Fiction",
                170,
                LocalDate.of(2009, 12, 18)
        );

        movie.setMovieId(1);

        movieService.updateMovie(movie);

        verify(movieDAO).updateMovie(movie);
    }

    @Test
    void shouldDeleteMovieSuccessfully() {

        movieService.deleteMovie(1);

        verify(movieDAO).deleteMovie(1);
    }
}
