package cinebook;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Persistable booking snapshot. */
public final class Booking {
    private final String bookingId, showId, movieTitle, cinema, showDate, showTime, customerName, customerEmail, bookedAt;
    private final List<String> seatNumbers;
    private final double totalPrice;
    private boolean cancelled;

    public Booking(String bookingId, String showId, String movieTitle, String cinema, String showDate, String showTime,
                   String customerName, String customerEmail, List<String> seatNumbers, double totalPrice,
                   String bookedAt, boolean cancelled) {
        if (bookingId == null || bookingId.isBlank() || showId == null || showId.isBlank() ||
                movieTitle == null || movieTitle.isBlank() || seatNumbers == null || seatNumbers.isEmpty() ||
                !Double.isFinite(totalPrice) || totalPrice < 0) throw new IllegalArgumentException("Invalid booking snapshot.");
        this.bookingId = bookingId; this.showId = showId; this.movieTitle = movieTitle; this.cinema = cinema;
        this.showDate = showDate; this.showTime = showTime; this.customerName = customerName; this.customerEmail = customerEmail;
        this.seatNumbers = new ArrayList<>(seatNumbers); this.totalPrice = totalPrice;
        this.bookedAt = bookedAt == null ? LocalDateTime.now().toString() : bookedAt; this.cancelled = cancelled;
    }
    public String getBookingId() { return bookingId; }
    public String getShowId() { return showId; }
    public String getMovieTitle() { return movieTitle; }
    public String getCinema() { return cinema; }
    public String getShowDate() { return showDate; }
    public String getShowTime() { return showTime; }
    public String getCustomerName() { return customerName; }
    public String getCustomerEmail() { return customerEmail; }
    public List<String> getSeatNumbers() { return Collections.unmodifiableList(seatNumbers); }
    public double getTotalPrice() { return totalPrice; }
    public String getBookedAt() { return bookedAt; }
    public boolean isCancelled() { return cancelled; }
    public void markCancelled() { cancelled = true; }
    public void restoreActive() { cancelled = false; }
    @Override public String toString() { return String.format("%s | %s | %s %s | seats %s | Rs. %.2f | %s", bookingId, movieTitle, showDate, showTime, String.join(",", seatNumbers), totalPrice, cancelled ? "CANCELLED" : "CONFIRMED"); }
}
