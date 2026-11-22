# CineBook problem and scope

## Problem statement

A small cinema needs a consistent way to show movie performances and seat availability while preventing the same seat from being sold twice. CineBook is an academic Java console prototype that lets a customer browse sample movies and shows, reserve available seats, and cancel a booking. Its defined sample catalogue and local text-file persistence make the workflow small enough to implement and explain as a standalone Java application.

## Intended users and context

- **Customer:** browses the sample catalogue, selects a show and seats, and can review or cancel bookings.
- **Cinema operator/evaluator:** runs the prototype and reviews the booking report and local booking records.
- **Context:** a single-process classroom demonstration; no connection to real cinema inventory or payment service.

## Measurable objectives

1. List five sample movies and five shows, and search the catalogue by title or genre.
2. Display a show-specific seat map and current availability.
3. Accept an available multi-seat selection, produce a unique booking reference, calculate the seat total, save it to a UTF-8 text/CSV file, and reload the saved booking before confirming it.
4. Reject an invalid show, malformed customer email, missing/nonexistent/duplicate/already-booked seat without changing availability.
5. Reload active bookings and occupied seats after restart; cancel an active booking, persist its cancelled status, release its seats, and allow those seats to be booked again.
6. Produce a Stream-based count of active bookings by movie.

## Scope and expected outputs

The Phase 1 prototype has exactly the 20 top-level classes listed in the `TASK.md` class map. It covers movie/show search, seat layout, multi-seat reservation, history, cancellation, text-file persistence, and a small booking report. It uses Java collections and standard Java file I/O only.

Expected console outputs include movie/show lists, `O`/`X` seat maps, a booking reference and total, controlled validation messages, history/cancellation status, and report counts. A saved booking file contains a header plus one escaped CSV row per booking.

## Boundaries and assumptions

- Java 21 source compatibility; no SQL, JDBC, relational database, server, or external dependency.
- The prototype uses fixed sample movies/shows embedded in `CineBookApp`; no administrator catalogue editor is included in Phase 1.
- Customer name/email are collected when booking; registration and authentication are not part of the 20-class map.
- Booking records and active occupied seats are restored from file. Concurrent multi-process access, remote inventory, payment, food, and production security are excluded.
- Phase 2's ten classes and additional admin/import/export features remain out of this first-evaluation build.
