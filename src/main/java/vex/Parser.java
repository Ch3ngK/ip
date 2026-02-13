package vex;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Parses user input into commands and delegates execution
 * to TaskList, Ui, and Storage.
 *
 * This class acts as the command-processing layer between
 * user input and application logic.
 */
public class Parser {

    /**
     * Expected format for date-time input: yyyy-MM-dd HHmm.
     */
    public static final DateTimeFormatter INPUT_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");

    private static final String COMMAND_LIST = "list";
    private static final String COMMAND_SHOW = "show";
    private static final String COMMAND_MARK = "mark";
    private static final String COMMAND_UNMARK = "unmark";
    private static final String COMMAND_DELETE = "delete";
    private static final String COMMAND_TODO = "todo";
    private static final String COMMAND_DEADLINE = "deadline";
    private static final String COMMAND_EVENT = "event";
    private static final String COMMAND_FIND = "find";

    private static final String DEADLINE_DELIMITER = " /by ";
    private static final String EVENT_FROM_DELIMITER = " /from ";
    private static final String EVENT_TO_DELIMITER = " /to ";

    private static final int TODO_PREFIX_LENGTH = 4;
    private static final int DEADLINE_PREFIX_LENGTH = 8;
    private static final int EVENT_PREFIX_LENGTH = 5;

    /**
     * Processes user input and executes the corresponding command.
     *
     * @param input   Raw user input string
     * @param tasks   TaskList to be modified or queried
     * @param ui      Ui instance for displaying feedback
     * @param storage Storage instance for persisting changes
     */
    public static void handleCommand(String input,
            TaskList tasks,
            Ui ui,
            Storage storage) {

        if (input.equalsIgnoreCase(COMMAND_LIST)) {
            ui.showTaskList(tasks);
            return;
        }

        if (input.startsWith(COMMAND_SHOW)) {
            handleShow(input, tasks, ui);
            return;
        }

        if (input.startsWith(COMMAND_MARK) || input.startsWith(COMMAND_UNMARK)) {
            handleMarkStatus(input, tasks, ui, storage);
            return;
        }

        if (input.startsWith(COMMAND_DELETE)) {
            handleDelete(input, tasks, ui, storage);
            return;
        }

        if (input.startsWith(COMMAND_TODO)
                || input.startsWith(COMMAND_DEADLINE)
                || input.startsWith(COMMAND_EVENT)) {
            handleAddTask(input, tasks, ui, storage);
            return;
        }

        if (input.startsWith(COMMAND_FIND)) {
            handleFind(input, tasks, ui);
            return;
        }

        ui.showError("I apologise, but I am unsure of what that means. Care to edit your message? :-(");
    }

    /**
     * Handles the 'show' command to display tasks on a specific date.
     *
     * @param input Raw user input
     * @param tasks TaskList to query
     * @param ui    Ui for output
     */
    private static void handleShow(String input, TaskList tasks, Ui ui) {
        String[] parts = input.split(" ", 2);

        if (parts.length < 2 || parts[1].trim().isEmpty()) {
            ui.showError("You must provide a date in yyyy-MM-dd format.");
            return;
        }

        try {
            LocalDate queryDate = LocalDate.parse(parts[1].trim());
            ui.showTasksOnDate(tasks, queryDate);
        } catch (DateTimeParseException e) {
            ui.showError("Invalid date format. Use yyyy-MM-dd.");
        }
    }

    /**
     * Handles 'mark' and 'unmark' commands to update task completion status.
     *
     * @param input   Raw user input
     * @param tasks   TaskList to modify
     * @param ui      Ui for output
     * @param storage Storage to persist changes
     */
    private static void handleMarkStatus(String input,
            TaskList tasks,
            Ui ui,
            Storage storage) {

        String[] parts = input.split(" ");

        if (parts.length < 2) {
            ui.showError("You must specify a task number.");
            return;
        }

        try {
            int index = Integer.parseInt(parts[1]) - 1;

            if (index < 0 || index >= tasks.size()) {
                ui.showError("Task number out of range.");
                return;
            }

            Task task = tasks.get(index);

            if (parts[0].equals(COMMAND_MARK)) {
                task.markAsDone();
                ui.showMarkedTask(task);
            } else {
                task.markAsUndone();
                ui.showUnmarkedTask(task);
            }

            storage.save(tasks.getTasks());

        } catch (NumberFormatException e) {
            ui.showError("Invalid task number format.");
        }
    }

