# CineBook Phase 1 viva prototype

This is the 20-class, Java-only Phase 1 prototype listed in the root `TASK.md`. It demonstrates movie/show browsing, seat maps, seat validation, booking, CSV/text save and reload, cancellation, and a Stream-based booking report. It uses no SQL, JDBC, database, build tool, or external library.

## Requirements and run

Use JDK 21 LTS or later. From this directory in PowerShell:

```powershell
javac --release 21 -d out src\cinebook\*.java
java -cp out cinebook.CineBookMain
```

The default booking file is `data/bookings.txt`. Set `CINEBOOK_BOOKINGS_FILE` to select another path. When the app starts, it loads saved records and reserves their seats before displaying availability.

## Menu

1. Browse Movies
2. Search Movies
3. View Shows
4. View Seats
5. Book Tickets
6. Booking History
7. Cancel Booking
8. Movie Booking Report
9. Exit

For a booking, choose a show ID, enter a customer name and email, then enter seat IDs separated by commas, for example `A1,A2`. An active booking is written using a UTF-8 CSV header and one escaped record per booking. A booking is accepted only after the saved file is reloaded and its reference, seat count, and total are checked.

## Phase 1 class count

`src/cinebook/` contains exactly 20 top-level classes: the 20 Phase 1 classes listed in the root `TASK.md`. There are no additional top-level enum, interface, or helper class declarations in this prototype. Each class has a specific menu, model, catalogue, validation, inventory, persistence, booking, layout, or report responsibility.

## Limitations

The catalogue uses sample data in code. Customer accounts, administrator functions, and durable catalogue editing are not included in Phase 1. Booking records and seat occupancy reload from the booking file. File writes replace the prior file through a temporary file; this is a single-process academic prototype, not a multi-user ticketing service.
