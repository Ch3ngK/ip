package vex;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Ui {
    private final String LINE = "____________________________________________________________";

    public void showLine() {
        System.out.println(LINE);
    }

    public void showGreeting() {
        System.out.println(LINE);
        System.out.println("Hello! I'm Vex");
        System.out.println("What can I do for you?");
        System.out.println(LINE);
    }

    public void showBye() {
        System.out.println(LINE);
        System.out.println("Bye. Hope to see you again soon!");
        System.out.println(LINE);
    }

    public void showError(String message) {
        System.out.println(LINE);
        System.out.println(" Oh no! " + message);
        System.out.println(LINE);
    }

    public void showTaskList(TaskList tasks) {
        System.out.println(LINE);
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + "." + tasks.get(i));
        }
        System.out.println(LINE);
    }

    public void showMarkedTask(Task t) {
        System.out.println(LINE);
        System.out.println("Nice! I've marked this task as done:");
        System.out.println(t);
        System.out.println(LINE);
    }

    public void showUnmarkedTask(Task t) {
        System.out.println(LINE);
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println(t);
        System.out.println(LINE);
    }

    public void showAddedTask(Task t, int size) {
        System.out.println(LINE);
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + t);
        System.out.println("Now you have " + size + " tasks in the list.");
        System.out.println(LINE);
    }

    public void showDeletedTask(Task t, int size) {
        System.out.println(LINE);
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + t);
        System.out.println("Now you have " + size + " tasks in the list.");
        System.out.println(LINE);
    }

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
        if (!found)
            System.out.println("No tasks found on this date.");
        System.out.println(LINE);
    }
}
