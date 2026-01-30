import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;


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
            LocalDateTime byDate = LocalDateTime.parse(parts[3]); // parse ISO datetime
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
