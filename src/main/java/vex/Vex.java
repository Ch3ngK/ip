package vex;

import java.util.Scanner;

/**
 * Entry point of the Vex chatbot application.
 * Initializes the application's main components and handles the primary control
 * flow.
 */
public class Vex {
    private Storage storage;
    private TaskList tasks;
    private Ui ui;

    /**
     * Initializes a new Vex instance.
     * Sets up the UI, storage, and attempts to load existing tasks from the
     * specified file.
     *
     * @param filePath The path to the file where tasks are stored.
     */
    public Vex(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        try {
            tasks = new TaskList(storage.load());
        } catch (Exception e) {
            ui.showError("Loading error.");
            tasks = new TaskList();
        }
    }

    /**
     * Starts the main execution loop of the chatbot.
     * Continues to read and process user commands until the "bye" command is
     * received.
     */
    public void run() {
        ui.showGreeting();
        Scanner sc = new Scanner(System.in);
        boolean isExit = false;

        while (!isExit) {
            String fullCommand = sc.nextLine().trim();
            if (fullCommand.equalsIgnoreCase("bye")) {
                ui.showBye();
                isExit = true;
            } else if (!fullCommand.isEmpty()) {
                Parser.handleCommand(fullCommand, tasks, ui, storage);
            }
        }
        sc.close();
    }

    /**
     * Main method to launch the Vex application.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        new Vex("data/tasks.txt").run();
    }
}
