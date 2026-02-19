package vex;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Parses user input into commands and delegates execution to TaskList, Ui, and Storage.
 *
 * This class acts as the command-processing layer between user input and application logic.
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
    private static final String COMMAND_REMIND = "remind";

    private static final String DEADLINE_DELIMITER = " /by ";
    private static final String EVENT_FROM_DELIMITER = " /from ";
    private static final String EVENT_TO_DELIMITER = " /to ";

    private static final int DEFAULT_REMIND_DAYS = 7;

    private static final String ERROR_UNKNOWN_COMMAND =
            "The Ancient does not understand your orders. Try again, commander. "
            + "(Valid: list, show, mark, unmark, delete, todo, deadline, event, find, remind, bye)";
    private static final String ERROR_EMPTY_INPUT = "The battlefield awaits your command...";
    private static final String ERROR_TASK_NUMBER_INVALID = "That target does not exist in this lane.";
    private static final String ERROR_SHOW_NO_DATE = "State the date of battle. Use yyyy-MM-dd.";
    private static final String ERROR_SHOW_BAD_DATE = "Invalid date. The Ancient demands yyyy-MM-dd.";
    private static final String ERROR_NO_TASK_NUMBER = "Specify which objective. Give a task number.";
    private static final String ERROR_FIND_NO_KEYWORD = "What intel do you seek? Provide a keyword.";
    private static final String ERROR_REMIND_TOO_MANY = "One number only. Use: remind <days> (e.g., remind 3)";
    private static final String ERROR_REMIND_BAD_DAYS = "The timeline is unclear. Use: remind <days> (e.g., remind 3)";
    private static final String ERROR_REMIND_NEGATIVE = "Time does not flow backward. Days must be zero or more.";
    private static final String ERROR_SAVE_FAILED = "The campaign archives could not be written. Your changes were not saved.";

    /**
     * Processes user input and executes the corresponding command.
     *
     * @param input   Raw user input string
     * @param tasks   TaskList to be modified or queried
     * @param ui      Ui instance for displaying feedback
     * @param storage Storage instance for persisting changes
     */
    public static void handleCommand(String input, TaskList tasks, Ui ui, Storage storage) {
        if (tasks == null || ui == null || storage == null) {
            throw new IllegalArgumentException("tasks/ui/storage must not be null");
        }

        String trimmed = normalizeInput(input);
        if (trimmed.isEmpty()) {
            ui.showError(ERROR_EMPTY_INPUT);
            return;
        }

        String[] parts = trimmed.split("\\s+", 2);
        String command = parts[0].toLowerCase();
        String args = parts.length == 2 ? parts[1] : "";

        switch (command) {
        case COMMAND_LIST:
            ui.showTaskList(tasks);
            break;

        case COMMAND_SHOW:
            handleShow(args, tasks, ui);
            break;

        case COMMAND_MARK:
        case COMMAND_UNMARK:
            handleMarkStatus(command, args, tasks, ui, storage);
            break;

        case COMMAND_DELETE:
            handleDelete(args, tasks, ui, storage);
            break;

        case COMMAND_TODO:
        case COMMAND_DEADLINE:
        case COMMAND_EVENT:
            handleAddTask(command, args, tasks, ui, storage);
            break;

        case COMMAND_FIND:
            handleFind(args, tasks, ui);
            break;

        case COMMAND_REMIND:
            handleRemind(args, tasks, ui);
            break;

        default:
            ui.showError(ERROR_UNKNOWN_COMMAND);
            break;
        }
    }

    private static String normalizeInput(String input) {
        return trimToEmpty(input);
    }

    /** Returns empty string if s is null, otherwise s trimmed. */
    private static String trimToEmpty(String s) {
        return s == null ? "" : s.trim();
    }

    /**
     * Handles the 'show' command to display tasks on a specific date.
     *
     * @param args  Raw argument string (expected date)
     * @param tasks TaskList to query
     * @param ui    Ui for output
     */
    private static void handleShow(String args, TaskList tasks, Ui ui) {
        if (trimToEmpty(args).isEmpty()) {
            ui.showError(ERROR_SHOW_NO_DATE);
            return;
        }

        try {
            LocalDate queryDate = LocalDate.parse(trimToEmpty(args));
            ui.showTasksOnDate(tasks, queryDate);
        } catch (DateTimeParseException e) {
            ui.showError(ERROR_SHOW_BAD_DATE);
        }
    }

    /**
     * Handles 'mark' and 'unmark' commands to update task completion status.
     *
     * @param command Either "mark" or "unmark"
     * @param args    Raw argument string (expected task number)
     * @param tasks   TaskList to modify
     * @param ui      Ui for output
     * @param storage Storage to persist changes
     */
    private static void handleMarkStatus(String command, String args, TaskList tasks, Ui ui, Storage storage) {
        try {
            int index = parseTaskIndexOrThrow(args, tasks);
            Task task = tasks.get(index);

            if (COMMAND_MARK.equals(command)) {
                task.markAsDone();
                ui.showMarkedTask(task);
            } else {
                task.markAsUndone();
                ui.showUnmarkedTask(task);
            }

            if (!storage.save(tasks.getTasks())) {
                ui.showError(ERROR_SAVE_FAILED);
            }
        } catch (IllegalArgumentException e) {
            ui.showError(e.getMessage());
        }
    }

    /**
     * Handles the 'delete' command to remove a task.
     *
     * @param args    Raw argument string (expected task number)
     * @param tasks   TaskList to modify
     * @param ui      Ui for output
     * @param storage Storage to persist changes
     */
    private static void handleDelete(String args, TaskList tasks, Ui ui, Storage storage) {
        try {
            int index = parseTaskIndexOrThrow(args, tasks);
            Task removed = tasks.delete(index);

            if (!storage.save(tasks.getTasks())) {
                ui.showError(ERROR_SAVE_FAILED);
            }
            ui.showDeletedTask(removed, tasks.size());
        } catch (IllegalArgumentException e) {
            ui.showError(e.getMessage());
        }
    }

    private static int parseTaskIndexOrThrow(String raw, TaskList tasks) {
        if (trimToEmpty(raw).isEmpty()) {
            throw new IllegalArgumentException(ERROR_NO_TASK_NUMBER);
        }

        int index;
        try {
            index = Integer.parseInt(trimToEmpty(raw)) - 1;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(ERROR_TASK_NUMBER_INVALID);
        }

        if (index < 0 || index >= tasks.size()) {
            throw new IllegalArgumentException("You cannot attack what is not there!");
        }
        return index;
    }

    /**
     * Handles task creation commands (todo, deadline, event).
     *
     * @param command Command word
     * @param args    Rest of line after command
     * @param tasks   TaskList to modify
     * @param ui      Ui for output
     * @param storage Storage to persist changes
     */
    private static void handleAddTask(String command, String args, TaskList tasks, Ui ui, Storage storage) {
        try {
            Task newTask = parseTaskFromInput(command, args);

            tasks.add(newTask);
            if (!storage.save(tasks.getTasks())) {
                ui.showError(ERROR_SAVE_FAILED);
            }
            ui.showAddedTask(newTask, tasks.size());
        } catch (IllegalArgumentException | DateTimeParseException e) {
            ui.showError(e.getMessage());
        }
    }

    /**
     * Parses a task creation command into a Task object.
     *
     * @param command Command word
     * @param args    Rest of input after command
     * @return A Task object based on the command
     */
    private static Task parseTaskFromInput(String command, String args) {
        switch (command) {
        case COMMAND_TODO:
            return parseTodo(args);
        case COMMAND_DEADLINE:
            return parseDeadline(args);
        case COMMAND_EVENT:
            return parseEvent(args);
        default:
            throw new IllegalArgumentException("The Ancient does not recognize this objective format.");
        }
    }

    /**
     * Parses a todo command.
     *
     * @param args Rest of input after "todo"
     * @return A ToDos task
     */
    private static Task parseTodo(String args) {
        String desc = trimToEmpty(args);
        if (desc.isEmpty()) {
            throw new IllegalArgumentException("A quest without purpose? Even creeps have objectives.");
        }
        return new ToDos(desc);
    }

    /**
     * Parses a deadline command.
     *
     * Format: <desc> /by yyyy-MM-dd HHmm
     *
     * @param args Rest of input after "deadline"
     * @return A Deadlines task
     */
    private static Task parseDeadline(String args) {
        String payload = trimToEmpty(args);

        int delimiterIndex = payload.indexOf(DEADLINE_DELIMITER);
        if (delimiterIndex < 0) {
            throw new IllegalArgumentException("Your timeline is unclear. State it properly, or the battle is lost.");
        }

        String desc = payload.substring(0, delimiterIndex).trim();
        String byString = payload.substring(delimiterIndex + DEADLINE_DELIMITER.length()).trim();

        if (desc.isEmpty() || byString.isEmpty()) {
            throw new IllegalArgumentException("State the deadline clearly: deadline <desc> /by yyyy-MM-dd HHmm");
        }

        LocalDateTime by = LocalDateTime.parse(byString, INPUT_FORMAT);
        return new Deadlines(desc, by);
    }

    /**
     * Parses an event command.
     *
     * Format: <desc> /from yyyy-MM-dd HHmm /to yyyy-MM-dd HHmm
     *
     * @param args Rest of input after "event"
     * @return An Events task
     */
    private static Task parseEvent(String args) {
        String payload = trimToEmpty(args);

        int fromIndex = payload.indexOf(EVENT_FROM_DELIMITER);
        int toIndex = payload.indexOf(EVENT_TO_DELIMITER);

        if (fromIndex < 0 || toIndex < 0 || toIndex <= fromIndex) {
            throw new IllegalArgumentException(
                    "Your battle plan lacks clarity. Use: event <desc> /from yyyy-MM-dd HHmm /to yyyy-MM-dd HHmm");
        }

        String desc = payload.substring(0, fromIndex).trim();
        String fromString = payload.substring(fromIndex + EVENT_FROM_DELIMITER.length(), toIndex).trim();
        String toString = payload.substring(toIndex + EVENT_TO_DELIMITER.length()).trim();

        if (desc.isEmpty() || fromString.isEmpty() || toString.isEmpty()) {
            throw new IllegalArgumentException(
                    "Your battle plan lacks clarity. Use: event <desc> /from yyyy-MM-dd HHmm /to yyyy-MM-dd HHmm");
        }

        LocalDateTime from = LocalDateTime.parse(fromString, INPUT_FORMAT);
        LocalDateTime to = LocalDateTime.parse(toString, INPUT_FORMAT);

        if (to.isBefore(from)) {
            throw new IllegalArgumentException("Event end time must be after start time.");
        }

        return new Events(desc, from, to);
    }

    /**
     * Handles the 'find' command to search for matching tasks.
     *
     * @param args  Rest of input after "find"
     * @param tasks TaskList to search
     * @param ui    Ui for output
     */
    private static void handleFind(String args, TaskList tasks, Ui ui) {
        String keyword = trimToEmpty(args);
        if (keyword.isEmpty()) {
            ui.showError(ERROR_FIND_NO_KEYWORD);
            return;
        }

        TaskList matchingTasks = tasks.findTasks(keyword);
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
    public static String handleCommandForGui(String input, TaskList tasks, Storage storage) {
        if (tasks == null || storage == null) {
            throw new IllegalArgumentException("tasks/storage must not be null");
        }

        Ui ui = new Ui();
        ui.clearMessages();
        handleCommand(input, tasks, ui, storage);
        return ui.getAllMessages();
    }

    /**
     * Handles the 'remind' command to show upcoming deadlines/events.
     *
     * Formats:
     * - remind (defaults to 7 days)
     * - remind <days> (e.g., remind 3)
     *
     * @param args  Rest of input after "remind"
     * @param tasks TaskList to search
     * @param ui    Ui for output
     */
    private static void handleRemind(String args, TaskList tasks, Ui ui) {
        String raw = trimToEmpty(args);

        int days = DEFAULT_REMIND_DAYS;

        if (!raw.isEmpty()) {
            String[] parts = raw.split("\\s+");
            if (parts.length > 1) {
                ui.showError(ERROR_REMIND_TOO_MANY);
                return;
            }

            try {
                days = Integer.parseInt(parts[0]);
            } catch (NumberFormatException e) {
                ui.showError(ERROR_REMIND_BAD_DAYS);
                return;
            }
        }

        if (days < 0) {
            ui.showError(ERROR_REMIND_NEGATIVE);
            return;
        }

        TaskList reminders = tasks.getReminders(days);
        ui.showReminders(reminders, days);
    }
}
