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
    /** A decorative line used to separate message blocks. */
    private final String line = "____________________________________________________________";

    /** Stores messages for GUI output. */
    private final List<String> messages;

    /**
     * Constructs a new Ui instance.
     * Initializes the messages list used to store output for GUI purposes.
     */
    public Ui() {
        messages = new ArrayList<>();
    }

    /**
     * Adds a message to the message list and prints it to the console.
     * This method is used to centralize output handling for both console and GUI.
     *
     * @param message The message to display.
     */
    private void showMessage(String message) {
        messages.add(message); // store for GUI
        System.out.println(message); // keep console output
    }

    /**
     * Returns all messages generated since the last command as a single String.
     *
     * @return Combined response string
     */
    public String getAllMessages() {
        return String.join("\n", messages);
    }

    /** Prints a decorative line separator. */
    public void showLine() {
        showMessage(line);
    }

    /** Prints the welcome greeting to the user. */
    public void showGreeting() {
        showMessage(line);
        showMessage("Hello! I'm Vex");
        showMessage("What can I do for you?");
        showMessage(line);
    }

    /** Prints the goodbye message when the application terminates. */
    public void showBye() {
        showMessage(line);
        showMessage("Bye. Hope to see you again soon!");
        showMessage(line);
    }

    /**
     * Displays an error message to the user.
     *
     * @param message The error message to display.
     */
    public void showError(String message) {
        showMessage(line);
        showMessage(" Oh no! " + message);
        showMessage(line);
    }

    /**
     * Displays all tasks currently in the task list.
     *
     * @param tasks The TaskList containing tasks to show.
     */
    public void showTaskList(TaskList tasks) {
        showMessage(line);
        showMessage("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            showMessage((i + 1) + "." + tasks.get(i));
        }
        showMessage(line);
    }

    /**
     * Confirms to the user that a task has been marked as completed.
     *
     * @param t The task that was marked.
     */
    public void showMarkedTask(Task t) {
        showMessage(line);
        showMessage("Nice! I've marked this task as done:");
        showMessage(t.toString());
        showMessage(line);
    }

    /**
     * Confirms to the user that a task has been marked as incomplete.
     *
     * @param t The task that was unmarked.
     */
    public void showUnmarkedTask(Task t) {
        showMessage(line);
        showMessage("OK, I've marked this task as not done yet:");
        showMessage(t.toString());
        showMessage(line);
    }

    /**
     * Confirms that a task has been successfully added and shows the new total.
     *
     * @param t    The task that was added.
     * @param size The current number of tasks in the list.
     */
    public void showAddedTask(Task t, int size) {
        showMessage(line);
        showMessage("Got it. I've added this task:");
        showMessage("  " + t);
        showMessage("Now you have " + size + " tasks in the list.");
        showMessage(line);
    }

    /**
     * Confirms that a task has been removed and shows the new total.
     *
     * @param t    The task that was removed.
     * @param size The current number of tasks remaining in the list.
     */
    public void showDeletedTask(Task t, int size) {
        showMessage(line);
        showMessage("Noted. I've removed this task:");
        showMessage("  " + t);
        showMessage("Now you have " + size + " tasks in the list.");
        showMessage(line);
    }

    /**
     * Displays tasks that occur on a specific date.
     *
     * @param tasks     The TaskList to search through.
     * @param queryDate The date for which to filter tasks.
     */
    public void showTasksOnDate(TaskList tasks, LocalDate queryDate) {
        showMessage(line);
        showMessage("Tasks on " + queryDate.format(DateTimeFormatter.ofPattern("MMM d yyyy")) + ":");
        boolean found = false;
        for (Task t : tasks.getTasks()) {
            if (t.occursOn(queryDate)) {
                showMessage(t.toString());
                found = true;
            }
        }
        if (!found) {
            showMessage("No tasks found on this date.");
        }
        showMessage(line);
    }

    /**
     * Displays the tasks that match a search keyword.
     * Prints a message if no matching tasks are found.
     *
     * @param matchingTasks The TaskList containing matching tasks.
     */
    public void showSearchResults(TaskList matchingTasks) {
        showLine();
        if (matchingTasks.size() == 0) {
            showMessage("No matching tasks found in your list!");
        } else {
            showMessage("Here are the matching tasks in your list:");
            for (int i = 0; i < matchingTasks.size(); i++) {
                showMessage(" " + (i + 1) + "." + matchingTasks.get(i));
            }
        }
        showLine();
    }

    /**
     * Clears all stored messages.
     * Should be called before processing a new GUI command.
     */
    public void clearMessages() {
        messages.clear();
    }

}
