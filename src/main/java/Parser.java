import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Parser {

    public static final DateTimeFormatter INPUT_FORMAT = 
            DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");

    /**
     * Parses the user input and executes the corresponding actions.
     * * @param input   The full string entered by the user.
     * @param tasks   The TaskList to modify or query.
     * @param ui      The Ui to handle user feedback.
     * @param storage The Storage to save changes to the file.
     */
    public static void handleCommand(String input, TaskList tasks, Ui ui, Storage storage) {
        
        // Show List
        if (input.equalsIgnoreCase("list")) {
            ui.showTaskList(tasks);
            return;
        }

        // Show Date 
        if (input.startsWith("show")) {
            handleShow(input, tasks, ui);
            return;
        }

        // Mark/Unmark
        if (input.startsWith("mark") || input.startsWith("unmark")) {
            handleMarkStatus(input, tasks, ui, storage);
            return;
        }

        // Delete
        if (input.startsWith("delete")) {
            handleDelete(input, tasks, ui, storage);
            return;
        }

        // Create tasks (Todo, Deadline, Event)
        if (input.startsWith("todo") || input.startsWith("deadline") || input.startsWith("event")) {
            handleAddTask(input, tasks, ui, storage);
            return;
        }

        // For when input is unknown
        ui.showError("I apologise, but I am unsure of what that means. Care to edit your message? :-(");
    }

    private static void handleShow(String input, TaskList tasks, Ui ui) {
        String[] parts = input.split(" ", 2);
        if (parts.length < 2 || parts[1].trim().isEmpty()) {
            ui.showError("You must provide a date in yyyy-MM-dd format. Remember to do so!");
            return;
        }
        try {
            LocalDate queryDate = LocalDate.parse(parts[1].trim());
            ui.showTasksOnDate(tasks, queryDate);
        } catch (DateTimeParseException e) {
            ui.showError("Invalid date format. Use yyyy-MM-dd.");
        }
    }

    private static void handleMarkStatus(String input, TaskList tasks, Ui ui, Storage storage) {
        String[] parts = input.split(" ");
        if (parts.length < 2) {
            ui.showError("You did not specify a task number to " + parts[0] + ".");
            return;
        }
        try {
            int index = Integer.parseInt(parts[1]) - 1;
            if (index < 0 || index >= tasks.size()) {
                ui.showError("Task number out of range.");
                return;
            }
            Task t = tasks.get(index);
            if (parts[0].equals("mark")) {
                t.markAsDone();
                ui.showMarkedTask(t);
            } else {
                t.markAsUndone();
                ui.showUnmarkedTask(t);
            }
            storage.save(tasks.getTasks());
        } catch (NumberFormatException e) {
            ui.showError("Invalid task number format.");
        }
    }

    private static void handleDelete(String input, TaskList tasks, Ui ui, Storage storage) {
        String[] parts = input.split(" ", 2);
        if (parts.length < 2) {
            ui.showError("You did not specify a task number to delete.");
            return;
        }
        try {
            int index = Integer.parseInt(parts[1]) - 1;
            if (index < 0 || index >= tasks.size()) {
                ui.showError("Task number out of range.");
                return;
            }
            Task removed = tasks.delete(index);
            storage.save(tasks.getTasks());
            ui.showDeletedTask(removed, tasks.size());
        } catch (NumberFormatException e) {
            ui.showError("Invalid task number format.");
        }
    }

    private static void handleAddTask(String input, TaskList tasks, Ui ui, Storage storage) {
        try {
            Task newTask = null;
            if (input.startsWith("todo")) {
                String desc = input.substring(4).trim();
                if (desc.isEmpty()) throw new IllegalArgumentException("The description of a todo cannot be empty! Remember to fill it up! :-)");
                newTask = new ToDos(desc);
            } else if (input.startsWith("deadline")) {
                String[] parts = input.substring(8).split(" /by ", 2);
                newTask = new Deadlines(parts[0].trim(), LocalDateTime.parse(parts[1].trim(), INPUT_FORMAT));
            } else if (input.startsWith("event")) {
                String[] parts = input.substring(5).split(" /from | /to ");
                newTask = new Events(parts[0].trim(), 
                        LocalDateTime.parse(parts[1].trim(), INPUT_FORMAT), 
                        LocalDateTime.parse(parts[2].trim(), INPUT_FORMAT));
            }

            if (newTask != null) {
                tasks.add(newTask);
                storage.save(tasks.getTasks());
                ui.showAddedTask(newTask, tasks.size());
            }
        } catch (Exception e) {
            ui.showError("Invalid format for adding a task. Please check your timing or description!");
        }
    }
}