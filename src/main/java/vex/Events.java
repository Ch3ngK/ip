package vex;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents an event task that occurs during a specific time range.
 * An event has a description, a start time, and an end time.
 */
public class Events extends Task {

    /** Formatter used for displaying event times. */
    private static final DateTimeFormatter DISPLAY_FORMAT = DateTimeFormatter.ofPattern("MMM d yyyy HH:mm");

    /** Start date and time of the event. */
    private final LocalDateTime from;

    /** End date and time of the event. */
    private final LocalDateTime to;

    /**
     * Constructs an Event task with a time range.
     *
     * @param description Task description
     * @param from        Start date and time
     * @param to          End date and time
     * @throws IllegalArgumentException if dates are null or invalid
     */
    public Events(String description, LocalDateTime from, LocalDateTime to) {
        super(description);

        if (from == null || to == null) {
            throw new IllegalArgumentException("Event dates must not be null.");
        }

        if (from.isAfter(to)) {
            throw new IllegalArgumentException("Event start time cannot be after end time.");
        }

        this.from = from;
        this.to = to;
    }

    /**
     * Returns the start time of the event.
     *
     * @return Start LocalDateTime
     */
    public LocalDateTime getFrom() {
        return from;
    }

    /**
     * Returns the end time of the event.
     *
     * @return End LocalDateTime
     */
    public LocalDateTime getTo() {
        return to;
    }

    /**
     * Returns a formatted string representation of the event.
     *
     * @return User-friendly event string
     */
    @Override
    public String toString() {
        return "[E]" + super.toString()
                + " (from: " + from.format(DISPLAY_FORMAT)
                + " to: " + to.format(DISPLAY_FORMAT) + ")";
    }

    /**
     * Converts the event into a format suitable for file storage.
     *
     * @return Pipe-separated save string
     */
    @Override
    public String toFileString() {
        return String.format("E | %d | %s | %s | %s",
                isDone() ? 1 : 0,
                getDescription(),
                from,
                to);
    }

    /**
     * Checks whether the event occurs on the given date.
     * An event occurs on a date if that date is between the start and end dates
     * (inclusive).
     *
     * @param date The date to check
     * @return true if the event spans the given date
     */
    @Override
    public boolean occursOn(LocalDate date) {
        if (date == null) {
            return false;
        }

        LocalDate start = from.toLocalDate();
        LocalDate end = to.toLocalDate();

        return !date.isBefore(start) && !date.isAfter(end);
    }
}
