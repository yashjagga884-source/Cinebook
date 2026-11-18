package cinebook;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

/** Low-level UTF-8 text file operations; complete replacement is staged before the move. */
public final class BookingFileStore {
    public static final String HEADER = "booking_id,show_id,movie_title,cinema,show_date,show_time,customer_name,customer_email,seats,total_price,booked_at,cancelled";
    private final Path file;

    public BookingFileStore(Path file) { this.file = file.toAbsolutePath().normalize(); }

    public synchronized List<String> readRecords() throws IOException {
        if (Files.notExists(file)) return List.of();
        List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
        if (lines.isEmpty()) return List.of();
        if (!HEADER.equals(lines.get(0))) throw new IOException("Booking file header is invalid: " + file);
        return new ArrayList<>(lines.subList(1, lines.size()));
    }

    public synchronized void replaceRecords(List<String> records) throws IOException {
        Path parent = file.getParent();
        Files.createDirectories(parent);
        List<String> lines = new ArrayList<>();
        lines.add(HEADER);
        lines.addAll(records);
        Path temporary = Files.createTempFile(parent, "cinebook-bookings-", ".tmp");
        try {
            Files.write(temporary, lines, StandardCharsets.UTF_8);
            try { Files.move(temporary, file, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING); }
            catch (AtomicMoveNotSupportedException ex) { Files.move(temporary, file, StandardCopyOption.REPLACE_EXISTING); }
        } finally {
            Files.deleteIfExists(temporary);
        }
    }
    public Path getFile() { return file; }
}
