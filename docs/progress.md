# CineBook milestone progress

## Phase 1 — 2026-11-25

### Implemented and verified

- Created all 20 top-level classes named by the Phase 1 class map at `outputs/CineBookPrototype/src/cinebook/`; `javac --release 21` succeeds.
- Implemented movie and show catalogues, search, show-specific seat maps, seat validation, multi-seat booking, reference generation, booking history, cancellation and a Stream-based movie report.
- Implemented Java-only UTF-8 text/CSV persistence. New bookings are saved and reloaded/verified; active bookings reload and reserve their seats after restart; cancellation persists before releasing seats.
- Ran separate processes to observe booking, rejection of booked seats, restart restoration, cancellation/rebooking, report output, invalid email and unknown-show recovery.
- Added the Phase 1 README, design, problem/scope, concept map, validation record, progress log and dated evidence captures.

### Still open for the formal submission

- The task calls for a GitHub repository and regular commits; this workspace currently has no Git repository or remote. No upload/history is claimed.
- The task references a team of 3-4 and full attendance. Team identities and attendance are external facts not available in this workspace.
- Phase 2 classes and features remain unimplemented.

### Environment and build

- The handout targets JDK 21 LTS. Available compiler is JDK 26; compiling with `--release 21` succeeded.
- No SQL/JDBC/database or external library is used.
- Canonical prototype: `outputs/CineBookPrototype`; default booking file: `outputs/CineBookPrototype/data/bookings.txt` when run from the prototype directory.
