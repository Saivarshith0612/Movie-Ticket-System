package com.movieticket.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Booking {

    private int bookingId;
    private int showId;
    private int userId;
    private LocalDateTime bookingDate;
    private BigDecimal totalAmount;
    private String bookingStatus;

    public Booking() {
    }

    public Booking(int showId, int userId,
                   LocalDateTime bookingDate,
                   BigDecimal totalAmount,
                   String bookingStatus) {
        this.showId = showId;
        this.userId = userId;
        this.bookingDate = bookingDate;
        this.totalAmount = totalAmount;
        this.bookingStatus = bookingStatus;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public int getShowId() {
        return showId;
    }

    public void setShowId(int showId) {
        this.showId = showId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public LocalDateTime getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDateTime bookingDate) {
        this.bookingDate = bookingDate;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }
}
