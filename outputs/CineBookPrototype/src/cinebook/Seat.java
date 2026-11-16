package cinebook;

/** Seat identity, tier and live booking state for one show. */
public final class Seat {
    private final String seatNumber, type;
    private final double price;
    private boolean booked;

    public Seat(String seatNumber, String type, double price) {
        if (seatNumber == null || seatNumber.isBlank() || type == null || type.isBlank() || price < 0) throw new IllegalArgumentException("Invalid seat details.");
        this.seatNumber = seatNumber.toUpperCase(); this.type = type; this.price = price;
    }
    public String getSeatNumber() { return seatNumber; }
    public String getType() { return type; }
    public double getPrice() { return price; }
    public boolean isBooked() { return booked; }
    public void reserve() { if (booked) throw new IllegalStateException("Seat " + seatNumber + " is already booked."); booked = true; }
    public void release() { booked = false; }
}
