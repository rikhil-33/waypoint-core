# waypoint-core

A Java domain model for a parking management system, built around Domain-Driven Design (DDD) principles—strict invariants, immutable value objects, identity-driven entities, and pure domain logic with zero external framework dependencies.

## Domain Model

| Component | Type | Responsibility & Invariants |
| :--- | :--- | :--- |
| `Money` | Value Object | Exact integer arithmetic in minor units (paise) to avoid floating-point drift. Enforces currency matching (`CurrencyMismatch`) and lossless `allocate(n)`. |
| `Registration` | Value Object | Normalizes (trims, upper-cases) and validates Indian vehicle registration plates (e.g., `MH12AB1234`). |
| `TicketId`, `SpotId`, `VehicleId` | Value Objects (`Ids.java`) | Distinct strongly-typed ID records rejecting `null`/blank values so identifiers cannot be swapped at call sites. |
| `Spot` | Entity | Tracks a parking spot's occupancy. Enforces single-occupant assignment (`tryAssign`), idempotent `release`, and equality strictly by `SpotId`. |
| `Ticket` | Entity | Manages a parking session lifecycle (`open` → `close`). Enforces non-null `entry`, `exit >= entry`, non-negative `fare`, and one-way closure (`TicketAlreadyClosed`). |
| `FarePolicy` | Functional Interface | Decouples fare calculation (`fareFor(entry, exit, spot)`) from `Ticket` state transitions. |

## Project Structure

```text
app/src/
├── main/java/waypoint/model/
│   ├── FarePolicy.java
│   ├── Ids.java
│   ├── Money.java
│   ├── Registration.java
│   ├── Spot.java
│   └── Ticket.java
└── test/java/waypoint/model/
    ├── MoneyTest.java
    ├── SpotTest.java
    └── TicketTest.java
```

## Design Highlights

- **Always-valid objects:** Constructors and compact record constructors reject invalid state immediately—no invalid `Money`, `Registration`, or ID can exist.
- **Deterministic time:** `Ticket` never calls `Instant.now()`; timestamps are passed in explicitly, making all business rules fast and deterministic to test.
- **Entity vs. Value equality:** Value objects compare by all fields; entities (`Spot`, `Ticket`) compare strictly by their ID.

## Running Tests

Requires Java 17+ (Gradle Wrapper included).

```bash
./gradlew test
```