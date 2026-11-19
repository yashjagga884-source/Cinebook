package cinebook;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/** Produces a small Stream-based report of active bookings by movie. */
public final class BookingReportService {
    public List<String> createMovieBookingReport(List<Booking> bookings) {
        Map<String, Long> counts = bookings.stream().filter(b -> !b.isCancelled())
                .collect(Collectors.groupingBy(Booking::getMovieTitle, TreeMap::new, Collectors.counting()));
        if (counts.isEmpty()) return List.of("No active bookings to report.");
        return counts.entrySet().stream().map(entry -> entry.getKey() + ": " + entry.getValue() + " booking(s)").toList();
    }
}
