package manager;

import model.*;
import java.util.*;
import java.io.IOException;
import util.FileBookingRepository;

public class BookingManager {
    private final List<Booking> bookings = new ArrayList<>();
    private final FileBookingRepository repository;

    public BookingManager() { this(null); }

    public BookingManager(FileBookingRepository repository) { this.repository = repository; }

    public synchronized Booking book(Customer customer, Show show, List<String> seatNumbers) {
        if (seatNumbers == null || seatNumbers.isEmpty()) throw new IllegalArgumentException("Select at least one seat.");
        Set<String> requested = new LinkedHashSet<>();
        for (String number : seatNumbers) {
            String normalized = number.trim().toUpperCase(Locale.ROOT);
            if (normalized.isEmpty() || !requested.add(normalized)) throw new IllegalArgumentException("Seat selection is empty or contains duplicates.");
        }
        List<Seat> selected = new ArrayList<>();
        for (String number : requested) {
            Seat seat = show.getSeats().stream().filter(s -> s.getSeatNumber().equalsIgnoreCase(number)).findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Seat " + number + " does not exist."));
            if (seat.isBooked()) throw new IllegalArgumentException("Seat " + number + " is already booked.");
            selected.add(seat);
        }
        selected.forEach(seat -> seat.setBooked(true));
        Booking booking = new Booking("CB-" + UUID.randomUUID().toString().toUpperCase(Locale.ROOT), customer, show, selected);
        if (repository != null) {
            try {
                repository.save(booking);
                FileBookingRepository.PersistedBooking restored = repository.findById(booking.getBookingId());
                if (restored == null || !restored.bookingId.equals(booking.getBookingId()) ||
                        restored.seats.size() != booking.getSeats().size() ||
                        Math.abs(restored.totalPrice - booking.getTotalPrice()) > 0.001) {
                    throw new IOException("Saved booking did not match its file reload.");
                }
            } catch (IOException ex) {
                selected.forEach(seat -> seat.setBooked(false));
                throw new IllegalArgumentException("Could not save and reload booking: " + ex.getMessage(), ex);
            }
        }
        bookings.add(booking);
        customer.addBooking(booking);
        return booking;
    }

    public synchronized boolean cancel(Customer customer, String bookingId) {
        Optional<Booking> match = customer.getBookings().stream()
                .filter(b -> b.getBookingId().equalsIgnoreCase(bookingId) && !b.isCancelled()).findFirst();
        if (!match.isPresent()) return false;
        Booking booking = match.get();
        booking.getSeats().forEach(seat -> seat.setBooked(false));
        booking.cancel();
        return true;
    }

    public synchronized List<Booking> getBookings(Customer customer) {
        return Collections.unmodifiableList(new ArrayList<>(customer.getBookings()));
    }

    public boolean usesFilePersistence() { return repository != null; }
}
