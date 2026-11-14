package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Booking {
    private final String bookingId;
    private final Customer customer;
    private final Show show;
    private final List<Seat> seats;
    private final double totalPrice;
    private final LocalDateTime bookedAt;
    private boolean cancelled;

    public Booking(String bookingId, Customer customer, Show show, List<Seat> seats) {
        this.bookingId = bookingId;
        this.customer = customer;
        this.show = show;
        this.seats = new ArrayList<>(seats);
        this.totalPrice = seats.stream().mapToDouble(Seat::getPrice).sum();
        this.bookedAt = LocalDateTime.now();
    }

    public String getBookingId() { return bookingId; }
    public Customer getCustomer() { return customer; }
    public Show getShow() { return show; }
    public List<Seat> getSeats() { return Collections.unmodifiableList(seats); }
    public double getTotalPrice() { return totalPrice; }
    public LocalDateTime getBookedAt() { return bookedAt; }
    public boolean isCancelled() { return cancelled; }
    public void cancel() { cancelled = true; }
}
