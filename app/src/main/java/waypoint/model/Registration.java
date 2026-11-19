package waypoint.model;

import java.util.Objects;

/** VALUE OBJECT. */
public record Registration(String value) {
    
    public Registration {
        Objects.requireNonNull(value, "Registration cannot be null");
        value = value.trim().toUpperCase();
        
        // Format: two letters, two digits, one or two letters, four digits. e.g. MH12AB1234
        if (!value.matches("^[A-Z]{2}\\d{2}[A-Z]{1,2}\\d{4}$")) {
            throw new IllegalArgumentException("Invalid registration format. Expected format: MH12AB1234");
        }
    }
}