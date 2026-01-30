import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Storage {

    private final Path filePath;

    public Storage() {
        filePath = Paths.get("data", "vex.txt");
    }

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
            task = new Deadlines(desc, parts[3]);
        } else if (type.equals("E")) {
            task = new Events(desc, parts[3], parts[4]);
        } else {
            throw new IllegalArgumentException();
        }

        if (done) {
            task.markAsDone();
        }

        return task;
    }
}
