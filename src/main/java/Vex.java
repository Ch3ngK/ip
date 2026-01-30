import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Vex {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String name = "Vex";
        Storage storage = new Storage();
        ArrayList<Task> arr = storage.load(); // create a new arraylist storing the tasks completed

        System.out.println("Hello! I'm " + name);
        System.out.println("What can I do for you?");
        System.out.println("____________________________________________________________");

        DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
        DateTimeFormatter displayFormat = DateTimeFormatter.ofPattern("MMM d yyyy HH:mm"); 

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

            // ==== New 'show' command to list tasks on a specific date ====
            else if (input.startsWith("show")) {
                String[] parts = input.split(" ", 2);
                if (parts.length < 2 || parts[1].trim().isEmpty()) {
                    showError("You must provide a date in yyyy-MM-dd format.");
                    continue;
                }
                LocalDate queryDate;
                try {
                    queryDate = LocalDate.parse(parts[1].trim());
                } catch (DateTimeParseException e) {
                    showError("Invalid date format. Use yyyy-MM-dd.");
                    continue;
                }

                System.out.println("____________________________________________________________");
                System.out.println("Tasks on " + queryDate.format(DateTimeFormatter.ofPattern("MMM d yyyy")) + ":");
                boolean found = false;
                for (Task t : arr) {
                    if (t instanceof Deadlines) {
                        Deadlines d = (Deadlines) t;
                        if (d.getBy().toLocalDate().equals(queryDate)) {
                            System.out.println(d.toString());
                            found = true;
                        }
                    } else if (t instanceof Events) {
                        Events e = (Events) t;
                        if (!e.getFrom().toLocalDate().isAfter(queryDate) && !e.getTo().toLocalDate().isBefore(queryDate)) {
                            System.out.println(e.toString());
                            found = true;
                        }
                    }
                }
                if (!found) System.out.println("No tasks found on this date.");
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
                        storage.save(arr);
                        continue;
                    } else if (parts[0].equals("unmark")) {
                        t.markAsUndone();
                        System.out.println("____________________________________________________________");
                        System.out.println("OK, I've marked this task as not done yet:");
                        System.out.println(t.toString());
                        System.out.println("____________________________________________________________");
                        storage.save(arr);
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
                    description = input.substring(5).trim();
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
                String description = parts[0].trim();
                String byInput = parts[1]; 
                LocalDateTime byDate; 
                try {
                    byDate = LocalDateTime.parse(byInput, inputFormat); 
                } catch (DateTimeParseException e) {
                    showError("Invalid date format. Use yyyy-MM-dd HHmm.");
                    continue; 
                }
                newTask = new Deadlines(description, byDate);
            }
            else if (input.startsWith("event")) {
                if (!input.contains("/from") || !input.contains("/to")) {
                    showError("Event command must have /from and /to with times! :-)");
                    continue;
                }
                String[] parts = input.substring(6).split(" /from | /to ");
                if (parts.length < 3 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty() || parts[2].trim().isEmpty()) {
                    showError("The description or times of an event is empty. Remember to fill it up!");
                    continue;
                }
                String description = parts[0].trim();
                LocalDateTime fromDate, toDate; 
                try {
                    fromDate = LocalDateTime.parse(parts[1].trim(), inputFormat); 
                    toDate = LocalDateTime.parse(parts[2].trim(), inputFormat); 
                } catch (DateTimeParseException e) {
                    showError("Invalid date format. Use yyyy-MM-dd HHmm.");
                    continue; 
                }
                newTask = new Events(description, fromDate, toDate);
            }
            else if (input.startsWith("delete")) {
                String[] parts = input.split(" " ,2);
                if (parts.length < 2) {
                    showError("You did not specify a task number. Remember to do so! :-)");
                    continue;
                }
                int taskNumber = Integer.parseInt(parts[1]);
                if (taskNumber < 1 || taskNumber > arr.size()) {
                    showError("Task number out of range.");
                    continue;
                }
                Task removedTask = arr.remove(taskNumber - 1);
                storage.save(arr);
                System.out.println("____________________________________________________________");
                System.out.println("Noted. I've removed this task:");
                System.out.println("  " + removedTask.toString());
                System.out.println("Now you have " + arr.size() + " tasks in the list.");
                System.out.println("____________________________________________________________");
                continue;
            } else {
                showError("I apologise, but I am unsure of what that means. Care to edit your message? :-(");
                continue;
            }

            arr.add(newTask);
            storage.save(arr);
            System.out.println("____________________________________________________________");
            System.out.println("Got it. I've added this task:");
            System.out.println("  " + newTask.toString());
            System.out.println("Now you have " + arr.size() + " tasks in the list.");
            System.out.println("____________________________________________________________");
        }
        sc.close();
    }

    // Helper methods
    private static void showError(String message) {
        System.out.println("____________________________________________________________");
        System.out.println(" Oh no! " + message);
        System.out.println("____________________________________________________________");
    }
}
