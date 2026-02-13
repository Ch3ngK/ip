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
        showMessage("Hello! I'm Vex.");
        showMessage("What can I do for you?");
    }

    /** Prints the goodbye message when the application terminates. */
    public void showBye() {
        showMessage("Bye. Hope to see you again soon!");
    }

    /**
     * Displays an error message to the user.
     *
     * @param message The error message to display.
     */
    public void showError(String message) {
        showMessage("Error: " + message);
    }

    /**
     * Displays all tasks currently in the task list.
     *
     * @param tasks The TaskList containing tasks to show.
     */
    public void showTaskList(TaskList tasks) {
        showMessage("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            showMessage((i + 1) + ". " + tasks.get(i));
        }
    }

    /**
     * Confirms that a task has been marked as completed.
     *
     * @param task The task that was marked.
     */
    public void showMarkedTask(Task task) {
        showMessage("Nice! I've marked this task as done:");
        showMessage(task.toString());
    }

    /**
     * Confirms that a task has been marked as incomplete.
     *
     * @param task The task that was unmarked.
     */
    public void showUnmarkedTask(Task task) {
        showMessage("OK, I've marked this task as not done yet:");
        showMessage(task.toString());
    }

    /**
     * Confirms that a task has been successfully added and shows the new total.
     *
     * @param task The task that was added.
     * @param size The current number of tasks in the list.
     */
    public void showAddedTask(Task task, int size) {
        showMessage("Got it. I've added this task:");
        showMessage("  " + task);
        showMessage("Now you have " + size + " tasks in the list.");
    }

    /**
     * Confirms that a task has been removed and shows the new total.
     *
     * @param task The task that was removed.
     * @param size The current number of tasks remaining.
     */
    public void showDeletedTask(Task task, int size) {
        showMessage("Noted. I've removed this task:");
        showMessage("  " + task);
        showMessage("Now you have " + size + " tasks in the list.");
    }

    /**
     * Displays tasks that occur on a specific date.
     *
     * @param tasks     The TaskList to search through.
     * @param queryDate The date for which to filter tasks.
     */
    public void showTasksOnDate(TaskList tasks, LocalDate queryDate) {
        String formattedDate = queryDate.format(DateTimeFormatter.ofPattern("MMM d yyyy"));

        showMessage("Tasks on " + formattedDate + ":");

        boolean found = false;
        for (Task task : tasks.getTasks()) {
            if (task.occursOn(queryDate)) {
                showMessage(task.toString());
                found = true;
            }
        }

        if (!found) {
            showMessage("No tasks found on this date.");
        }
    }

    /**
     * Displays the tasks that match a search keyword.
     *
     * @param matchingTasks The TaskList containing matching tasks.
     */
    public void showSearchResults(TaskList matchingTasks) {
        if (matchingTasks.size() == 0) {
            showMessage("No matching tasks found in your list!");
            return;
        }

        showMessage("Here are the matching tasks in your list:");
        for (int i = 0; i < matchingTasks.size(); i++) {
            showMessage((i + 1) + ". " + matchingTasks.get(i));
        }
    }

    /**
     * Clears all stored messages.
     * Should be called before processing a new GUI command.
     */
    public void clearMessages() {
        messages.clear();
    }
}
