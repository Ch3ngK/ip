import java.util.*;
public class Vex {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String name = "Vex";
        String memory = "";
        int number = 1;

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
                System.out.println(memory);
                System.out.println("____________________________________________________________");
                continue;
            }

            System.out.println("____________________________________________________________");
            System.out.println("added: " + input);
            System.out.println("____________________________________________________________");
            memory += number + ". " + input + "\n";
            number++;

        }
        sc.close();
    }
}