    /**
     * Handles the 'delete' command to remove a task.
     *
     * @param input   Raw user input
     * @param tasks   TaskList to modify
     * @param ui      Ui for output
     * @param storage Storage to persist changes
     */
    private static void handleDelete(String input,
            TaskList tasks,
            Ui ui,
            Storage storage) {

        String[] parts = input.split(" ", 2);

        if (parts.length < 2) {
            ui.showError("You must specify a task number.");
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

    /**
     * Handles task creation commands (todo, deadline, event).
     *
     * @param input   Raw user input
     * @param tasks   TaskList to modify
     * @param ui      Ui for output
     * @param storage Storage to persist changes
     */
    private static void handleAddTask(String input,
            TaskList tasks,
            Ui ui,
            Storage storage) {

        try {
            Task newTask = parseTaskFromInput(input);

            tasks.add(newTask);
            storage.save(tasks.getTasks());
            ui.showAddedTask(newTask, tasks.size());

        } catch (IllegalArgumentException | DateTimeParseException e) {
            ui.showError(e.getMessage());
        }
    }

    /**
     * Parses a task creation command into a Task object.
     *
     * @param input Raw user input
     * @return A Task object based on the command
     * @throws IllegalArgumentException If format is invalid
     */
    private static Task parseTaskFromInput(String input) {
        if (input.startsWith(COMMAND_TODO)) {
            return parseTodo(input);
        }

        if (input.startsWith(COMMAND_DEADLINE)) {
            return parseDeadline(input);
        }

        if (input.startsWith(COMMAND_EVENT)) {
            return parseEvent(input);
        }

        throw new IllegalArgumentException("Invalid format for adding a task.");
    }

    /**
     * Parses a todo command.
     *
     * @param input Raw user input
     * @return A ToDos task
     */
    private static Task parseTodo(String input) {
        String desc = input.substring(TODO_PREFIX_LENGTH).trim();

        if (desc.isEmpty()) {
            throw new IllegalArgumentException("The description of a todo cannot be empty.");
        }

        return new ToDos(desc);
    }

    /**
     * Parses a deadline command.
     *
     * @param input Raw user input
     * @return A Deadlines task
     */
    private static Task parseDeadline(String input) {
        String payload = input.substring(DEADLINE_PREFIX_LENGTH).trim();
        String[] parts = payload.split(DEADLINE_DELIMITER, 2);

        if (parts.length < 2) {
            throw new IllegalArgumentException(
                    "Invalid deadline format. Use: deadline <desc> /by yyyy-MM-dd HHmm");
        }

        LocalDateTime by = LocalDateTime.parse(parts[1].trim(), INPUT_FORMAT);
        return new Deadlines(parts[0].trim(), by);
    }

    /**
     * Parses an event command.
     *
     * @param input Raw user input
     * @return An Events task
     */
    private static Task parseEvent(String input) {
        String payload = input.substring(EVENT_PREFIX_LENGTH).trim();

        int fromIndex = payload.indexOf(EVENT_FROM_DELIMITER);
        int toIndex = payload.indexOf(EVENT_TO_DELIMITER);

        if (fromIndex < 0 || toIndex < 0 || toIndex <= fromIndex) {
            throw new IllegalArgumentException(
                    "Invalid event format. Use: event <desc> /from yyyy-MM-dd HHmm /to yyyy-MM-dd HHmm");
        }

        String desc = payload.substring(0, fromIndex).trim();
        String fromString = payload.substring(fromIndex + EVENT_FROM_DELIMITER.length(), toIndex).trim();
        String toString = payload.substring(toIndex + EVENT_TO_DELIMITER.length()).trim();

        LocalDateTime from = LocalDateTime.parse(fromString, INPUT_FORMAT);
        LocalDateTime to = LocalDateTime.parse(toString, INPUT_FORMAT);

        return new Events(desc, from, to);
    }

    /**
     * Handles the 'find' command to search for matching tasks.
     *
     * @param input Raw user input
     * @param tasks TaskList to search
     * @param ui    Ui for output
     */
    private static void handleFind(String input, TaskList tasks, Ui ui) {
        String[] parts = input.split(" ", 2);

        if (parts.length < 2 || parts[1].trim().isEmpty()) {
            ui.showError("You must provide a keyword to find.");
            return;
        }

        TaskList matchingTasks = tasks.findTasks(parts[1].trim());
        ui.showSearchResults(matchingTasks);
    }

    /**
     * Processes commands for GUI usage and returns the result as a String.
     *
     * @param input   User input
     * @param tasks   TaskList to modify or query
     * @param storage Storage manager
     * @return Aggregated UI response string
     */
    public static String handleCommandForGui(String input,
            TaskList tasks,
            Storage storage) {

        Ui ui = new Ui();
        ui.clearMessages();
        handleCommand(input, tasks, ui, storage);
        return ui.getAllMessages();
    }
}
