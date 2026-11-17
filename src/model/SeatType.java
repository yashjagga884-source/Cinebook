package model;

public enum SeatType {
    SILVER(150),
    GOLD(200),
    PLATINUM(250),
    RECLINER(350);

    private final double price;

    SeatType(double price) {
        this.price = price;
    }

    public double getPrice() {
        return price;
    }
}
