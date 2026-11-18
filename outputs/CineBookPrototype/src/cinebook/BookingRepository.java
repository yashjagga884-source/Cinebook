package cinebook;

import java.io.IOException;
import java.util.List;

/** Persistence boundary used by booking rules. */
public abstract class BookingRepository {
    public abstract List<Booking> loadAll() throws IOException;
    public abstract void saveAll(List<Booking> bookings) throws IOException;
}
