package cinebook;

import java.util.Scanner;

/** Handles console input without leaking scanner details to menu logic. */
public final class InputReader {
    private final Scanner scanner;

    public InputReader() { this(new Scanner(System.in)); }
    public InputReader(Scanner scanner) { this.scanner = scanner; }

    public String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.hasNextLine() ? scanner.nextLine().trim() : "9";
    }

    public int readInt(String prompt) {
        String value = readLine(prompt);
        try { return Integer.parseInt(value); }
        catch (NumberFormatException ex) { return -1; }
    }
}
