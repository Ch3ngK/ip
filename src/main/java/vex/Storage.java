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
     * Constructs a Storage instance using the specified file path.
     *
     * @param filePathString File path where tasks are stored (e.g. "data/tasks.txt")
     * @throws IllegalArgumentException If filePathString is null or blank
     */
    public Storage(String filePathString) {
        if (filePathString == null || filePathString.isBlank()) {
            throw new IllegalArgumentException("file path must not be null or empty");
        }
        this.filePath = Paths.get(filePathString);
        assert this.filePath != null : "filePath should not be null after creation";
    }

    /**
     * Saves the list of tasks to disk.
     *
     * @param tasks List of tasks to save
     * @return true if save succeeded, false on IOException
     * @throws IllegalArgumentException If tasks is null
     */
    public boolean save(List<Task> tasks) {
        if (tasks == null) {
            throw new IllegalArgumentException("tasks must not be null");
        }

        try {
            Files.createDirectories(filePath.getParent());

            try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
                for (Task task : tasks) {
                    if (task == null) {
                        continue;
                    }
                    String serialized = task.toFileString();
                    assert serialized != null : "toFileString() should not return null";

                    writer.write(serialized);
                    writer.newLine();
                }
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Loads tasks from disk.
     * If the file does not exist, the directory is created, the file is created,
     * and an empty list is returned. If the file exists but cannot be read
     * (e.g. permissions, corrupted path), throws IOException so the caller can
     * inform the user.
     *
     * @return Tasks loaded from the save file (never null)
     * @throws IOException if the file or directory cannot be created or read
     */
    public ArrayList<Task> load() throws IOException {
        ArrayList<Task> tasks = new ArrayList<>();

        if (filePath.getParent() != null) {
            Files.createDirectories(filePath.getParent());
        }

        if (!Files.exists(filePath)) {
            Files.createFile(filePath);
            return tasks;
        }

        List<String> lines = Files.readAllLines(filePath);
        assert lines != null : "readAllLines should not return null";

        for (String line : lines) {
            Task parsed = tryParseTask(line);
            if (parsed != null) {
                tasks.add(parsed);
            }
        }

        return tasks;
    }

    /**
     * Attempts to parse a line into a Task.
     * Returns null if the line is corrupted or cannot be parsed.
     *
     * @param line A line from the save file
     * @return Parsed Task, or null if invalid
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
     * @param line The string line representing a task in the save file
     * @return The corresponding Task object (Todo, Deadline, or Event)
     * @throws IllegalArgumentException If the line format is invalid
     */
    private Task parseTask(String line) {
        assert line != null : "line passed to parseTask should not be null";

        String[] parts = line.split(DELIMITER);

        if (parts.length < 3) {
            throw new IllegalArgumentException("Invalid save format: " + line);
        }

        String type = parts[0];
        boolean done = parts[1].equals("1");
        String desc = parts[2];

        assert type != null : "Task type should not be null";
        assert desc != null : "Task description should not be null";

        Task task = createTaskFromParts(type, desc, parts);

        if (done) {
            task.markAsDone();
        }

        return task;
    }

    /**
     * Creates a Task instance based on the parsed fields.
     *
     * @param type Task type identifier (T, D, E)
     * @param desc Task description
     * @param parts Full split parts array
     * @return A Task instance
     * @throws IllegalArgumentException If fields are missing or type is unknown
     */
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
