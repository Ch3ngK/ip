package vex;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Events extends Task {
    protected LocalDateTime from;
    protected LocalDateTime to;

    public Events(String description, LocalDateTime from, LocalDateTime to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    public LocalDateTime getFrom() {
        return from;
    }

    public LocalDateTime getTo() {
        return to;
    }   


    @Override
    public String toString() {
        DateTimeFormatter display = DateTimeFormatter.ofPattern("MMM d yyyy HH:mm");
        return "[E]" + super.toString() + " (from: " + from.format(display) + " to: " + to.format(display) + ")";
    }

    @Override
    public String toFileString() {
        return String.format("E | %d | %s | %s | %s",
                isDone ? 1 : 0,
                description,
                from,
                to);
    }

    @Override
    public boolean occursOn(LocalDate date) {
        LocalDate start = from.toLocalDate();
        LocalDate end = to.toLocalDate();
        return (date.isEqual(start) || date.isEqual(end)) || (date.isAfter(start) && date.isBefore(end));
    }
}