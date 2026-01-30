import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Deadlines extends Task {
    private LocalDateTime by;

    public Deadlines(String description, LocalDateTime by) {
        super(description);
        this.by = by;
    }
    
    public LocalDateTime getBy() {
        return by;
    }


    @Override
    public String toString() {
        DateTimeFormatter display = DateTimeFormatter.ofPattern("MMM d yyyy HH:mm");
        return "[D]" + super.toString() + " (by: " + by.format(display) + ")";
    }

    @Override
    public String toFileString() {
        return String.format("D | %d | %s | %s",
                isDone ? 1 : 0,
                description,
                by);
    }
}