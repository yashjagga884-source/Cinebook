package cinebook;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/** BookingRepository implementation backed by the local UTF-8 text file. */
public final class LocalBookingRepository extends BookingRepository {
    private final BookingFileStore store;
    private final BookingFileCodec codec = new BookingFileCodec();

    public LocalBookingRepository(Path path) { this.store = new BookingFileStore(path); }

    @Override public synchronized List<Booking> loadAll() throws IOException {
        List<String> rows = store.readRecords();
        List<Booking> result = new ArrayList<>();
        for (int i = 0; i < rows.size(); i++) {
            if (rows.get(i).isBlank()) continue;
            result.add(codec.decode(rows.get(i), i + 2));
        }
        return result;
    }

    @Override public synchronized void saveAll(List<Booking> bookings) throws IOException {
        List<String> rows = new ArrayList<>();
        for (Booking booking : bookings) rows.add(codec.encode(booking));
        store.replaceRecords(rows);
    }
}
