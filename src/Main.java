import model.*;
import manager.BookingManager;
import util.FileBookingRepository;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final List<Movie> movies = new ArrayList<>();
    private static final List<Cinema> cinemas = new ArrayList<>();
    private static final List<Screen> screens = new ArrayList<>();
    private static final List<Show> shows = new ArrayList<>();
    private static final Map<String, Customer> customersByEmail = new LinkedHashMap<>();
    private static BookingManager bookingManager;
    private static int nextCustomerId = 1;

    public static void main(String[] args) {
        bookingManager = createBookingManager();
        initializeSampleData();
        while (true) {
            System.out.println("\n========== CINEBOOK ==========");
            System.out.println("1. Register\n2. Login\n3. Exit");
            String choice = prompt("Choose an option: ");
            switch (choice) {
                case "1": register(); break;
                case "2": login(); break;
                case "3": System.out.println("Goodbye!"); return;
                default: System.out.println("Invalid option.");
            }
        }
    }

    private static BookingManager createBookingManager() {
        try {
            String filePath = System.getenv().getOrDefault("CINEBOOK_BOOKINGS_FILE", "data/bookings.csv");
            FileBookingRepository repository = new FileBookingRepository(Paths.get(filePath));
            System.out.println("Booking file: " + Paths.get(filePath).toAbsolutePath().normalize());
            return new BookingManager(repository);
        } catch (IOException ex) {
            System.out.println("Booking file unavailable; this run will keep bookings in memory only. Details: " + ex.getMessage());
            return new BookingManager();
        }
    }

    private static void initializeSampleData() {
        Cinema c1 = new Cinema("CIN001", "PVR", "Mumbai");
        Cinema c2 = new Cinema("CIN002", "INOX", "Delhi");
        cinemas.add(c1); cinemas.add(c2);
        Screen s1 = new Screen("SCR001", 40, "5x8");
        Screen s2 = new Screen("SCR002", 30, "5x6");
        screens.add(s1); screens.add(s2);
        Movie m1 = new Movie("MOV001", "Interstellar", "Sci-Fi", "English", 169, 8.8);
        Movie m2 = new Movie("MOV002", "The Dark Knight", "Action", "English", 152, 9.0);
        Movie m3 = new Movie("MOV003", "Inception", "Sci-Fi", "English", 148, 8.8);
        Movie m4 = new Movie("MOV004", "Avengers", "Action", "English", 143, 8.0);
        Movie m5 = new Movie("MOV005", "Spider-Man", "Action", "English", 121, 7.5);
        movies.addAll(Arrays.asList(m1, m2, m3, m4, m5));
        shows.add(new Show("SH001", m1, c1, s1, "28-09-2026", "07:30 PM", createSeatsForScreen(s1)));
        shows.add(new Show("SH002", m2, c1, s2, "28-09-2026", "05:00 PM", createSeatsForScreen(s2)));
        shows.add(new Show("SH003", m3, c2, s1, "29-09-2026", "03:00 PM", createSeatsForScreen(s1)));
        shows.add(new Show("SH004", m4, c2, s2, "29-09-2026", "07:30 PM", createSeatsForScreen(s2)));
        shows.add(new Show("SH005", m5, c1, s1, "30-09-2026", "06:00 PM", createSeatsForScreen(s1)));
    }

    private static List<Seat> createSeatsForScreen(Screen screen) {
        List<Seat> seats = new ArrayList<>();
        String[] dimensions = screen.getSeatLayout().split("x");
        int rows = Integer.parseInt(dimensions[0]), columns = Integer.parseInt(dimensions[1]);
        int count = Math.min(screen.getTotalSeats(), rows * columns);
        for (int i = 0; i < count; i++) {
            int row = i / columns, col = i % columns + 1;
            SeatType type = SeatType.values()[Math.min(row, SeatType.values().length - 1)];
            seats.add(new Seat(String.valueOf((char) ('A' + row)) + col, type));
        }
        return seats;
    }

    private static void register() {
        System.out.println("\n========== CUSTOMER REGISTRATION ==========");
        String name = prompt("Name: ").trim();
        String email = prompt("Email: ").trim().toLowerCase(Locale.ROOT);
        String password = prompt("Password: ");
        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) { System.out.println("All fields are required."); return; }
        if (!email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) { System.out.println("Enter a valid email address."); return; }
        if (customersByEmail.containsKey(email)) { System.out.println("An account with that email already exists."); return; }
        Customer customer = new Customer(String.format("CUS%03d", nextCustomerId++), name, email, password);
        customersByEmail.put(email, customer);
        System.out.println("Registration successful. You can now log in.");
    }

    private static void login() {
        String email = prompt("Email: ").trim().toLowerCase(Locale.ROOT);
        String password = prompt("Password: ");
        Customer customer = customersByEmail.get(email);
        if (customer == null || !customer.authenticate(password)) { System.out.println("Invalid email or password."); return; }
        System.out.println("Welcome, " + customer.getName() + "!");
        customerMenu(customer);
    }

    private static void customerMenu(Customer customer) {
        while (true) {
            System.out.println("\n========== CUSTOMER MENU ==========");
            System.out.println("1. Browse Movies\n2. Search Movies\n3. View Shows\n4. Book Tickets\n5. My Bookings\n6. Cancel Booking\n7. Logout");
            switch (prompt("Choose an option: ")) {
                case "1": displayAllMovies(); break;
                case "2": searchMovies(); break;
                case "3": displayShows(shows); break;
                case "4": bookTickets(customer); break;
                case "5": showBookings(customer); break;
                case "6": cancelBooking(customer); break;
                case "7": System.out.println("Logged out."); return;
                default: System.out.println("Invalid option.");
            }
        }
    }

    private static void displayAllMovies() {
        System.out.println("\n========== AVAILABLE MOVIES ==========");
        for (Movie m : movies) printMovie(m);
        if (movies.isEmpty()) System.out.println("No movies available.");
    }

    private static void printMovie(Movie m) {
        System.out.printf("[%s] %s (%s) | Genre: %s | Duration: %d min | Rating: %.1f%n",
                m.getMovieId(), m.getName(), m.getLanguage(), m.getGenre(), m.getDuration(), m.getRating());
    }

    private static void searchMovies() {
        String query = prompt("Enter movie name or genre: ").trim().toLowerCase(Locale.ROOT);
        if (query.isEmpty()) { System.out.println("Enter a movie name or genre to search."); return; }
        boolean found = false;
        for (Movie m : movies) if (m.getName().toLowerCase(Locale.ROOT).contains(query) || m.getGenre().toLowerCase(Locale.ROOT).contains(query)) { printMovie(m); found = true; }
        if (!found) System.out.println("No movies found matching: " + query);
    }

    private static void displayShows(List<Show> items) {
        System.out.println("\n========== AVAILABLE SHOWS ==========");
        if (items.isEmpty()) { System.out.println("No shows available."); return; }
        for (Show s : items) {
            long available = s.getSeats().stream().filter(seat -> !seat.isBooked()).count();
            System.out.printf("[%s] %s | Cinema: %s (%s) | Screen: %s | Date: %s | Time: %s | Available: %d/%d%n",
                    s.getShowId(), s.getMovie().getName(), s.getCinema().getName(), s.getCinema().getLocation(),
                    s.getScreen().getScreenId(), s.getDate(), s.getTime(), available, s.getSeats().size());
        }
    }

    private static void bookTickets(Customer customer) {
        displayAllMovies();
        String movieId = prompt("Select movie ID: ").trim();
        Movie movie = movies.stream().filter(m -> m.getMovieId().equalsIgnoreCase(movieId)).findFirst().orElse(null);
        if (movie == null) { System.out.println("Movie not found."); return; }
        List<Show> movieShows = new ArrayList<>();
        for (Show s : shows) if (s.getMovie().getMovieId().equals(movie.getMovieId())) movieShows.add(s);
        Set<String> cinemaIds = new LinkedHashSet<>();
        for (Show s : movieShows) cinemaIds.add(s.getCinema().getCinemaId());
        System.out.println("\nCinemas showing " + movie.getName() + ":");
        for (Show s : movieShows) if (cinemaIds.remove(s.getCinema().getCinemaId()))
            System.out.printf("[%s] %s (%s)%n", s.getCinema().getCinemaId(), s.getCinema().getName(), s.getCinema().getLocation());
        String cinemaId = prompt("Select cinema ID: ").trim();
        List<Show> cinemaShows = new ArrayList<>();
        for (Show s : movieShows) if (s.getCinema().getCinemaId().equalsIgnoreCase(cinemaId)) cinemaShows.add(s);
        if (cinemaShows.isEmpty()) { System.out.println("No shows for that cinema and movie."); return; }
        displayShows(cinemaShows);
        String showId = prompt("Select show ID: ").trim();
        Show show = null;
        for (Show s : cinemaShows) if (s.getShowId().equalsIgnoreCase(showId)) { show = s; break; }
        if (show == null) { System.out.println("Show not found."); return; }
        displaySeats(show);
        String input = prompt("Enter seat numbers separated by commas (for example A1,A2): ");
        List<String> seatNumbers = Arrays.asList(input.split(","));
        try {
            Booking booking = bookingManager.book(customer, show, seatNumbers);
            printBooking(booking);
            if (bookingManager.usesFilePersistence()) System.out.println("CSV save and reload verified.");
        } catch (IllegalArgumentException ex) { System.out.println("Booking failed: " + ex.getMessage()); }
    }

    private static void displaySeats(Show show) {
        System.out.println("Seats for " + show.getShowId() + " (O = available, X = booked):");
        int columns = Integer.parseInt(show.getScreen().getSeatLayout().split("x")[1]);
        for (int i = 0; i < show.getSeats().size(); i++) {
            Seat seat = show.getSeats().get(i);
            System.out.printf("%s%s ", seat.isBooked() ? "X" : "O", seat.getSeatNumber());
            if ((i + 1) % columns == 0 || i == show.getSeats().size() - 1) System.out.println();
        }
    }

    private static void printBooking(Booking b) {
        System.out.println("\n========== BOOKING CONFIRMED ==========");
        System.out.println("Booking ID: " + b.getBookingId());
        System.out.println("Customer: " + b.getCustomer().getName());
        System.out.println("Movie: " + b.getShow().getMovie().getName());
        System.out.println("Cinema: " + b.getShow().getCinema().getName() + " (" + b.getShow().getCinema().getLocation() + ")");
        System.out.println("Date / Time: " + b.getShow().getDate() + " " + b.getShow().getTime());
        System.out.println("Seats:");
        for (Seat seat : b.getSeats()) System.out.printf("  %s - %s - Rs. %.2f%n", seat.getSeatNumber(), seat.getSeatType(), seat.getPrice());
        System.out.printf("Total: Rs. %.2f%nStatus: %s%n", b.getTotalPrice(), b.isCancelled() ? "CANCELLED" : "CONFIRMED");
    }

    private static void showBookings(Customer customer) {
        List<Booking> bookings = bookingManager.getBookings(customer);
        if (bookings.isEmpty()) { System.out.println("You have no bookings."); return; }
        for (Booking booking : bookings) printBooking(booking);
    }

    private static void cancelBooking(Customer customer) {
        List<Booking> active = new ArrayList<>();
        for (Booking b : bookingManager.getBookings(customer)) if (!b.isCancelled()) active.add(b);
        if (active.isEmpty()) { System.out.println("You have no active bookings to cancel."); return; }
        for (Booking b : active) System.out.printf("%s | %s | %s | Seats: %s | Rs. %.2f%n", b.getBookingId(), b.getShow().getMovie().getName(), b.getShow().getDate(), seatList(b), b.getTotalPrice());
        String id = prompt("Enter booking ID to cancel (or press Enter to go back): ").trim();
        if (id.isEmpty()) return;
        if (bookingManager.cancel(customer, id)) System.out.println("Booking cancelled. Its seats are available again.");
        else System.out.println("Active booking not found.");
    }

    private static String seatList(Booking b) {
        List<String> names = new ArrayList<>();
        for (Seat seat : b.getSeats()) names.add(seat.getSeatNumber());
        return String.join(", ", names);
    }

    private static String prompt(String message) { System.out.print(message); return scanner.nextLine().trim(); }
}
