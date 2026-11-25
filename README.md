# CineBook

A standalone Java console application for browsing movies and shows, inspecting seat layouts, reserving seats, and managing bookings with UTF-8 CSV/text persistence — built using standard Java (`JDK 21+`) with no external libraries, SQL, or JDBC.

## Overview

CineBook prevents double-booking by tracking seat occupancy per show, validating seat selections, calculating tier-based ticket totals, generating unique booking references (`CB-<UUID>`), and verifying saved records by reloading the booking file after writes.

The repository includes:
- **Root Console Application (`src/`)**: Customer registration/login flow (`Main`, `Customer`, `User`, `BookingManager`, `FileBookingRepository`) with multi-seat reservation, history, and cancellation.
- **Phase 1 20-Class Prototype (`outputs/CineBookPrototype/`)**: Modular 20-class `cinebook` package featuring guest booking, startup occupancy restoration from disk, cancellation persistence before seat release, and a Java Stream-based active booking report (`BookingReportService`).

## Features

- **Movie & Show Browsing**: Lists 5 sample movies (`Interstellar`, `The Dark Knight`, `Inception`, `Avengers`, `Spider-Man`) and 5 scheduled shows across `PVR (Mumbai)` and `INOX (Delhi)`, with search by title or genre.
- **Seat Map & Tier Pricing**: Displays `O` (available) / `X` (booked) seat grids (`5x8` and `5x6` screens) with row-based pricing:
  - Row A — `Silver`: Rs. 150.00
  - Row B — `Gold`: Rs. 200.00
  - Row C — `Platinum`: Rs. 250.00
  - Rows D–E — `Recliner`: Rs. 350.00
- **Input & Seat Validation**: Rejects invalid email formats, unknown shows, duplicate seat IDs, nonexistent seats, and already-booked seats without modifying availability.
- **Atomic File Persistence**: Writes booking updates through a temporary file and moves it into place (`ATOMIC_MOVE` with fallback to `REPLACE_EXISTING`), then reloads and verifies booking reference, seat count, and total.
- **Booking History, Cancellation & Reporting**: View confirmed/cancelled bookings, cancel active bookings to release seats, and generate a Stream-grouped active booking count per movie (in the Phase 1 prototype).

## Project Structure

```text
Cinebook/
├── src/                               # Root console app (registration/login + booking)
│   ├── Main.java
│   ├── manager/BookingManager.java
│   ├── model/                         # Movie, Cinema, Screen, Show, Seat, SeatType, User, Customer, Booking
│   └── util/FileBookingRepository.java
├── outputs/CineBookPrototype/         # Standalone 20-class Phase 1 prototype
│   ├── src/cinebook/                  # 20 top-level classes in package cinebook
│   ├── data/bookings.txt
│   └── README.md
├── data/
│   ├── movies.dat
│   └── bookings.csv
└── docs/                              # Scope, concept map, architecture, validation & test captures
    ├── problem-and-scope.md
    ├── concept-map.md
    ├── design.md
    ├── validation.md
    ├── progress.md
    └── evidence/
```

## Prerequisites

- **Java Development Kit (JDK) 21 LTS or later**

## Quick Run (One-Line Commands)

Compiled `.class` files are already included in `out/` and `outputs/CineBookPrototype/out/`, so you can launch immediately from the repository root in PowerShell:

- **Phase 1 Prototype (Recommended):**
  ```powershell
  cd outputs/CineBookPrototype; java -cp out cinebook.CineBookMain
  ```
- **Root Application:**
  ```powershell
  java -cp out Main
  ```

### Compile & Run from Source (One-Line)

If you modify `.java` files, compile and run in one line (PowerShell):

- **Phase 1 Prototype:**
  ```powershell
  cd outputs/CineBookPrototype; javac --release 21 -d out src\cinebook\*.java; if ($?) { java -cp out cinebook.CineBookMain }
  ```
  - Default booking store: `outputs/CineBookPrototype/data/bookings.txt` (restores active bookings and occupied seats on startup; override with `CINEBOOK_BOOKINGS_FILE`).

- **Root Application:**
  ```powershell
  javac --release 21 -d out src\Main.java src\model\*.java src\manager\*.java src\util\*.java; if ($?) { java -cp out Main }
  ```
  - Default booking store: `data/bookings.csv` (override with `CINEBOOK_BOOKINGS_FILE`).

## Documentation & Validation Evidence

- [Problem and Scope](docs/problem-and-scope.md)
- [Design and Architecture](docs/design.md)
- [Java Concept Map](docs/concept-map.md)
- [Validation Record](docs/validation.md)
- [Milestone Progress](docs/progress.md)
- [Captured Run Logs](docs/evidence/README.md)

## Known Scope & Limitations

- Single-process academic prototype; not intended for concurrent multi-process file locking or production payment processing.
- Movie and show catalogues are preloaded in code; admin catalogue management, SQL/JDBC persistence, and external inventory integration are outside Phase 1 scope.

## Contributors

- **Yash Jagga** ([@yashjagga884-source](https://github.com/yashjagga884-source))
- **Vaibhav** ([@Vaibhav-1512](https://github.com/Vaibhav-1512))
- **Hitesh** ([@Hitesh0425](https://github.com/Hitesh0425))
