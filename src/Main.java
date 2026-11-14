import model.*;
import java.util.*;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static List<Movie> movies = new ArrayList<>();
    private static List<Cinema> cinemas = new ArrayList<>();
    private static List<Screen> screens = new ArrayList<>();
    private static List<Show> shows = new ArrayList<>();

    public static void main(String[] args) {
        initializeSampleData();

        while (true) {
            System.out.println("\n=== MOVIE TICKET BOOKING SYSTEM - Phase 1 ===");
            System.out.println("1. View Movies");
            System.out.println("2. Search Movie");
            System.out.println("3. View Shows");
            System.out.println("4. View Seats");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1": displayAllMovies(); break;
                case "2": searchMovies(); break;
                case "3": displayShows(); break;
                case "4": viewSeats(); break;
                case "5":
                    System.out.println("Thank you for using Cinebook! Goodbye!");
                    System.exit(0);
                    break;
                default: System.out.println("Invalid option. Try again.");
            }
        }
    }

    private static void initializeSampleData() {
        // Create cinemas
        Cinema c1 = new Cinema("CIN001", "PVR", "Mumbai");
        Cinema c2 = new Cinema("CIN002", "INOX", "Delhi");
        cinemas.add(c1);
        cinemas.add(c2);

        // Create screens
        Screen s1 = new Screen("SCR001", 40, "5x8");
        Screen s2 = new Screen("SCR002", 30, "5x6");
        screens.add(s1);
        screens.add(s2);

        // Create movies
        Movie m1 = new Movie("MOV001", "Interstellar", "Sci-Fi", "English", 169, 8.8);
        Movie m2 = new Movie("MOV002", "The Dark Knight", "Action", "English", 152, 9.0);
        Movie m3 = new Movie("MOV003", "Inception", "Sci-Fi", "English", 148, 8.8);
        Movie m4 = new Movie("MOV004", "Avengers", "Action", "English", 143, 8.0);
        Movie m5 = new Movie("MOV005", "Spider-Man", "Action", "English", 121, 7.5);
        movies.add(m1);
        movies.add(m2);
        movies.add(m3);
        movies.add(m4);
        movies.add(m5);

        // Create shows with sample seats
        // Show 1: Interstellar at PVR Mumbai
        List<Seat> seats1 = createSeatsForScreen(s1);
        shows.add(new Show("SH001", m1, c1, s1, "28-09-2026", "07:30 PM", seats1));

        // Show 2: The Dark Knight at PVR Mumbai
        List<Seat> seats2 = createSeatsForScreen(s2);
        shows.add(new Show("SH002", m2, c1, s2, "28-09-2026", "05:00 PM", seats2));

        // Show 3: Inception at INOX Delhi
        List<Seat> seats3 = createSeatsForScreen(s1);
        shows.add(new Show("SH003", m3, c2, s1, "29-09-2026", "03:00 PM", seats3));

        // Show 4: Avengers at INOX Delhi
        List<Seat> seats4 = createSeatsForScreen(s2);
        shows.add(new Show("SH004", m4, c2, s2, "29-09-2026", "07:30 PM", seats4));

        // Show 5: Spider-Man at PVR Mumbai
        List<Seat> seats5 = createSeatsForScreen(s1);
        shows.add(new Show("SH005", m5, c1, s1, "30-09-2026", "06:00 PM", seats5));
    }

    private static List<Seat> createSeatsForScreen(Screen screen) {
        List<Seat> seats = new ArrayList<>();
        String[] rows = {"A", "B", "C", "D", "E"};
        int colCount = screen.getTotalSeats() / 5;
        if (colCount == 0) colCount = 8;
        SeatType[] types = SeatType.values();
        int idx = 0;
        for (String row : rows) {
            for (int col = 1; col <= colCount && idx < screen.getTotalSeats(); col++) {
                SeatType type = types[idx % types.length];
                seats.add(new Seat(row + col, type, false, type.getPrice()));
                idx++;
            }
        }
        return seats;
    }

    private static void displayAllMovies() {
        System.out.println("\n========== AVAILABLE MOVIES ==========");
        if (movies.isEmpty()) { System.out.println("No movies available."); return; }
        for (Movie m : movies) {
            System.out.printf(" [%s] %s (%s) | Genre: %s | Duration: %d min | Rating: %.1f%n",
                    m.getMovieId(), m.getName(), m.getLanguage(), m.getGenre(), m.getDuration(), m.getRating());
        }
    }

    private static void searchMovies() {
        System.out.print("Enter movie name to search: ");
        String query = scanner.nextLine().trim().toLowerCase();
        System.out.println("\n========== SEARCH RESULTS ==========");
        boolean found = false;
        for (Movie m : movies) {
            if (m.getName().toLowerCase().contains(query) || m.getGenre().toLowerCase().contains(query)) {
                System.out.printf(" [%s] %s (%s) | Genre: %s | Duration: %d min | Rating: %.1f%n",
                        m.getMovieId(), m.getName(), m.getLanguage(), m.getGenre(), m.getDuration(), m.getRating());
                found = true;
            }
        }
        if (!found) System.out.println("No movies found matching: " + query);
    }

    private static void displayShows() {
        System.out.println("\n========== AVAILABLE SHOWS ==========");
        if (shows.isEmpty()) { System.out.println("No shows available."); return; }
        for (Show s : shows) {
            System.out.printf(" [%s] %s at %s | Cinema: %s | Screen: %s | Date: %s | Time: %s | Seats: %d%n",
                    s.getShowId(), s.getMovie().getName(), s.getCinema().getName(),
                    s.getCinema().getName(), s.getScreen().getScreenId(),
                    s.getDate(), s.getTime(), s.getSeats().size());
        }
        System.out.println();
    }

    private static void viewSeats() {
        System.out.println("\n========== VIEW SEATS ==========");
        if (shows.isEmpty()) { System.out.println("No shows available."); return; }
        System.out.print("Enter show ID to view seats: ");
        String showId = scanner.nextLine().trim();
        boolean found = false;
        for (Show s : shows) {
            if (s.getShowId().equals(showId)) {
                found = true;
                List<Seat> seats = s.getSeats();
                System.out.printf("Show: %s - %s%n", s.getMovie().getName(), s.getCinema().getName());
                System.out.println("Seat Layout:");
                for (Seat seat : seats) {
                    String marker = seat.isBooked() ? "X" : "O";
                    System.out.printf("  %s [%s] %s - %s%n", marker, seat.getSeatType(), seat.getSeatNumber(),
                            seat.isBooked() ? "BOOKED" : "Available");
                }
                Screen screen = s.getScreen();
                System.out.printf("Total seats: %d (layout: %s)%n", screen.getTotalSeats(), screen.getSeatLayout());
                break;
            }
        }
        if (!found) System.out.println("Show not found.");
    }
}