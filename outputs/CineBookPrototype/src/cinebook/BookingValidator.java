package cinebook;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/** Validates customer details and seat requests before inventory changes. */
public final class BookingValidator {
    public List<Seat> validate(Show show, String customerName, String customerEmail, List<String> requestedSeats) {
        if (show == null) throw new IllegalArgumentException("Show not found.");
        if (customerName == null || customerName.isBlank()) throw new IllegalArgumentException("Customer name is required.");
        if (customerEmail == null || !customerEmail.trim().matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) throw new IllegalArgumentException("Enter a valid customer email.");
        if (requestedSeats == null || requestedSeats.isEmpty()) throw new IllegalArgumentException("Select at least one seat.");
        Set<String> normalized = new LinkedHashSet<>();
        for (String value : requestedSeats) {
            String number = value == null ? "" : value.trim().toUpperCase(Locale.ROOT);
            if (number.isEmpty()) throw new IllegalArgumentException("Seat number cannot be empty.");
            if (!normalized.add(number)) throw new IllegalArgumentException("Seat " + number + " is listed more than once.");
        }
        List<Seat> result = new ArrayList<>();
        for (String number : normalized) {
            Seat seat = show.findSeat(number);
            if (seat == null) throw new IllegalArgumentException("Seat " + number + " does not exist.");
            if (seat.isBooked()) throw new IllegalArgumentException("Seat " + number + " is already booked.");
            result.add(seat);
        }
        return result;
    }
}
