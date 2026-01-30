import java.util.Scanner; 

public class Vex {
    private Storage storage;
    private TaskList tasks;
    private Ui ui;

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

    public static void main(String[] args) {
        new Vex("data/tasks.txt").run();
    }
}