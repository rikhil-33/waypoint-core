package waypoint.model;

import java.util.*;

/** VALUE OBJECT. Exact money. */
public final class Money implements Comparable<Money> {
    private final long paise;
    private final Currency currency;

    private Money(long paise, Currency currency) {
        this.currency = Objects.requireNonNull(currency, "Currency cannot be null");
        this.paise = paise;
    }

    public static Money ofMinor(long paise, Currency currency) {
        return new Money(paise, currency);
    }

    public static Money rupees(long amount) {
        return new Money(amount * 100, Currency.getInstance("INR"));
    }

    public Money plus(Money other) {
        requireSameCurrency(other);
        return new Money(this.paise + other.paise, this.currency);
    }

    public Money minus(Money other) {
        requireSameCurrency(other);
        return new Money(this.paise - other.paise, this.currency);
    }

    public Money times(int multiplier) {
        return new Money(this.paise * multiplier, this.currency);
    }

    public boolean isNegative() {
        return this.paise < 0;
    }

    @Override
    public int compareTo(Money other) {
        requireSameCurrency(other);
        return Long.compare(this.paise, other.paise);
    }

    public List<Money> allocate(int n) {
        if (n < 1) throw new IllegalArgumentException("Cannot allocate to less than 1");
        long base = this.paise / n;
        long remainder = this.paise % n;

        List<Money> shares = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            long amount = base + (i < remainder ? 1 : 0);
            shares.add(new Money(amount, this.currency));
        }
        return shares;
    }

    private void requireSameCurrency(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new CurrencyMismatch("Currency mismatch");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return this.paise == money.paise && this.currency.equals(money.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(paise, currency);
    }

    @Override
    public String toString() {
        return String.format("%s %d.%02d", currency.getCurrencyCode(), paise / 100, Math.abs(paise % 100));
    }
}

class CurrencyMismatch extends RuntimeException {
    public CurrencyMismatch(String message) {
        super(message);
    }
}