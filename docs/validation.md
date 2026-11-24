# Phase 1 validation record

Current source and observed results are captured under `docs/evidence/`.

| Scenario | Expected | Observed | Evidence |
|---|---|---|---|
| Compile prototype | All 20 Phase 1 classes compile with Java 21 language/API compatibility. | Passed using JDK 26 `javac --release 21`; source file and top-level class counts are both 20. | `docs/evidence/phase1-class-count.txt` |
| Book multiple available seats | Booking receives a unique reference, correct seat total, and a stored/reloaded file record. | Passed; seats A1 and A2 total Rs. 300.00 and the app reports save/reload verification. | `docs/evidence/phase1-first-run.txt`, `outputs/CineBookPrototype/data/bookings.txt` |
| Book a seat already held | Existing active seat cannot be booked again. | Passed; A1 was rejected and availability stayed unchanged. | `docs/evidence/phase1-first-run.txt` |
| Restart and reload active seats | Booking history and occupancy are restored from text file. | Passed; restarted run showed 38/40 available and A1/A2 marked booked. | `docs/evidence/phase1-reload-cancel-rebook.txt` |
| Cancel and rebook | Cancellation is persisted; seats become available; a new booking can reserve the released seat. | Passed; cancelled A1/A2 booking and then rebooked A1. | `docs/evidence/phase1-reload-cancel-rebook.txt` |
| Report | Stream report counts active bookings by movie. | Passed; Interstellar count shown after rebooking. | `docs/evidence/phase1-reload-cancel-rebook.txt` |
| Invalid input | Invalid email, unknown show and unavailable seat produce recoverable errors. | Passed; invalid email and booked A1 are rejected without corrupting bookings. | `docs/evidence/phase1-restart-check.txt` |
| CSV/text format | Fields are escaped, parsed, and restored. | CSV records reload across separate processes; email/name are stored in rows. | `outputs/CineBookPrototype/data/bookings.txt` |

## Reproduction

From `outputs/CineBookPrototype` in PowerShell:

```powershell
javac --release 21 -d out src\cinebook\*.java
java -cp out cinebook.CineBookMain
```

The default file is `data/bookings.txt`. The application restores active seat reservations from that file at startup. Set `CINEBOOK_BOOKINGS_FILE` to choose another file path.

## Known limits

No concurrency race, malformed-file injection, forced write-failure, admin workflow, or SQL/JDBC scenario was demonstrated. These are not represented as passed Phase 1 evidence.
