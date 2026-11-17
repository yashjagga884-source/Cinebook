package model;

import java.util.List;
import java.util.ArrayList;

public class Show {
    private String showId;
    private Movie movie;
    private Cinema cinema;
    private Screen screen;
    private String date;
    private String time;
    private List<Seat> seats;

    public Show() {
        this.seats = new ArrayList<>();
    }

    public Show(String showId, Movie movie, Cinema cinema, Screen screen, String date, String time, List<Seat> seats) {
        this.showId = showId;
        this.movie = movie;
        this.cinema = cinema;
        this.screen = screen;
        this.date = date;
        this.time = time;
        this.seats = seats != null ? seats : new ArrayList<>();
    }

    public String getShowId() {
        return showId;
    }

    public void setShowId(String showId) {
        this.showId = showId;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public Cinema getCinema() {
        return cinema;
    }

    public void setCinema(Cinema cinema) {
        this.cinema = cinema;
    }

    public Screen getScreen() {
        return screen;
    }

    public void setScreen(Screen screen) {
        this.screen = screen;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }

    @Override
    public String toString() {
        return "Show{" +
                "showId='" + showId + '\'' +
                ", movie=" + movie +
                ", cinema=" + cinema +
                ", screen=" + screen +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", seats=" + seats +
                '}';
    }
}
