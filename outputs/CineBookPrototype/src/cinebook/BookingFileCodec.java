package cinebook;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** Converts booking snapshots to and from one escaped CSV record per line. */
public final class BookingFileCodec {
    public String encode(Booking booking) {
        List<String> fields = Arrays.asList(booking.getBookingId(), booking.getShowId(), booking.getMovieTitle(),
                booking.getCinema(), booking.getShowDate(), booking.getShowTime(), booking.getCustomerName(),
                booking.getCustomerEmail(), String.join(";", booking.getSeatNumbers()),
                Double.toString(booking.getTotalPrice()), booking.getBookedAt(), Boolean.toString(booking.isCancelled()));
        return fields.stream().map(this::escape).reduce((left, right) -> left + "," + right).orElse("");
    }

    public Booking decode(String record, int lineNumber) throws IOException {
        List<String> fields;
        try { fields = parse(record); }
        catch (IllegalArgumentException ex) { throw new IOException("Malformed CSV quoting at line " + lineNumber + ".", ex); }
        if (fields.size() != 12) throw new IOException("Expected 12 columns at line " + lineNumber + ".");
        try {
            List<String> seats = fields.get(8).isBlank() ? List.of() : Arrays.asList(fields.get(8).split(";"));
            double price = Double.parseDouble(fields.get(9));
            if (seats.isEmpty() || !Double.isFinite(price) || price < 0 ||
                    !(fields.get(11).equalsIgnoreCase("true") || fields.get(11).equalsIgnoreCase("false")))
                throw new IllegalArgumentException("Booking needs seats, a finite non-negative price, and a boolean cancellation flag.");
            return new Booking(fields.get(0), fields.get(1), fields.get(2), fields.get(3), fields.get(4), fields.get(5),
                    fields.get(6), fields.get(7), seats, price, fields.get(10), Boolean.parseBoolean(fields.get(11)));
        } catch (RuntimeException ex) {
            throw new IOException("Invalid booking values at line " + lineNumber + ".", ex);
        }
    }

    private String escape(String value) {
        String text = value == null ? "" : value;
        if (text.contains(",") || text.contains("\"") || text.contains("\n") || text.contains("\r")) return "\"" + text.replace("\"", "\"\"") + "\"";
        return text;
    }

    private List<String> parse(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder field = new StringBuilder();
        boolean quoted = false;
        for (int index = 0; index < line.length(); index++) {
            char current = line.charAt(index);
            if (quoted) {
                if (current == '"' && index + 1 < line.length() && line.charAt(index + 1) == '"') { field.append('"'); index++; }
                else if (current == '"') quoted = false;
                else field.append(current);
            } else if (current == ',' ) {
                fields.add(field.toString()); field.setLength(0);
            } else if (current == '"' && field.length() == 0) quoted = true;
            else field.append(current);
        }
        if (quoted) throw new IllegalArgumentException("Unclosed quote.");
        fields.add(field.toString());
        return fields;
    }
}
