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
            String input = sc.nextLine();
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
                    System.out.println((i + 1) + "." + arr.get(i).getStatus());
                }
                System.out.println("____________________________________________________________");
                continue;
            }

            if (input.startsWith("mark")) {
                int taskNumber = Integer.parseInt(input.split(" ")[1]);
                Task t = arr.get(taskNumber - 1);
                t.markAsDone();
                System.out.println("____________________________________________________________");
                System.out.println("Nice! I've marked this task as done:");
                System.out.println(t.getStatus());
                System.out.println("____________________________________________________________");
                continue;
            }
            if (input.startsWith("unmark")) {
                int taskNumber = Integer.parseInt(input.split(" ")[1]);
                Task t = arr.get(taskNumber - 1);
                t.markAsUndone();
                System.out.println("____________________________________________________________");
                System.out.println("OK, I've marked this task as not done yet:");
                System.out.println(t.getStatus());
                System.out.println("____________________________________________________________");
                continue;
            }

            System.out.println("____________________________________________________________");
            Task newTask = new Task(input);
            arr.add(newTask);
            System.out.println("added: " + input);
            System.out.println("____________________________________________________________");

        }
        sc.close();
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

    public String getStatus() {
        return "[" + getStatusIcon() + "] " + this.description;
    }

    public void markAsDone() {
        this.complete();
    }

    public void markAsUndone() {
        this.incomplete();
    }
}
