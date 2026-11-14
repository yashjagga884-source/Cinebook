package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Customer extends User {
    private final List<Booking> bookings = new ArrayList<>();

    public Customer(String userId, String name, String email, String password) {
        super(userId, name, email, password);
    }

    public List<Booking> getBookings() { return Collections.unmodifiableList(bookings); }
    public void addBooking(Booking booking) { bookings.add(booking); }
}
