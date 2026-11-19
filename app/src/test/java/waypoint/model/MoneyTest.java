package waypoint.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.Currency;

class MoneyTest {
    static final Currency INR = Currency.getInstance("INR");
    static final Currency USD = Currency.getInstance("USD");

    @Test void tenPaiseAddedHundredTimesIsExactlyOneRupee() {
        Money sum = Money.ofMinor(0, INR);
        for (int i = 0; i < 100; i++) sum = sum.plus(Money.ofMinor(10, INR));
        assertEquals(Money.rupees(10), sum);
    }

    @Test void equalAmountsAreEqualAndHashAlike() {
        assertEquals(Money.rupees(120), Money.ofMinor(12000, INR));
        assertEquals(Money.rupees(120).hashCode(), Money.ofMinor(12000, INR).hashCode());
    }

    @Test void mixingCurrenciesThrows() {
        assertThrows(CurrencyMismatch.class, () -> Money.rupees(10).plus(Money.ofMinor(10, USD)));
    }

    @Test void allocationLosesNoPaise() {
        var shares = Money.rupees(100).allocate(3);
        assertEquals(3, shares.size());
        assertEquals(Money.rupees(100), shares.stream().reduce(Money.ofMinor(0, INR), Money::plus));
    }
}