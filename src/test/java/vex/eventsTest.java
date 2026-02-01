package vex; 

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import vex.Events;

public class eventsTest {
    
    @Test
    public void toString_validFormat_returnsCustomString() {
        LocalDateTime start = LocalDateTime.of(2026, 2, 1, 10, 0);
        LocalDateTime end = LocalDateTime.of(2026, 2, 1, 12, 0);
        Events event = new Events("Concert", start, end);
        
        String expected = "[E][ ] Concert (from: Feb 1 2026 10:00 to: Feb 1 2026 12:00)";
        assertEquals(expected, event.toString());
    }

    @Test
    public void toFileString_validInput_formattedCorrectly() {
        LocalDateTime start = LocalDateTime.of(2026, 2, 1, 10, 0);
        LocalDateTime end = LocalDateTime.of(2026, 2, 1, 12, 0);
        Events event = new Events("Meeting", start, end);
        
        assertEquals("E | 0 | Meeting | 2026-02-01T10:00 | 2026-02-01T12:00", event.toFileString());
        event.markAsDone();
        assertEquals("E | 1 | Meeting | 2026-02-01T10:00 | 2026-02-01T12:00", event.toFileString());
    }
}