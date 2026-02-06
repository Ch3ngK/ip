package vex;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents an event task that occurs during a specific time range.
 * An event has a description, a start time, and an end time.
 */
public class Events extends Task {
    protected LocalDateTime from;
    protected LocalDateTime to;

    /**
     * Initializes a new Events task with the specified description and time range.
     *
     * @param description The description of the event.
     * @param from        The start date and time of the event.
     * @param to          The end date and time of the event.
     */
    public Events(String description, LocalDateTime from, LocalDateTime to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns the start time of the event.
     *
     * @return The LocalDateTime representing the start of the event.
     */
    public LocalDateTime getFrom() {
        return from;
    }

    /**
     * Returns the end time of the event.
     *
     * @return The LocalDateTime representing the end of the event.
     */
    public LocalDateTime getTo() {
        return to;
    }

    /**
     * Returns a string representation of the event task, including the
     * status icon, description, and the formatted start and end times.
     *
     * @return A formatted string representing the event.
     */

    @Override
    public String toString() {
        DateTimeFormatter display = DateTimeFormatter.ofPattern("MMM d yyyy HH:mm");
        return "[E]" + super.toString() + " (from: " + from.format(display)
                + " to: " + to.format(display) + ")";
    }

    /**
     * Formats the event task into a string suitable for saving to a file.
     *
     * @return A pipe-separated string representing the event task for storage.
     */
    @Override
    public String toFileString() {
        return String.format("E | %d | %s | %s | %s",
                isDone ? 1 : 0,
                description,
                from,
                to);
    }

    /**
     * Checks if the event occurs on or spans across the specified date.
     *
     * @param date The date to check.
     * @return true if the date is the start date, end date, or falls between them.
     */
    @Override
    public boolean occursOn(LocalDate date) {
        LocalDate start = from.toLocalDate();
        LocalDate end = to.toLocalDate();
        return (date.isEqual(start) || date.isEqual(end))
                || (date.isAfter(start) && date.isBefore(end));
    }
}
