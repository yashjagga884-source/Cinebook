# Phase 1 evidence index

All captures are dated 2026-11-25 and use fictional demo customer data.

- `phase1-class-count.txt`: confirms exactly 20 top-level classes in the Phase 1 source package and records the Java 21-targeted compile.
- `phase1-first-run.txt`: shows the main menu, booking A1/A2, total, save/reload verification, booked-seat rejection, seat map, history and Stream report.
- `phase1-reload-cancel-rebook.txt`: shows history and occupied seats loaded after restart, cancellation, seat release, and a new A1 booking.
- `phase1-restart-check.txt`: shows the rebooked A1 restored after another restart, duplicate-seat rejection, invalid email recovery, unknown-show recovery, and report output.
- `../..` source of the saved records: `outputs/CineBookPrototype/data/bookings.txt`.

The data uses `Demo Customer` and the reserved test domain `example.test`; no real personal information is included.
