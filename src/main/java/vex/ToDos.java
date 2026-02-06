package vex;

/**
 * Represents a to-do task that contains only a description.
 * A to-do task does not have any associated date or time.
 */
public class ToDos extends Task {

    /**
     * Initializes a new ToDos task with the specified description.
     *
     * @param description The description of the todo task.
     */
    public ToDos(String description) {
        super(description);
    }

    /**
     * Returns a string representation of the todo task, including the
     * status icon and the description.
     *
     * @return A formatted string representing the todo task.
     */
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}
