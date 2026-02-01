package vex;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import vex.ToDos;
import vex.Task;

public class storageTest {

    @TempDir
    Path tempDir;

    @Test
    public void saveAndLoad_singleTodo_success() {
        Path tempFile = tempDir.resolve("test_vex.txt");
        Storage storage = new Storage(tempFile.toString());
        
        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(new ToDos("run marathons"));
        
        storage.save(tasks);
        ArrayList<Task> loadedTasks = storage.load();
        
        assertEquals(1, loadedTasks.size());
        assertEquals("[T][ ] run marathons", loadedTasks.get(0).toString());
    }

    @Test
    public void load_corruptedFile_returnsEmptyListGracefully() {
        Storage storage = new Storage(tempDir.resolve("non_existent.txt").toString());
        
        ArrayList<Task> tasks = storage.load();
        assertNotNull(tasks);
        assertEquals(0, tasks.size(), "Should return an empty list if file is missing/new");
    }
}