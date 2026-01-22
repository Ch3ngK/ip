import java.util.*;
public class Vex {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String name = "Vex";
        ArrayList<Task> arr = new ArrayList<>(); //create a new arraylist storing the tasks completed

        System.out.println("Hello! I'm " + name);
        System.out.println("What can I do for you?");
        System.out.println("____________________________________________________________");

        while (true) {
            String input = sc.nextLine().trim();
            if (input.isEmpty()) continue;

            if (input.equalsIgnoreCase("bye")) {
                System.out.println("____________________________________________________________");
                System.out.println("Bye. Hope to see you again soon!");
                System.out.println("____________________________________________________________");
                break;
            }

            if (input.equalsIgnoreCase("list")) {
                System.out.println("____________________________________________________________");
                System.out.println("Here are the tasks in your list: ");
                for (int i = 0; i < arr.size(); i++) {
                    System.out.println((i + 1) + "." + arr.get(i).toString());
                }
                System.out.println("____________________________________________________________");
                continue;
            }

            if (input.startsWith("mark") || input.startsWith("unmark")) {
                String[] parts = input.split(" ");
                if (parts.length < 2) {
                    showError("You did not specify a task number to " + parts[0] + ". Remember to do so!");
                    continue;
                }
                try {
                    int taskNumber = Integer.parseInt(parts[1]);
                    if (taskNumber < 1 || taskNumber > arr.size()) {
                        showError("Task number out of range.");
                        continue;
                    }
                    Task t = arr.get(taskNumber - 1);
                    if (parts[0].equals("mark")) {
                        t.markAsDone();
                        System.out.println("____________________________________________________________");
                        System.out.println("Nice! I've marked this task as done:");
                        System.out.println(t.toString());
                        System.out.println("____________________________________________________________");
                        continue;
                    } else if (parts[0].equals("unmark")) {
                        t.markAsUndone();
                        System.out.println("____________________________________________________________");
                        System.out.println("OK, I've marked this task as not done yet:");
                        System.out.println(t.toString());
                        System.out.println("____________________________________________________________");
                        continue;
                    }
                } catch (NumberFormatException e) {
                    showError("Invalid task number format");
                }
                continue;
            }

            Task newTask;
            if (input.startsWith("todo")) {
                String description = "";
                if (input.length() > 5) {
                    description = input.substring(5).trim(); // safely get text after "todo "
                }
                if (description.isEmpty()) {
                    showError("The description of a todo cannot be empty! Remember to fill it up! :-)");
                    continue;
                }
                newTask = new ToDos(description);
            }
            else if (input.startsWith("deadline")) {
                if (!input.contains("/by")) {
                    showError("Deadline command must have /by followed by a date/time! :-)");
                    continue;
                }
                String[] parts = input.substring(9).split(" /by ",2);
                if (parts.length < 2 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty()) {
                    showError("The description or times of an event is empty. Remember to fill it up! :-)");
                    continue;
                }
                String description = parts[0];
                String by = parts[1];
                newTask = new Deadlines(description, by);
            }
            else if (input.startsWith("event")) {
                if (!input.contains("/from") || !input.contains("/to")) { // <-- EDITED: validate format
                    showError("Event command must have /from and /to with times! :-)");
                    continue;
                }
                String[] parts = input.substring(6).split(" /from | /to ");
                if (parts.length < 3 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty() || parts[2].trim().isEmpty()) {
                    showError("The description or times of an event is empty. Remember to fill it up!");
                    continue;
                }
                String description = parts[0];
                String from = parts[1];
                String to = parts[2];
                newTask = new Events(description, from, to);
            }
            else {
                showError("I apologise, but I am unsure of what that means. Care to edit your message? :-(");
                continue;
            }

            arr.add(newTask);
            System.out.println("____________________________________________________________");
            System.out.println("Got it. I've added this task:");
            System.out.println("  " + newTask.toString());
            System.out.println("Now you have " + arr.size() + " tasks in the list.");
            System.out.println("____________________________________________________________");


        }
        sc.close();

    }
    //Helper methods
    private static void showError(String message) {
        System.out.println("____________________________________________________________");
        System.out.println(" Oh no! " + message);
        System.out.println("____________________________________________________________");
    }

}

class Task {
    protected String description;
    protected boolean isDone;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public String getStatusIcon() {
        return (isDone ? "X" : " ");
    }

    public void complete() {
        this.isDone = true;
    }

    public void incomplete() {
        this.isDone = false;
    }

    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + this.description;
    }

    public void markAsDone() {
        this.complete();
    }

    public void markAsUndone() {
        this.incomplete();
    }
}

class ToDos extends Task {

    public ToDos(String description) {
        super(description);
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}

class Deadlines extends Task {
    protected String by;

    public Deadlines(String description, String by) {
        super(description);
        this.by = by;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by + ")";
    }
}

class Events extends Task {
    protected String from;
    protected String to;

    public Events(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }
}

