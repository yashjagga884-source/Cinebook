package cinebook;

import java.util.UUID;

/** Generates non-repeating, readable booking references. */
public final class BookingReferenceGenerator {
    public String nextReference() { return "CB-" + UUID.randomUUID().toString().toUpperCase(); }
}
