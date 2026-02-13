package vex;

import java.util.ArrayList;

/**
 * Represents a list of tasks.
 * Provides methods to manipulate the list such as adding, deleting, and
 * retrieving tasks.
 */
public class TaskList {
    private ArrayList<Task> tasks;

    /**
     * Constructs a TaskList with an existing list of tasks.
     *
     * @param tasks The initial list of tasks.
     */
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * Constructs an empty TaskList.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Adds a task to the list.
     *
     * @param t The task to be added.
     */
    public void add(Task t) {
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
        return tasks.remove(index);
    }

    /**
     * Retrieves a task from the list at the specified index.
     *
     * @param index The index of the task to retrieve.
     * @return The task at the specified index.
     * @throws IndexOutOfBoundsException If the index is out of range.
     */
    public Task get(int index) {
        return tasks.get(index);
    }

    /**
     * Returns the number of tasks in the list.
     *
     * @return The size of the task list.
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns the underlying list of tasks.
     *
     * @return An ArrayList containing all tasks.
     */
    public ArrayList<Task> getTasks() {
        return tasks;
    }

    /**
     * Returns a TaskList containing tasks whose string representation
     * contains the specified keyword.
     *
     * This method uses the Streams API to filter matching tasks
     * in a declarative manner.
     *
     * @param keyword The keyword used to search for matching tasks
     * @return A TaskList containing all matching tasks
     * @throws IllegalArgumentException if keyword is null
     */
    public TaskList findTasks(String keyword) {
        if (keyword == null) {
            throw new IllegalArgumentException("Keyword must not be null.");
        }

        TaskList matchingTasks = new TaskList();

        tasks.stream()
                .filter(task -> task.toString().contains(keyword))
                .forEach(matchingTasks::add);

        return matchingTasks;
    }

}
