package vex;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Deals with loading tasks from the file and saving tasks in the file.
 */
public class Storage {

    private static final String DELIMITER = " \\| ";

    private final Path filePath;

    /**
     * Initializes a Storage object with the specified file path.
     *
     * @param filePathString The string path of the file to store data.
     */
    public Storage(String filePathString) {
        if (filePathString == null) {
            throw new IllegalArgumentException("filePathString must not be null");
        }
        this.filePath = Paths.get(filePathString);
    }

    /**
     * Saves the list of tasks to the hard disk.
     *
     * @param tasks The list of Task objects to be saved.
     */
    public void save(List<Task> tasks) {
        if (tasks == null) {
            throw new IllegalArgumentException("tasks must not be null");
        }

        try {
            Files.createDirectories(filePath.getParent());

            try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
                for (Task task : tasks) {
                    if (task == null) {
                        continue; // ignore unexpected null entries safely
                    }
                    writer.write(task.toFileString());
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.out.println("Error saving tasks to file.");
        }
    }

    /**
     * Loads the tasks from the hard disk and returns them as an ArrayList.
     * If the file does not exist, a new file is created and an empty list is
     * returned.
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
                Task parsed = tryParseTask(line);
                if (parsed != null) {
                    tasks.add(parsed);
                }
            }

        } catch (IOException e) {
            System.out.println("Error loading tasks from file.");
        }

        return tasks;
    }

    /**
     * Attempts to parse a line into a Task.
     * Returns null if the line is corrupted or cannot be parsed.
     */
    private Task tryParseTask(String line) {
        if (line == null || line.isBlank()) {
            return null;
        }

        try {
            return parseTask(line);
        } catch (IllegalArgumentException | DateTimeParseException e) {
            System.out.println("Ignoring corrupted data: " + line);
            return null;
        }
    }

    /**
     * Parses a single line from the save file into a Task object.
     *
     * @param line The string line representing a task in the save file.
     * @return The corresponding Task object (Todo, Deadline, or Event).
     * @throws IllegalArgumentException If the line format is invalid.
     */
    private Task parseTask(String line) {
        String[] parts = line.split(DELIMITER);

        if (parts.length < 3) {
            throw new IllegalArgumentException("Invalid save format: " + line);
        }

        String type = parts[0];
        boolean done = parts[1].equals("1");
        String desc = parts[2];

        Task task = createTaskFromParts(type, desc, parts);

        if (done) {
            task.markAsDone();
        }

        return task;
    }

    private Task createTaskFromParts(String type, String desc, String[] parts) {
        switch (type) {
            case "T":
                return new ToDos(desc);

            case "D":
                if (parts.length < 4) {
                    throw new IllegalArgumentException("Deadline missing by-date");
                }
                return new Deadlines(desc, LocalDateTime.parse(parts[3]));

            case "E":
                if (parts.length < 5) {
                    throw new IllegalArgumentException("Event missing from/to dates");
                }
                LocalDateTime fromDate = LocalDateTime.parse(parts[3]);
                LocalDateTime toDate = LocalDateTime.parse(parts[4]);
                return new Events(desc, fromDate, toDate);

            default:
                throw new IllegalArgumentException("Unknown task type: " + type);
        }
    }
}
