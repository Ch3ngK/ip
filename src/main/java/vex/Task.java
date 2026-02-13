package vex;

import java.time.LocalDate;

/**
 * Represents a generic task in the Vex application.
 * A task contains a description and a completion status.
 */
public class Task {

    /** Description of the task. */
    private final String description;

    /** Whether the task is marked as completed. */
    private boolean isDone;

    /**
     * Constructs a Task with the given description.
     * The task is initially marked as not done.
     *
     * @param description Description of the task
     * @throws IllegalArgumentException If description is null or blank
     */
    public Task(String description) {
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Task description must not be empty.");
        }
        this.description = description;
        this.isDone = false;
    }

    /**
     * Returns the task description.
     *
     * @return Task description
     */
    protected String getDescription() {
        return description;
    }

    /**
     * Returns whether the task is marked as done.
     *
     * @return true if completed
     */
    protected boolean isDone() {
        return isDone;
    }

    /**
     * Returns the status icon based on whether the task is done.
     *
     * @return "X" if the task is done, otherwise " "
     */
    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    /**
     * Marks the task as done.
     */
    public void markAsDone() {
        isDone = true;
    }

    /**
     * Marks the task as not done.
     */
    public void markAsUndone() {
        isDone = false;
    }

    /**
     * Returns a user-friendly string representation of the task.
     *
     * @return Formatted task string
     */
    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }

    /**
     * Converts this task into a format suitable for file storage.
     * Subclasses should override if they store additional fields.
     *
     * @return Pipe-separated save string
     */
    public String toFileString() {
        return String.format("T | %d | %s", isDone ? 1 : 0, description);
    }

    /**
     * Checks whether this task occurs on the given date.
     * The default implementation returns false.
     *
     * @param date Date to check
     * @return false for tasks without date information
     */
    public boolean occursOn(LocalDate date) {
        return false;
    }

    /**
     * Checks whether this task is due within the given date range.
     * The default implementation returns false.
     *
     * @param startDate Start of the date range (inclusive)
     * @param days      Number of days in the range
     * @return false for tasks without date information
     */
    public boolean isDueWithin(LocalDate todayDate, int days) {
        return false;
    }
}
