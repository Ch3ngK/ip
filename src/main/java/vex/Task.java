package vex;

import java.time.LocalDate;

/**
 * Represents a generic task in the Vex application.
 * A task contains a description and a completion status.
 */
public class Task {
    protected String description;
    protected boolean isDone;

    /**
     * Initializes a new Task with the given description.
     * The task is set to not done by default.
     *
     * @param description The description of the task.
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Returns the status icon based on whether the task is done.
     *
     * @return "X" if the task is done, otherwise a space " ".
     */
    public String getStatusIcon() {
        return (isDone ? "X" : " ");
    }

    /**
     * Sets the task status to completed.
     */
    public void complete() {
        this.isDone = true;
    }

    /**
     * Sets the task status to incomplete.
     */
    public void incomplete() {
        this.isDone = false;
    }

    /**
     * Returns a string representation of the task, including its status and description.
     *
     * @return A formatted string of the task.
     */
    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + this.description;
    }

    /**
     * Marks the task as done.
     */
    public void markAsDone() {
        this.complete();
    }

    /**
     * Marks the task as undone.
     */
    public void markAsUndone() {
        this.incomplete();
    }

    /**
     * Formats the task into a string suitable for saving to a file.
     * Base implementation defaults to a Todo-style format.
     *
     * @return A formatted string for file storage.
     */
    public String toFileString() {
        return String.format("T | %d | %s",
                isDone ? 1 : 0,
                description);
    }

    /**
     * Checks if the task occurs on a specific date.
     * Base implementation returns false; intended to be overridden by subclasses.
     *
     * @param date The date to check against.
     * @return false by default.
     */
    public boolean occursOn(LocalDate date) {
        return false; 
    }
}