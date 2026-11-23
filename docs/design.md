# CineBook Phase 1 design

## Package and class responsibilities

All 20 top-level classes are in package `cinebook` at `outputs/CineBookPrototype/src/cinebook/`.

| Responsibility group | Classes |
|---|---|
| Application / console | `CineBookMain`, `CineBookApp`, `ConsoleMenu`, `InputReader` |
| Domain models and catalogues | `Movie`, `Show`, `Seat`, `Booking`, `MovieCatalog`, `ShowCatalog` |
| Booking rules and inventory | `BookingValidator`, `BookingReferenceGenerator`, `SeatInventory`, `BookingService` |
| File persistence | `BookingFileCodec`, `BookingFileStore`, abstract `BookingRepository`, `LocalBookingRepository` |
| Presentation / report | `SeatMapFormatter`, `BookingReportService` |

The count is 20 top-level class declarations. `BookingRepository` is abstract; no top-level enum/interface/helper declarations are present in this Phase 1 prototype.

## Booking flow

```mermaid
flowchart TD
  A[Browse/search movies] --> B[Choose show]
  B --> C[Display seat map]
  C --> D[Enter customer details and seats]
  D --> E{Input and seats valid?}
  E -- No --> F[Show recoverable error]
  F --> C
  E -- Yes --> G[Reserve seats in SeatInventory]
  G --> H[Write all bookings through a temporary file]
  H --> I[Reload and verify booking snapshot]
  I --> J[Show reference and total]
  J --> K[History, report or cancellation]
```

## Model relationships

```mermaid
classDiagram
  Movie "1" --> "many" Show
  Show "1" *-- "many" Seat
  Show "1" --> "many" Booking
  Booking "1" --> "many" seatNumbers
  CineBookApp --> MovieCatalog
  CineBookApp --> ShowCatalog
  CineBookApp --> BookingService
  BookingService --> BookingValidator
  BookingService --> SeatInventory
  BookingService --> BookingRepository
  BookingRepository <|-- LocalBookingRepository
  LocalBookingRepository --> BookingFileCodec
  LocalBookingRepository --> BookingFileStore
```

## File format and persistence

The booking file is UTF-8 CSV with a fixed header. Each row records booking reference, show/movie/cinema details, customer name/email, selected seat IDs, total, timestamp, and cancellation status. `BookingFileCodec` escapes and parses CSV fields. `BookingFileStore` writes a complete replacement into a temporary file and moves it into place; `LocalBookingRepository` maps the text records to `Booking` objects. `BookingService` loads active bookings and reserves their seats at startup. After a new booking is saved, the service reloads it and verifies reference, seats, and total before reporting success.

Cancellation persists the cancelled flag before releasing seats in memory. This is a local single-process prototype; it does not provide file locking across independent running processes.
