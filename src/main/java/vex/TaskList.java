package vex;

import java.util.ArrayList;

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
     * @param tasks The initial list of tasks.
     */
    public TaskList(ArrayList<Task> tasks) {
        assert tasks != null : "tasks list should not be null";
        this.tasks = tasks;
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
     * @param t The task to be added.
     */
    public void add(Task t) {
        assert t != null : "cannot add null task";
        tasks.add(t);
    }

    /**
     * Deletes a task from the list at the specified index.
     *
     * @param index The index of the task to be removed.
     * @return The task that was removed.
     * @throws IndexOutOfBoundsException If the index is out of range.
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
     * @param index The index of the task to retrieve.
     * @return The task at the specified index.
     * @throws IndexOutOfBoundsException If the index is out of range.
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
     * @return The size of the task list.
     */
    public int size() {
        assert tasks != null : "tasks list should not be null";
        return tasks.size();
    }

    /**
     * Returns the underlying list of tasks.
     *
     * @return An ArrayList containing all tasks.
     */
    public ArrayList<Task> getTasks() {
        assert tasks != null : "tasks list should not be null";
        return tasks;
    }

    /**
     * Finds and returns tasks whose string representation contains the given
     * keyword.
     *
     * @param keyword The keyword used to search for matching tasks.
     * @return A TaskList containing all tasks that match the keyword.
     */
    public TaskList findTasks(String keyword) {
        assert keyword != null : "keyword should not be null";

        TaskList matchingTasks = new TaskList();

        for (Task task : tasks) {
            assert task != null : "stored task should not be null";
            if (task.toString().contains(keyword)) {
                matchingTasks.add(task);
            }
        }

        return matchingTasks;
    }
}
