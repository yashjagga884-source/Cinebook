package util;

import model.Booking;
import model.Seat;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Plain CSV persistence for the milestone prototype; this class does not use a database. */
public class FileBookingRepository {
    private static final String HEADER = "booking_id,customer_id,customer_name,customer_email,movie_id,movie_name," +
            "cinema_id,cinema_name,show_id,show_date,show_time,total_price,booked_at,cancelled,seats";
    private final Path file;

    public FileBookingRepository(Path file) throws IOException {
        this.file = file.toAbsolutePath().normalize();
        Path parent = this.file.getParent();
        if (parent != null) Files.createDirectories(parent);
        if (Files.notExists(this.file)) Files.writeString(this.file, HEADER + System.lineSeparator(), StandardCharsets.UTF_8);
        else if (Files.size(this.file) == 0) Files.writeString(this.file, HEADER + System.lineSeparator(), StandardCharsets.UTF_8);
    }

    public synchronized void save(Booking booking) throws IOException {
        if (findById(booking.getBookingId()) != null) throw new IOException("Booking ID already exists in the CSV file.");
        String row = toCsvRow(booking);
        String oldContent = Files.readString(file, StandardCharsets.UTF_8);
        String separator = oldContent.endsWith("\n") || oldContent.endsWith("\r") ? "" : System.lineSeparator();
        String newContent = oldContent + separator + row + System.lineSeparator();
        Path parent = file.getParent();
        Path temporaryFile = Files.createTempFile(parent, "bookings-", ".tmp");
        try {
            Files.writeString(temporaryFile, newContent, StandardCharsets.UTF_8);
            try {
                Files.move(temporaryFile, file, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
            } catch (AtomicMoveNotSupportedException ex) {
                Files.move(temporaryFile, file, StandardCopyOption.REPLACE_EXISTING);
            }
        } finally {
            Files.deleteIfExists(temporaryFile);
        }
    }

    public synchronized PersistedBooking findById(String bookingId) throws IOException {
        List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
        for (int lineNumber = 1; lineNumber < lines.size(); lineNumber++) {
            String line = lines.get(lineNumber);
            if (line.isBlank()) continue;
            List<String> fields;
            try {
                fields = parseCsv(line);
            } catch (IllegalArgumentException ex) {
                throw new IOException("Malformed CSV at line " + (lineNumber + 1) + ".", ex);
            }
            if (fields.size() != 15) throw new IOException("Expected 15 CSV fields at line " + (lineNumber + 1) + ".");
            if (fields.get(0).equals(bookingId)) return toPersistedBooking(fields, lineNumber + 1);
        }
        return null;
    }

    private String toCsvRow(Booking booking) {
        List<String> fields = new ArrayList<>();
        fields.add(booking.getBookingId());
        fields.add(booking.getCustomer().getUserId());
        fields.add(booking.getCustomer().getName());
        fields.add(booking.getCustomer().getEmail());
        fields.add(booking.getShow().getMovie().getMovieId());
        fields.add(booking.getShow().getMovie().getName());
        fields.add(booking.getShow().getCinema().getCinemaId());
        fields.add(booking.getShow().getCinema().getName());
        fields.add(booking.getShow().getShowId());
        fields.add(booking.getShow().getDate());
        fields.add(booking.getShow().getTime());
        fields.add(Double.toString(booking.getTotalPrice()));
        fields.add(booking.getBookedAt().toString());
        fields.add(Boolean.toString(booking.isCancelled()));
        List<String> seats = new ArrayList<>();
        for (Seat seat : booking.getSeats()) seats.add(seat.getSeatNumber() + ":" + seat.getSeatType().name() + ":" + seat.getPrice());
        fields.add(String.join(";", seats));
        List<String> escaped = new ArrayList<>();
        for (String field : fields) escaped.add(escapeCsv(field));
        return String.join(",", escaped);
    }

    private PersistedBooking toPersistedBooking(List<String> f, int lineNumber) throws IOException {
        try {
            List<PersistedSeat> seats = new ArrayList<>();
            if (!f.get(14).isEmpty()) {
                for (String entry : f.get(14).split(";")) {
                    String[] seat = entry.split(":", -1);
                    if (seat.length != 3) throw new IllegalArgumentException("Invalid seat encoding.");
                    seats.add(new PersistedSeat(seat[0], seat[1], Double.parseDouble(seat[2])));
                }
            }
            return new PersistedBooking(f.get(0), f.get(1), f.get(2), f.get(3), f.get(4), f.get(5),
                    f.get(6), f.get(7), f.get(8), f.get(9), f.get(10), Double.parseDouble(f.get(11)),
                    f.get(12), Boolean.parseBoolean(f.get(13)), seats);
        } catch (RuntimeException ex) {
            throw new IOException("Invalid booking data at CSV line " + lineNumber + ".", ex);
        }
    }

    private static String escapeCsv(String value) {
        String safe = value == null ? "" : value;
        if (safe.contains(",") || safe.contains("\"") || safe.contains("\n") || safe.contains("\r"))
            return "\"" + safe.replace("\"", "\"\"") + "\"";
        return safe;
    }

    private static List<String> parseCsv(String line) {
        List<String> values = new ArrayList<>();
        StringBuilder value = new StringBuilder();
        boolean quoted = false;
        for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);
            if (quoted) {
                if (ch == '"' && i + 1 < line.length() && line.charAt(i + 1) == '"') { value.append('"'); i++; }
                else if (ch == '"') quoted = false;
                else value.append(ch);
            } else if (ch == ',' ) {
                values.add(value.toString()); value.setLength(0);
            } else if (ch == '"' && value.length() == 0) quoted = true;
            else value.append(ch);
        }
        if (quoted) throw new IllegalArgumentException("Unclosed quoted CSV field.");
        values.add(value.toString());
        return values;
    }

    public static final class PersistedBooking {
        public final String bookingId, customerId, customerName, customerEmail, movieId, movieName;
        public final String cinemaId, cinemaName, showId, showDate, showTime, bookedAt;
        public final double totalPrice;
        public final boolean cancelled;
        public final List<PersistedSeat> seats;

        private PersistedBooking(String bookingId, String customerId, String customerName, String customerEmail,
                String movieId, String movieName, String cinemaId, String cinemaName, String showId,
                String showDate, String showTime, double totalPrice, String bookedAt, boolean cancelled,
                List<PersistedSeat> seats) {
            this.bookingId = bookingId; this.customerId = customerId; this.customerName = customerName;
            this.customerEmail = customerEmail; this.movieId = movieId; this.movieName = movieName;
            this.cinemaId = cinemaId; this.cinemaName = cinemaName; this.showId = showId;
            this.showDate = showDate; this.showTime = showTime; this.totalPrice = totalPrice;
            this.bookedAt = bookedAt; this.cancelled = cancelled;
            this.seats = Collections.unmodifiableList(new ArrayList<>(seats));
        }
    }

    public static final class PersistedSeat {
        public final String seatNumber, seatType;
        public final double price;
        private PersistedSeat(String seatNumber, String seatType, double price) {
            this.seatNumber = seatNumber; this.seatType = seatType; this.price = price;
        }
    }
}
