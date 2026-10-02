package com.movieticket.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Show {

    private int showId;
    private int theatreId;
    private int movieId;
    private LocalDate showDate;
    private LocalTime startTime;
    private LocalTime endTime;

    public Show() {
    }

    public Show(int theatreId, int movieId,
                LocalDate showDate,
                LocalTime startTime,
                LocalTime endTime) {
        this.theatreId = theatreId;
        this.movieId = movieId;
        this.showDate = showDate;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getShowId() {
        return showId;
    }

    public void setShowId(int showId) {
        this.showId = showId;
    }

    public int getTheatreId() {
        return theatreId;
    }

    public void setTheatreId(int theatreId) {
        this.theatreId = theatreId;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public LocalDate getShowDate() {
        return showDate;
    }

    public void setShowDate(LocalDate showDate) {
        this.showDate = showDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }
}
