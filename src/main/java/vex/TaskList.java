package vex;

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
     * @param tasks The initial list of tasks.
     */
    public TaskList(ArrayList<Task> tasks) {
        if (tasks == null) {
            throw new IllegalArgumentException("tasks must not be null");
        }
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
     * @param task The task to be added.
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
     * Returns an unmodifiable view of the underlying list of tasks.
     *
     * @return A read-only view containing all tasks.
     */
    public List<Task> getTasks() {
        return Collections.unmodifiableList(tasks);
    }

    /**
     * Finds and returns tasks whose string representation contains the given
     * keyword.
     *
     * @param keyword The keyword used to search for matching tasks.
     * @return A TaskList containing all tasks that match the keyword.
     */
    public TaskList findTasks(String keyword) {
        if (keyword == null) {
            throw new IllegalArgumentException("keyword must not be null");
        }

        TaskList matchingTasks = new TaskList();
        for (Task task : tasks) {
            if (task.toString().contains(keyword)) {
                matchingTasks.add(task);
            }
        }
        return matchingTasks;
    }
}
