package vex;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a deadline task.
 * A deadline task has a description and a date/time by which it must be
 * completed.
 */
public class Deadlines extends Task {

    /** Formatter used for displaying deadlines. */
    private static final DateTimeFormatter DISPLAY_FORMAT = DateTimeFormatter.ofPattern("MMM d yyyy HH:mm");

    /** The date and time by which the task must be completed. */
    private final LocalDateTime by;

    /**
     * Constructs a Deadline task with the specified description and due date.
     *
     * @param description The task description
     * @param by          The deadline date and time
     * @throws IllegalArgumentException If by is null
     */
    public Deadlines(String description, LocalDateTime by) {
        super(description);

        if (by == null) {
            throw new IllegalArgumentException("Deadline date must not be null.");
        }

        this.by = by;
    }

    /**
     * Returns the deadline date and time.
     *
     * @return The LocalDateTime representing when the task is due
     */
    public LocalDateTime getBy() {
        return by;
    }

    /**
     * Returns a formatted string representation of the deadline task.
     *
     * @return A user-friendly string showing task status and deadline
     */
    @Override
    public String toString() {
        return "[D]" + super.toString()
                + " (by: " + by.format(DISPLAY_FORMAT) + ")";
    }

    /**
     * Converts the deadline task into a format suitable for file storage.
     *
     * @return A pipe-separated string representing the task
     */
    @Override
    public String toFileString() {
        return String.format("D | %d | %s | %s",
                isDone() ? 1 : 0,
                getDescription(),
                by);
    }

    /**
     * Checks whether this deadline occurs on the specified date.
     *
     * @param date The date to compare against
     * @return true if the deadline falls on the given date
     */
    @Override
    public boolean occursOn(LocalDate date) {
        if (date == null) {
            return false;
        }
        return by.toLocalDate().equals(date);
    }
}
