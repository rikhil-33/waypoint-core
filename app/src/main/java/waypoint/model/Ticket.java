package waypoint.model;

import java.time.Instant;
import java.util.Objects;

/** ENTITY. Invariants:
-   I1  entry is always present
-   I2  exit is null, or not before entry
-   I3  fare is null, or not negative
-   I4  fare exists only if exit exists
-   I5  a closed ticket never reopens
 */
public final class Ticket {
    private final TicketId id;
    private final SpotId spotId;
    private final Instant entry;
    
    private Instant exit;
    private Money fare;

    private Ticket(TicketId id, SpotId spotId, Instant entry) {
        this.id = Objects.requireNonNull(id, "TicketId cannot be null");
        this.spotId = Objects.requireNonNull(spotId, "SpotId cannot be null");
        this.entry = Objects.requireNonNull(entry, "Entry Instant cannot be null");
    }

    public static Ticket open(TicketId id, SpotId spotId, Instant entry) {
        return new Ticket(id, spotId, entry);
    }

    public boolean isOpen() {
        return this.exit == null;
    }

    public Money close(Instant at, FarePolicy policy) {
        if (!isOpen()) {
            throw new TicketAlreadyClosed("A closed ticket never reopens");
        }
        if (at.isBefore(this.entry)) {
            throw new IllegalArgumentException("Exit time cannot be before entry time");
        }
        
        this.exit = at;
        this.fare = policy.fareFor(this.entry, this.exit, this.spotId);
        
        if (this.fare != null && this.fare.isNegative()) {
            throw new IllegalStateException("Fare cannot be negative");
        }
        
        return this.fare;
    }

    public TicketId id() { return id; }
    public SpotId spotId() { return spotId; }
    public Instant entry() { return entry; }
    public Instant exit() { return exit; }
    public Money fare() { return fare; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        return id.equals(ticket.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

class TicketAlreadyClosed extends RuntimeException {
    public TicketAlreadyClosed(String message) {
        super(message);
    }
}