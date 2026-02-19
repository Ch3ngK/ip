package vex;

import java.io.IOException;
import java.util.Scanner;

/**
 * Entry point of the Vex chatbot application.
 * Initializes the application's main components and handles the primary control
 * flow.
 */
public class Vex {

    private static final String DEFAULT_SAVE_PATH = "data/tasks.txt";
    private static final String COMMAND_BYE = "bye";

    private static final String LOAD_ERROR_MESSAGE =
            "The campaign archives could not be read. Starting with an empty list.";

    private final Storage storage;
    private final TaskList tasks;
    private final Ui ui;

    /** Non-null only when load failed; shown once at startup (CLI or GUI). */
    private String startupErrorMessage;

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
     * Loads tasks from storage. If the file is missing, it is created and an empty list is used.
     * If loading fails (e.g. path invalid, permission denied), stores a message to show at startup.
     *
     * @return Loaded TaskList, or an empty TaskList if loading fails.
     */
    private TaskList loadTasksOrDefault() {
        try {
            return new TaskList(storage.load());
        } catch (IOException e) {
            startupErrorMessage = LOAD_ERROR_MESSAGE;
            return new TaskList();
        } catch (RuntimeException e) {
            startupErrorMessage = LOAD_ERROR_MESSAGE;
            return new TaskList();
        }
    }

    /**
     * Starts the main execution loop of the chatbot.
     * Continues to read and process user commands until the "bye" command is
     * received.
     */
    public void run() {
        if (startupErrorMessage != null) {
            ui.showError(startupErrorMessage);
            startupErrorMessage = null;
        }
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
     * Returns the greeting message for display in the GUI at startup.
     *
     * @return Greeting text
     */
    public String getGreeting() {
        ui.clearMessages();
        if (startupErrorMessage != null) {
            ui.showError(startupErrorMessage);
            startupErrorMessage = null;
        }
        ui.showGreeting();
        return ui.getAllMessages();
    }

    /**
     * Returns the goodbye message for display when the user exits (e.g. in GUI).
     *
     * @return Goodbye text
     */
    public String getByeMessage() {
        ui.clearMessages();
        ui.showBye();
        return ui.getAllMessages();
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
