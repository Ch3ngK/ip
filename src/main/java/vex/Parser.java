package vex;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Parses user input into commands and executes them.
 * This class serves as the logic engine that connects user input to TaskList
 * and Storage.
 */
public class Parser {

    /** The format expected for date-time input: yyyy-MM-dd HHmm. */
    public static final DateTimeFormatter INPUT_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");

    /**
     * * Parses the user input and executes the corresponding actions. * * @param
     * input The full string entered by the user. * @param tasks The TaskList to
     * modify or query. * @param ui The Ui to handle user feedback. * @param storage
     * The Storage to save changes to the file.
     */
    public static void handleCommand(String input, TaskList tasks, Ui ui, Storage storage) {

        // Programmer assumptions
        assert input != null : "input should not be null";
        assert tasks != null : "tasks should not be null";
        assert ui != null : "ui should not be null";
        assert storage != null : "storage should not be null";

        if (input.equalsIgnoreCase("list")) {
            ui.showTaskList(tasks);
            return;
        }

        if (input.startsWith("show")) {
            handleShow(input, tasks, ui);
            return;
        }

        if (input.startsWith("mark") || input.startsWith("unmark")) {
            handleMarkStatus(input, tasks, ui, storage);
            return;
        }

        if (input.startsWith("delete")) {
            handleDelete(input, tasks, ui, storage);
            return;
        }

        if (input.startsWith("todo") || input.startsWith("deadline")
                || input.startsWith("event")) {
            handleAddTask(input, tasks, ui, storage);
            return;
        }

        if (input.startsWith("find")) {
            handleFind(input, tasks, ui);
            return;
        }

        ui.showError("I apologise, but I am unsure of what that means. Care to edit your message? :-(");
    }

    /**
     * * Processes the 'show' command to display tasks on a specific date. *
     * * @param input The raw user input. * @param tasks The current task list.
     * * @param ui The UI used for output.
     */
    private static void handleShow(String input, TaskList tasks, Ui ui) {

        String[] parts = input.split(" ", 2);

        if (parts.length < 2 || parts[1].trim().isEmpty()) {
            ui.showError("You must provide a date in yyyy-MM-dd format. Remember to do so!");
            return;
        }

        assert parts.length == 2 : "show command should contain a date part";

        try {
            LocalDate queryDate = LocalDate.parse(parts[1].trim());
            ui.showTasksOnDate(tasks, queryDate);
        } catch (DateTimeParseException e) {
            ui.showError("Invalid date format. Use yyyy-MM-dd.");
        }
    }

    /**
     * * Processes 'mark' and 'unmark' commands to change task completion status. *
     * * @param input The raw user input. * @param tasks The current task list.
     * * @param ui The UI used for output. * @param storage The storage to update
     * the local file.
     */
    private static void handleMarkStatus(String input, TaskList tasks,
            Ui ui, Storage storage) {

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

            assert index >= 0 && index < tasks.size()
                    : "index should be valid after range check";

            Task t = tasks.get(index);
            assert t != null : "TaskList.get(index) should not return null";

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

    /**
     * * Processes the 'delete' command to remove a task. * * @param input The raw
     * user input. * @param tasks The current task list. * @param ui The UI used for
     * output. * @param storage The storage to update the local file.
     */
    private static void handleDelete(String input, TaskList tasks,
            Ui ui, Storage storage) {

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

            assert index >= 0 && index < tasks.size()
                    : "index should be valid after range check";

            Task removed = tasks.delete(index);
            assert removed != null
                    : "delete(index) should return the removed task";

            storage.save(tasks.getTasks());
            ui.showDeletedTask(removed, tasks.size());

        } catch (NumberFormatException e) {
            ui.showError("Invalid task number format.");
        }
    }

    /**
     * * Processes commands for adding Todos, Deadlines, and Events. * * @param
     * input The raw user input. * @param tasks The current task list. * @param ui
     * The UI used for output. * @param storage The storage to update the local
     * file.
     */
    private static void handleAddTask(String input, TaskList tasks,
            Ui ui, Storage storage) {

        try {
            Task newTask = null;

            if (input.startsWith("todo")) {

                String desc = input.substring(4).trim();
                assert desc != null : "todo description substring should not be null";

                if (desc.isEmpty()) {
                    throw new IllegalArgumentException(
                            "The description of a todo cannot be empty!");
                }

                newTask = new ToDos(desc);

            } else if (input.startsWith("deadline")) {

                String[] parts = input.substring(8).split(" /by ", 2);
                assert parts.length == 2
                        : "deadline should contain ' /by '";

                newTask = new Deadlines(
                        parts[0].trim(),
                        LocalDateTime.parse(parts[1].trim(), INPUT_FORMAT));

            } else if (input.startsWith("event")) {

                String[] parts = input.substring(5)
                        .split(" /from | /to ");

                assert parts.length == 3
                        : "event should contain ' /from ' and ' /to '";

                newTask = new Events(
                        parts[0].trim(),
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

    private static void handleFind(String input, TaskList tasks, Ui ui) {

        String[] parts = input.split(" ", 2);

        if (parts.length < 2 || parts[1].trim().isEmpty()) {
            ui.showError("You must provide a keyword to find.");
            return;
        }

        assert parts.length == 2
                : "find command should contain a keyword";

        String keyword = parts[1].trim();
        TaskList matchingTasks = tasks.findTasks(keyword);

        assert matchingTasks != null
                : "findTasks should never return null";

        ui.showSearchResults(matchingTasks);
    }

    /**
     * * Handles user commands for GUI usage. * Returns the response as a String
     * instead of printing to console. * * @param input User input * @param tasks
     * Task list * @param storage Storage manager * @return Response string for GUI
     */
    public static String handleCommandForGui(String input,
            TaskList tasks,
            Storage storage) {

        assert input != null : "input should not be null";
        assert tasks != null : "tasks should not be null";
        assert storage != null : "storage should not be null";

        Ui ui = new Ui();
        ui.clearMessages();
        handleCommand(input, tasks, ui, storage);
        return ui.getAllMessages();
    }
}
