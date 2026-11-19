package cinebook;

/** Displays and reads the top-level prototype menu. */
public final class ConsoleMenu {
    private final InputReader input;

    public ConsoleMenu(InputReader input) { this.input = input; }

    public int show() {
        System.out.println("\n========== CINEBOOK ==========");
        System.out.println("1. Browse Movies\n2. Search Movies\n3. View Shows\n4. View Seats");
        System.out.println("5. Book Tickets\n6. Booking History\n7. Cancel Booking\n8. Movie Booking Report\n9. Exit");
        return input.readInt("Choose an option: ");
    }
}
