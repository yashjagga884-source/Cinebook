package cinebook;

/** Renders a show-specific seat map using O for open and X for reserved. */
public final class SeatMapFormatter {
    public String format(Show show) {
        StringBuilder map = new StringBuilder("Seat map for ").append(show.getShowId()).append(" - ").append(show.getMovie().getTitle()).append('\n');
        Character previousRow = null;
        for (Seat seat : show.getSeats()) {
            char row = seat.getSeatNumber().charAt(0);
            if (previousRow != null && previousRow != row) map.append('\n');
            map.append(seat.isBooked() ? "X" : "O").append(seat.getSeatNumber()).append(' ');
            previousRow = row;
        }
        return map.toString();
    }
}
