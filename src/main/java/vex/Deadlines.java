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
    private LocalDateTime by;

    /**
     * Initializes a new Deadlines task with the specified description and deadline.
     *
     * @param description The description of the task.
     * @param by          The date and time the task is due.
     */
    public Deadlines(String description, LocalDateTime by) {
        super(description);
        this.by = by;
    }

    /**
     * Returns the deadline of the task.
     *
     * @return The LocalDateTime representing when the task is due.
     */

    public LocalDateTime getBy() {
        return by;
    }

    /**
     * Returns a string representation of the deadline task, including the
     * status icon, description, and formatted deadline date.
     *
     * @return A formatted string representing the deadline.
     */
    @Override
    public String toString() {
        DateTimeFormatter display = DateTimeFormatter.ofPattern("MMM d yyyy HH:mm");
        return "[D]" + super.toString() + " (by: " + by.format(display) + ")";
    }

    /**
     * Formats the deadline task into a string suitable for saving to a file.
     *
     * @return A pipe-separated string representing the deadline task for storage.
     */
    @Override
    public String toFileString() {
        return String.format("D | %d | %s | %s",
                isDone ? 1 : 0,
                description,
                by);
    }

    /**
     * Checks if the deadline falls on the specified date.
     *
     * @param date The date to compare against.
     * @return true if the deadline date matches the input date, false otherwise.
     */
    @Override
    public boolean occursOn(LocalDate date) {
        return this.by.toLocalDate().equals(date);
    }
}
