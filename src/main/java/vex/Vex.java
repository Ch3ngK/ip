package vex;

import java.util.Scanner;

/**
 * Entry point of the Vex chatbot application.
 * Initializes the application's main components and handles the primary control
 * flow.
 */
public class Vex {

    private static final String DEFAULT_SAVE_PATH = "data/tasks.txt";
    private static final String COMMAND_BYE = "bye";

    private final Storage storage;
    private final TaskList tasks;
    private final Ui ui;

    /**
     * Initializes a new Vex instance.
     * Sets up the UI and storage, and attempts to load existing tasks from the
     * specified file.
     *
     * @param filePath The path to the file where tasks are stored.
     */
    public Vex(String filePath) {
        this.ui = new Ui();
        this.storage = new Storage(filePath);
        this.tasks = loadTasksOrDefault();
    }

    /**
     * Loads tasks from storage. If loading fails, returns an empty TaskList.
     *
     * @return Loaded TaskList, or an empty TaskList if loading fails.
     */
    private TaskList loadTasksOrDefault() {
        try {
            return new TaskList(storage.load());
        } catch (RuntimeException e) {
            ui.showError("Loading error.");
            return new TaskList();
        }
    }

    /**
     * Starts the main execution loop of the chatbot.
     * Continues to read and process user commands until the "bye" command is
     * received.
     */
    public void run() {
        ui.showGreeting();

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                String fullCommand = scanner.nextLine().trim();

                if (fullCommand.isEmpty()) {
                    continue;
                }

                if (fullCommand.equalsIgnoreCase(COMMAND_BYE)) {
                    ui.showBye();
                    return;
                }

                Parser.handleCommand(fullCommand, tasks, ui, storage);
            }
        }
    }

    /**
     * Main method to launch the Vex application.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        new Vex(DEFAULT_SAVE_PATH).run();
    }

    /**
     * Generates a response for GUI input.
     *
     * @param input User command
     * @return Response message
     */
    public String getResponse(String input) {
        return Parser.handleCommandForGui(input, tasks, storage);
    }
}
