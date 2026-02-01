package vex;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Handles the user interface of the application.
 * Responsible for displaying messages, errors, and task information to the
 * user.
 */
public class Ui {
    /** A decorative line used to separate message blocks. */
    private final String LINE = "____________________________________________________________";

    /**
     * Prints a decorative line separator.
     */
    public void showLine() {
        System.out.println(LINE);
    }

    /**
     * Prints the welcome greeting to the user.
     */
    public void showGreeting() {
        System.out.println(LINE);
        System.out.println("Hello! I'm Vex");
        System.out.println("What can I do for you?");
        System.out.println(LINE);
    }

    /**
     * Prints the goodbye message when the application terminates.
     */
    public void showBye() {
        System.out.println(LINE);
        System.out.println("Bye. Hope to see you again soon!");
        System.out.println(LINE);
    }

    /**
     * Displays an error message to the user.
     *
     * @param message The error message to be displayed.
     */
    public void showError(String message) {
        System.out.println(LINE);
        System.out.println(" Oh no! " + message);
        System.out.println(LINE);
    }

    /**
     * Displays all tasks currently in the task list.
     *
     * @param tasks The TaskList containing tasks to be shown.
     */
    public void showTaskList(TaskList tasks) {
        System.out.println(LINE);
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + "." + tasks.get(i));
        }
        System.out.println(LINE);
    }

    /**
     * Confirms to the user that a task has been marked as completed.
     *
     * @param t The task that was marked.
     */
    public void showMarkedTask(Task t) {
        System.out.println(LINE);
        System.out.println("Nice! I've marked this task as done:");
        System.out.println(t);
        System.out.println(LINE);
    }

    /**
     * Confirms to the user that a task has been marked as incomplete.
     *
     * @param t The task that was unmarked.
     */
    public void showUnmarkedTask(Task t) {
        System.out.println(LINE);
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println(t);
        System.out.println(LINE);
    }

    /**
     * Confirms that a task has been successfully added and shows the new total.
     *
     * @param t    The task that was added.
     * @param size The current number of tasks in the list.
     */
    public void showAddedTask(Task t, int size) {
        System.out.println(LINE);
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + t);
        System.out.println("Now you have " + size + " tasks in the list.");
        System.out.println(LINE);
    }

    /**
     * Confirms that a task has been removed and shows the new total.
     *
     * @param t    The task that was removed.
     * @param size The current number of tasks remaining in the list.
     */
    public void showDeletedTask(Task t, int size) {
        System.out.println(LINE);
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + t);
        System.out.println("Now you have " + size + " tasks in the list.");
        System.out.println(LINE);
    }

    /**
     * Displays tasks that occur on a specific date.
     *
     * @param tasks     The TaskList to search through.
     * @param queryDate The date for which to filter tasks.
     */
    public void showTasksOnDate(TaskList tasks, LocalDate queryDate) {
        System.out.println(LINE);
        System.out.println("Tasks on " + queryDate.format(DateTimeFormatter.ofPattern("MMM d yyyy")) + ":");
        boolean found = false;
        for (Task t : tasks.getTasks()) {
            if (t.occursOn(queryDate)) {
                System.out.println(t);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No tasks found on this date.");
        }
        if (!found)
            System.out.println("No tasks found on this date.");
        System.out.println(LINE);
    }

    public void showSearchResults(TaskList matchingTasks) {
        showLine();
        if (matchingTasks.size() == 0) {
            System.out.println("No matching tasks found in your list!");
        } else {
            System.out.println("Here are the matching tasks in your list:");
            for (int i = 0; i < matchingTasks.size(); i++) {
                System.out.println(" " + (i + 1) + "." + matchingTasks.get(i));
            }
        }
        showLine();
    }
}
