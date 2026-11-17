package cinebook;

import java.util.List;

/** Encapsulates seat availability and all-or-nothing reservations for one show. */
public final class SeatInventory {
    private final Show show;
    public SeatInventory(Show show) { this.show = show; }
    public int availableCount() { return show.availableSeats(); }
    public Seat find(String seatNumber) { return show.findSeat(seatNumber); }
    public synchronized void reserve(List<Seat> seats) {
        for (Seat seat : seats) if (seat.isBooked()) throw new IllegalStateException("Seat " + seat.getSeatNumber() + " is already booked.");
        for (Seat seat : seats) seat.reserve();
    }
    public synchronized void release(List<String> seatNumbers) {
        for (String number : seatNumbers) {
            Seat seat = show.findSeat(number);
            if (seat != null) seat.release();
        }
    }
}
