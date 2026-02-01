package vex;

import java.time.LocalDate;

public class Task {
    protected String description;
    protected boolean isDone;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public String getStatusIcon() {
        return (isDone ? "X" : " ");
    }

    public void complete() {
        this.isDone = true;
    }

    public void incomplete() {
        this.isDone = false;
    }

    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + this.description;
    }

    public void markAsDone() {
        this.complete();
    }

    public void markAsUndone() {
        this.incomplete();
    }

    public String toFileString() {
        return String.format("T | %d | %s",
                isDone ? 1 : 0,
                description);
    }

    public boolean occursOn(LocalDate date) {
        return false;
    }

}
