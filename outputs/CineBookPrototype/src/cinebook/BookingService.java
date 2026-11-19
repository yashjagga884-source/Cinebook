package cinebook;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Coordinates reservation rules, seat inventory, booking history and file persistence. */
public final class BookingService {
    private final ShowCatalog shows;
    private final BookingRepository repository;
    private final BookingValidator validator;
    private final BookingReferenceGenerator references;
    private final Map<String, SeatInventory> inventories = new LinkedHashMap<>();
    private final List<Booking> bookings = new ArrayList<>();

    public BookingService(ShowCatalog shows, BookingRepository repository, BookingValidator validator,
                          BookingReferenceGenerator references) throws IOException {
        this.shows = shows; this.repository = repository; this.validator = validator; this.references = references;
        for (Show show : shows.getAll()) inventories.put(show.getShowId(), new SeatInventory(show));
        bookings.addAll(repository.loadAll());
        restoreActiveSeats();
    }

    private void restoreActiveSeats() throws IOException {
        for (Booking booking : bookings) {
            if (booking.isCancelled()) continue;
            SeatInventory inventory = inventories.get(booking.getShowId());
            if (inventory == null) throw new IOException("Saved booking references unknown show " + booking.getShowId() + ".");
            List<Seat> seats = new ArrayList<>();
            java.util.Set<String> seen = new java.util.HashSet<>();
            for (String number : booking.getSeatNumbers()) {
                if (!seen.add(number.toUpperCase(java.util.Locale.ROOT))) throw new IOException("Saved booking repeats seat " + number + ".");
                Seat seat = inventory.find(number);
                if (seat == null || seat.isBooked()) throw new IOException("Saved seat is missing or duplicated: " + number);
                seats.add(seat);
            }
            inventory.reserve(seats);
        }
    }

    public synchronized Booking book(String showId, String customerName, String customerEmail,
                                     List<String> requestedSeats) throws IOException {
        Show show = shows.find(showId);
        List<Seat> selected = validator.validate(show, customerName, customerEmail, requestedSeats);
        SeatInventory inventory = inventories.get(show.getShowId());
        inventory.reserve(selected);
        List<String> numbers = selected.stream().map(Seat::getSeatNumber).toList();
        double total = selected.stream().mapToDouble(Seat::getPrice).sum();
        Booking booking = new Booking(references.nextReference(), show.getShowId(), show.getMovie().getTitle(),
                show.getCinema(), show.getDate(), show.getTime(), customerName.trim(), customerEmail.trim(),
                numbers, total, LocalDateTime.now().toString(), false);
        bookings.add(booking);
        try {
            repository.saveAll(bookings);
            Booking reloaded = repository.loadAll().stream()
                    .filter(saved -> saved.getBookingId().equals(booking.getBookingId())).findFirst().orElse(null);
            if (reloaded == null || reloaded.getSeatNumbers().size() != booking.getSeatNumbers().size() ||
                    !reloaded.getSeatNumbers().equals(booking.getSeatNumbers()) ||
                    Math.abs(reloaded.getTotalPrice() - booking.getTotalPrice()) > 0.001)
                throw new IOException("Saved booking did not match its file reload.");
        }
        catch (IOException ex) {
            bookings.remove(booking);
            inventory.release(numbers);
            throw ex;
        }
        return booking;
    }

    public synchronized List<Booking> getAllBookings() { return Collections.unmodifiableList(new ArrayList<>(bookings)); }

    public synchronized boolean cancel(String bookingId) throws IOException {
        Booking booking = bookings.stream().filter(b -> b.getBookingId().equalsIgnoreCase(bookingId) && !b.isCancelled()).findFirst().orElse(null);
        if (booking == null) return false;
        booking.markCancelled();
        try { repository.saveAll(bookings); }
        catch (IOException ex) { booking.restoreActive(); throw ex; }
        inventories.get(booking.getShowId()).release(booking.getSeatNumbers());
        return true;
    }
}
