package vex;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

/**
 * Deals with loading tasks from the file and saving tasks in the file.
 */
public class Storage {

    private final Path filePath;

    /**
     * Initializes a Storage object with the specified file path.
     *
     * @param filePathString The string path of the file to store data.
     */
    public Storage(String filePathString) {
        this.filePath = Paths.get(filePathString);
    }

    /**
     * Saves the list of tasks to the hard disk.
     *
     * @param tasks The list of Task objects to be saved.
     */
    public void save(List<Task> tasks) {
        try {
            Files.createDirectories(filePath.getParent());

            try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
                for (Task t : tasks) {
                    writer.write(t.toFileString());
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.out.println("Error saving tasks to file.");
        }
    }

    /**
     * Loads the tasks from the hard disk and returns them as an ArrayList.
     * If the file does not exist, a new file is created and an empty list is returned.
     *
     * @return An ArrayList of tasks loaded from the file.
     */
    public ArrayList<Task> load() {
        ArrayList<Task> tasks = new ArrayList<>();

        try {
            Files.createDirectories(filePath.getParent());

            if (!Files.exists(filePath)) {
                Files.createFile(filePath);
                return tasks;
            }

            List<String> lines = Files.readAllLines(filePath);
            for (String line : lines) {
                try {
                    tasks.add(parseTask(line));
                } catch (Exception e) {
                    System.out.println("Ignoring corrupted data: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading tasks from file.");
        }

        return tasks;
    }

    /**
     * Parses a single line from the save file into a Task object.
     *
     * @param line The string line representing a task in the save file.
     * @return The corresponding Task object (Todo, Deadline, or Event).
     * @throws IllegalArgumentException If the line format is invalid.
     */
    private Task parseTask(String line) {
        String[] parts = line.split(" \\| ");

        if (parts.length < 3) {
            throw new IllegalArgumentException();
        }

        String type = parts[0];
        boolean done = parts[1].equals("1");
        String desc = parts[2];

        Task task;

        if (type.equals("T")) {
            task = new ToDos(desc);
        } else if (type.equals("D")) {
            LocalDateTime byDate = LocalDateTime.parse(parts[3]);
            task = new Deadlines(desc, byDate);
        } else if (type.equals("E")) {
            LocalDateTime fromDate = LocalDateTime.parse(parts[3]);
            LocalDateTime toDate = LocalDateTime.parse(parts[4]);
            task = new Events(desc, fromDate, toDate);
        } else {
            throw new IllegalArgumentException();
        }

        if (done) {
            task.markAsDone();
        }

        return task;
    }
}