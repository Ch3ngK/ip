package vex;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a list of tasks.
 * Provides methods to manipulate the list such as adding, deleting, and
 * retrieving tasks.
 */
public class TaskList {

    private final ArrayList<Task> tasks;

    /**
     * Constructs a TaskList with an existing list of tasks.
     *
     * @param tasks The initial list of tasks
     * @throws IllegalArgumentException If tasks is null
     */
    public TaskList(ArrayList<Task> tasks) {
        if (tasks == null) {
            throw new IllegalArgumentException("tasks must not be null");
        }
        this.tasks = tasks;
        assert this.tasks != null : "tasks list should not be null after initialization";
    }

    /**
     * Constructs an empty TaskList.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
        assert this.tasks != null : "tasks list should not be null after initialization";
    }

    /**
     * Adds a task to the list.
     *
     * @param task The task to be added
     * @throws IllegalArgumentException If task is null
     */
    public void add(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("task must not be null");
        }
        tasks.add(task);
    }

    /**
     * Deletes a task from the list at the specified index.
     *
     * @param index The index of the task to be removed
     * @return The task that was removed
     * @throws IndexOutOfBoundsException If the index is out of range
     */
    public Task delete(int index) {
        assert index >= 0 && index < tasks.size() : "delete index out of range: " + index;
        Task removed = tasks.remove(index);
        assert removed != null : "removed task should not be null";
        return removed;
    }

    /**
     * Retrieves a task from the list at the specified index.
     *
     * @param index The index of the task to retrieve
     * @return The task at the specified index
     * @throws IndexOutOfBoundsException If the index is out of range
     */
    public Task get(int index) {
        assert index >= 0 && index < tasks.size() : "get index out of range: " + index;
        Task task = tasks.get(index);
        assert task != null : "stored task should not be null";
        return task;
    }

    /**
     * Returns the number of tasks in the list.
     *
     * @return The size of the task list
     */
    public int size() {
        assert tasks != null : "tasks list should not be null";
        return tasks.size();
    }

    /**
     * Returns true if there are no tasks in the list.
     *
     * @return true if the list is empty
     */
    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    /**
     * Returns an unmodifiable view of the underlying list of tasks.
     *
     * @return A read-only view containing all tasks
     */
    public List<Task> getTasks() {
        return Collections.unmodifiableList(tasks);
    }

    /**
     * Finds and returns tasks whose string representation contains the given
     * keyword.
     *
     * @param keyword The keyword used to search for matching tasks
     * @return A TaskList containing all tasks that match the keyword
     * @throws IllegalArgumentException If keyword is null
     */
    public TaskList findTasks(String keyword) {
        if (keyword == null) {
            throw new IllegalArgumentException("keyword must not be null");
        }
        assert tasks != null : "tasks list should not be null";

        TaskList matchingTasks = new TaskList();

        tasks.stream()
                .filter(task -> task.toString().contains(keyword))
                .forEach(matchingTasks::add);

        return matchingTasks;
    }

    /**
     * Gets tasks that are due or starting within the specified number of days from today.
     *
     * @param days Number of days from today to look ahead (inclusive)
     * @return A TaskList of matching deadline and event tasks
     */
    public TaskList getReminders(int days) {
        if (days < 0)
            throw new IllegalArgumentException("days must be non-negative!");
        LocalDate today = LocalDate.now();

        TaskList result = new TaskList();
        tasks.stream()
                .filter(t -> t.isDueWithin(today, days))
                .forEach(result::add);
        return result;
    }

}
