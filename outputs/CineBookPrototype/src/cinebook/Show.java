package cinebook;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** A scheduled movie performance with its own seat inventory. */
public final class Show {
    private final String showId, cinema, location, screen, date, time;
    private final Movie movie;
    private final List<Seat> seats;

    public Show(String showId, Movie movie, String cinema, String location, String screen, String date, String time, List<Seat> seats) {
        if (showId == null || showId.isBlank() || movie == null) throw new IllegalArgumentException("Show ID and movie are required.");
        this.showId = showId; this.movie = movie; this.cinema = cinema; this.location = location;
        this.screen = screen; this.date = date; this.time = time; this.seats = new ArrayList<>(seats);
    }
    public String getShowId() { return showId; }
    public Movie getMovie() { return movie; }
    public String getCinema() { return cinema; }
    public String getLocation() { return location; }
    public String getScreen() { return screen; }
    public String getDate() { return date; }
    public String getTime() { return time; }
    public List<Seat> getSeats() { return Collections.unmodifiableList(seats); }
    public Seat findSeat(String number) { return seats.stream().filter(s -> s.getSeatNumber().equalsIgnoreCase(number)).findFirst().orElse(null); }
    public int availableSeats() { return (int) seats.stream().filter(s -> !s.isBooked()).count(); }
}
