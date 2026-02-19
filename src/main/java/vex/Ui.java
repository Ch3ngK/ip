package vex;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles the user interface of the application.
 * Responsible for displaying messages, errors, and task information to the
 * user.
 * Also stores messages for GUI integration.
 */
public class Ui {

    /** Date format for display (e.g. "Jan 15 2025"). */
    private static final DateTimeFormatter DISPLAY_DATE_FORMAT =
            DateTimeFormatter.ofPattern("MMM d yyyy");

    /** Stores messages for GUI output. */
    private final List<String> messages;

    /**
     * Constructs a new Ui instance.
     * Initializes the messages list used to store output for GUI purposes.
     */
    public Ui() {
        this.messages = new ArrayList<>();
    }

    /**
     * Adds a message to the message list and prints it to the console.
     *
     * @param message The message to display.
     */
    private void showMessage(String message) {
        messages.add(message);
        System.out.println(message);
    }

    /**
     * Returns all messages generated since the last command as a single String.
     *
     * @return Combined response string
     */
    public String getAllMessages() {
        return String.join("\n", messages);
    }

    /** Prints the welcome greeting to the user. */
    public void showGreeting() {
        showMessage("The Ancient stirs. I am Vex.");
        showMessage("What orders do you bring, commander?");
    }

    /** Prints the goodbye message when the application terminates. */
    public void showBye() {
        showMessage("The battle ends. Until we meet again in the dark.");
    }

    /**
     * Displays an error message to the user.
     *
     * @param message The error message to display.
     */
    public void showError(String message) {
        showMessage("The Ancient frowns: " + message);
    }

    /**
     * Displays all tasks currently in the task list.
     *
     * @param tasks The TaskList containing tasks to show.
     */
    public void showTaskList(TaskList tasks) {
        showMessage("Your campaign log, commander:");
        if (tasks.isEmpty()) {
            showMessage("No objectives yet. Use todo, deadline, or event to add some.");
        } else {
            showNumberedTaskList(tasks);
        }
    }

    /**
     * Confirms that a task has been marked as completed.
     *
     * @param task The task that was marked.
     */
    public void showMarkedTask(Task task) {
        showMessage("Objective complete. Marked as done:");
        showMessage(task.toString());
    }

    /**
     * Confirms that a task has been marked as incomplete.
     *
     * @param task The task that was unmarked.
     */
    public void showUnmarkedTask(Task task) {
        showMessage("Reopened. Marked as not done:");
        showMessage(task.toString());
    }

    /**
     * Confirms that a task has been successfully added and shows the new total.
     *
     * @param task The task that was added.
     * @param size The current number of tasks in the list.
     */
    public void showAddedTask(Task task, int size) {
        showMessage("Added to the roster:");
        showMessage("  " + task);
        showMessage("You now have " + size + " objective(s) in your campaign.");
    }

    /**
     * Confirms that a task has been removed and shows the new total.
     *
     * @param task The task that was removed.
     * @param size The current number of tasks remaining.
     */
    public void showDeletedTask(Task task, int size) {
        showMessage("Struck from the roster:");
        showMessage("  " + task);
        showMessage("You now have " + size + " objective(s) in your campaign.");
    }

    /**
     * Displays tasks that occur on a specific date.
     *
     * @param tasks     The TaskList to search through.
     * @param queryDate The date for which to filter tasks.
     */
    public void showTasksOnDate(TaskList tasks, LocalDate queryDate) {
        String formattedDate = queryDate.format(DISPLAY_DATE_FORMAT);

        showMessage("Objectives on " + formattedDate + ":");

        boolean found = false;
        for (Task task : tasks.getTasks()) {
            if (task.occursOn(queryDate)) {
                showMessage(task.toString());
                found = true;
            }
        }

        if (!found) {
            showMessage("No battles scheduled for this date.");
        }
    }

    /**
     * Displays the tasks that match a search keyword.
     *
     * @param matchingTasks The TaskList containing matching tasks.
     */
    public void showSearchResults(TaskList matchingTasks) {
        if (matchingTasks.isEmpty()) {
            showMessage("No objectives match that call.");
            return;
        }

        showMessage("Intel matching your search:");
        showNumberedTaskList(matchingTasks);
    }

    /**
     * Clears all stored messages.
     * Should be called before processing a new GUI command.
     */
    public void clearMessages() {
        messages.clear();
    }

    /**
     * Displays tasks that are due/starting within the next given number of days.
     *
     * @param reminders TaskList containing reminder tasks
     * @param days      Number of days to look ahead
     */
    public void showReminders(TaskList reminders, int days) {
        if (reminders.isEmpty()) {
            showMessage("No engagements in the next " + days + " day(s). The lane is clear.");
        } else {
            showMessage("Upcoming engagements in the next " + days + " day(s):");
            showNumberedTaskList(reminders);
        }
    }

    /**
     * Displays a task list as numbered lines (1. task, 2. task, ...).
     *
     * @param tasks The TaskList to display.
     */
    private void showNumberedTaskList(TaskList tasks) {
        for (int i = 0; i < tasks.size(); i++) {
            showMessage((i + 1) + ". " + tasks.get(i));
        }
    }

}
