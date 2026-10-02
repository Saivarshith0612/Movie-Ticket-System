package com.movieticket.model;

public class BookedSeat {

    private int bookedSeatId;
    private int seatId;
    private int bookingId;

    public BookedSeat() {
    }

    public BookedSeat(int seatId, int bookingId) {
        this.seatId = seatId;
        this.bookingId = bookingId;
    }

    public int getBookedSeatId() {
        return bookedSeatId;
    }

    public void setBookedSeatId(int bookedSeatId) {
        this.bookedSeatId = bookedSeatId;
    }

    public int getSeatId() {
        return seatId;
    }

    public void setSeatId(int seatId) {
        this.seatId = seatId;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }
}