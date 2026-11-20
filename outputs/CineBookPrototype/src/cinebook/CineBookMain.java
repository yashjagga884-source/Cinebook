package cinebook;

/** Application entry point for the Phase 1 viva prototype. */
public final class CineBookMain {
    private CineBookMain() { }

    public static void main(String[] args) {
        try {
            new CineBookApp(new InputReader()).run();
        } catch (Exception ex) {
            System.err.println("CineBook could not start: " + ex.getMessage());
        }
    }
}
