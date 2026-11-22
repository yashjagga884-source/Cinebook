# Programming in Java concept map

References below follow the Programming in Java (25CSE0202) handout: CLOs on page 1; lecture topics on pages 2-3; labs on pages 4-5; guided project and evaluation scheme on page 7; syllabus topics on pages 7-8.

| Handout concept and reference | Phase 1 code | Status and evidence |
|---|---|---|
| OOP, classes/objects, constructors, encapsulation, abstraction and class relationships (CLO01; lectures 1-5) | `Movie`, `Show`, `Seat`, `Booking`; responsibilities split across catalogues, service, repository, menu and codec classes in `outputs/CineBookPrototype/src/cinebook/`. | Implemented; all 20 classes compile. See `docs/design.md` and `docs/evidence/phase1-class-count.txt`. |
| Strings and wrappers (CLO01; lectures 6-9) | `InputReader`, `BookingValidator`, `BookingFileCodec`; IDs and email/seat input are normalized and parsed. | Implemented; invalid email and unknown show cases are captured in `docs/evidence/phase1-restart-check.txt`. |
| Exception handling and validation (CLO01; lectures 10-15) | `BookingValidator` raises controlled argument errors; file/repository classes propagate `IOException`; menu prints recoverable messages. | Implemented; already-booked seat and invalid email are rejected without booking. See validation record. |
| Collections and searching (CLO02; lectures 16-25) | `MovieCatalog`/`ShowCatalog` use `LinkedHashMap`; `BookingService` keeps bookings in a `List`; `BookingValidator` uses a `Set` to reject duplicate seat IDs. | Implemented; search, availability, duplicate and history paths are observable in the run captures. |
| Threads and synchronization (CLO03; lectures 26-33) | `BookingService.book` and `cancel` are synchronized; `SeatInventory` synchronizes reservation/release. | Critical methods are guarded. A competing-thread demo is not part of the Phase 1 class map and has not been demonstrated. |
| JDBC/relational persistence (CLO04; lectures 34-42) | No JDBC or SQL is used, as required by the Java-only task. | Deliberately omitted; this task specifies Java file I/O. |
| Lambdas and Stream API (CLO04; lectures 43-46) | Stream filtering in movie search and seat validation; `BookingReportService` groups active bookings by movie with `Collectors.groupingBy`. | Implemented; report output is captured in `docs/evidence/phase1-first-run.txt` and later runs. |
| Generics (CLO04; lectures 47-48) | Standard typed collections such as `List<Booking>`, `Map<String, SeatInventory>`, and `List<String>`. | Implemented through collection declarations; no unnecessary generic framework added. |
| File I/O and collections integration (CLO04; lectures 49-54) | `BookingFileStore`, `BookingFileCodec`, `BookingRepository`, `LocalBookingRepository`; UTF-8 CSV/text format with atomic replacement and reload on startup. | Implemented; booking save/reload, restart occupancy, cancellation and rebooking are in dated captures. |
| Guided project, validation, documentation, viva (CLO05; lectures 55-60) | 20-class prototype, setup/readme, problem/scope, architecture, validation and progress docs. | Prototype compiled and core scenarios observed. GitHub history/team attendance remain external submission items. |

## Course concepts deliberately not claimed

This Phase 1 prototype does not implement JDBC, SQL, database transactions, external inventory, or a concurrent-thread race demonstration. File persistence is the chosen alternative. The class map limits Phase 1 to 20 meaningful top-level classes; Phase 2's additional classes are separately listed in `TASK.md`.
