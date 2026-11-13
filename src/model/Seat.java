package model;

public class Seat {
    private String seatNumber;
    private SeatType seatType;
    private boolean isBooked;
    private double price;

    public Seat() {
    }

    public Seat(String seatNumber, SeatType seatType, boolean isBooked, double price) {
        this.seatNumber = seatNumber;
        this.seatType = seatType;
        this.isBooked = isBooked;
        this.price = price;
    }

    public Seat(String seatNumber, SeatType seatType) {
        this.seatNumber = seatNumber;
        this.seatType = seatType;
        this.isBooked = false;
        this.price = seatType.getPrice();
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public SeatType getSeatType() {
        return seatType;
    }

    public void setSeatType(SeatType seatType) {
        this.seatType = seatType;
        this.price = seatType.getPrice();
    }

    public boolean isBooked() {
        return isBooked;
    }

    public void setBooked(boolean booked) {
        isBooked = booked;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Seat{" +
                "seatNumber='" + seatNumber + '\'' +
                ", seatType=" + seatType +
                ", isBooked=" + isBooked +
                ", price=" + price +
                '}';
    }
}
