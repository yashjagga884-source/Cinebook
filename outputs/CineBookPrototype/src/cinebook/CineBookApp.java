package cinebook;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** Wires the console, sample catalogue, booking service, and local persistence. */
public final class CineBookApp {
    private final InputReader input;
    private final ConsoleMenu menu;
    private final MovieCatalog movies = new MovieCatalog();
    private final ShowCatalog shows = new ShowCatalog();
    private final BookingService bookings;
    private final SeatMapFormatter seatFormatter = new SeatMapFormatter();
    private final BookingReportService reportService = new BookingReportService();

    public CineBookApp(InputReader input) throws IOException {
        this.input = input;
        this.menu = new ConsoleMenu(input);
        loadSampleCatalogue();
        String configuredPath = System.getenv("CINEBOOK_BOOKINGS_FILE");
        Path bookingPath = configuredPath == null || configuredPath.isBlank()
                ? Path.of("data", "bookings.txt") : Path.of(configuredPath);
        BookingRepository repository = new LocalBookingRepository(bookingPath);
        this.bookings = new BookingService(shows, repository, new BookingValidator(), new BookingReferenceGenerator());
    }

    public void run() {
        boolean running = true;
        while (running) {
            switch (menu.show()) {
                case 1 -> showMovies();
                case 2 -> searchMovies();
                case 3 -> showShows();
                case 4 -> showSeatMap();
                case 5 -> bookSeats();
                case 6 -> showBookings();
                case 7 -> cancelBooking();
                case 8 -> showReport();
                case 9 -> { running = false; System.out.println("Goodbye."); }
                default -> System.out.println("Choose a number from 1 to 9.");
            }
        }
    }

    private void loadSampleCatalogue() {
        Movie interstellar = new Movie("MOV001", "Interstellar", "Sci-Fi", "English", 169, 8.8);
        Movie darkKnight = new Movie("MOV002", "The Dark Knight", "Action", "English", 152, 9.0);
        Movie inception = new Movie("MOV003", "Inception", "Sci-Fi", "English", 148, 8.8);
        Movie avengers = new Movie("MOV004", "Avengers", "Action", "English", 143, 8.0);
        Movie spiderMan = new Movie("MOV005", "Spider-Man", "Action", "English", 121, 7.5);
        for (Movie movie : Arrays.asList(interstellar, darkKnight, inception, avengers, spiderMan)) movies.add(movie);
        shows.add(new Show("SH001", interstellar, "PVR", "Mumbai", "Screen 1", "28-09-2026", "07:30 PM", createSeats(5, 8)));
        shows.add(new Show("SH002", darkKnight, "PVR", "Mumbai", "Screen 2", "28-09-2026", "05:00 PM", createSeats(5, 6)));
        shows.add(new Show("SH003", inception, "INOX", "Delhi", "Screen 1", "29-09-2026", "03:00 PM", createSeats(5, 8)));
        shows.add(new Show("SH004", avengers, "INOX", "Delhi", "Screen 2", "29-09-2026", "07:30 PM", createSeats(5, 6)));
        shows.add(new Show("SH005", spiderMan, "PVR", "Mumbai", "Screen 1", "30-09-2026", "06:00 PM", createSeats(5, 8)));
    }

    private List<Seat> createSeats(int rows, int columns) {
        List<Seat> result = new ArrayList<>();
        String[] types = {"Silver", "Gold", "Platinum", "Recliner"};
        double[] prices = {150, 200, 250, 350};
        for (int row = 0; row < rows; row++) {
            for (int col = 1; col <= columns; col++) {
                int tier = Math.min(row, types.length - 1);
                result.add(new Seat((char) ('A' + row) + Integer.toString(col), types[tier], prices[tier]));
            }
        }
        return result;
    }

    private void showMovies() {
        System.out.println("\nAVAILABLE MOVIES");
        for (Movie movie : movies.getAll()) System.out.println(movie);
    }

    private void searchMovies() {
        String query = input.readLine("Movie title or genre: ");
        List<Movie> found = movies.search(query);
        if (found.isEmpty()) System.out.println("No matching movies.");
        else found.forEach(System.out::println);
    }

    private void showShows() {
        System.out.println("\nAVAILABLE SHOWS");
        for (Show show : shows.getAll()) System.out.printf("%s | %s | %s, %s | %s | %s %s | %d seats available%n",
                show.getShowId(), show.getMovie().getTitle(), show.getCinema(), show.getLocation(),
                show.getScreen(), show.getDate(), show.getTime(), show.availableSeats());
    }

    private void showSeatMap() {
        Show show = findShow(input.readLine("Show ID: "));
        if (show == null) { System.out.println("Show not found."); return; }
        System.out.println(seatFormatter.format(show));
    }

    private void bookSeats() {
        showShows();
        String showId = input.readLine("Select show ID: ");
        String customer = input.readLine("Customer name: ");
        String email = input.readLine("Customer email: ");
        String[] requested = input.readLine("Seat numbers (comma-separated): ").split(",");
        try {
            Booking booking = bookings.book(showId, customer, email, Arrays.asList(requested));
            System.out.println("\nBOOKING CONFIRMED");
            System.out.println(booking);
            System.out.println("File save/reload verified.");
        } catch (IllegalArgumentException | IOException ex) {
            System.out.println("Booking failed: " + ex.getMessage());
        }
    }

    private void showBookings() {
        List<Booking> all = bookings.getAllBookings();
        if (all.isEmpty()) System.out.println("No bookings found.");
        else all.forEach(System.out::println);
    }

    private void cancelBooking() {
        String id = input.readLine("Booking reference to cancel: ");
        try {
            if (bookings.cancel(id)) System.out.println("Booking cancelled; its seats are available again.");
            else System.out.println("Active booking not found.");
        } catch (IOException ex) {
            System.out.println("Cancellation could not be saved: " + ex.getMessage());
        }
    }

    private void showReport() {
        reportService.createMovieBookingReport(bookings.getAllBookings()).forEach(System.out::println);
    }

    private Show findShow(String showId) { return shows.find(showId); }
}
