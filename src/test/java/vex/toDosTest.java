package vex;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class ToDosTest {

    @Test
    public void testToString() {
        ToDos todo = new ToDos("read book");
        assertEquals("[T][ ] read book", todo.toString());
    }

    @Test
    public void testMarkAsDone() {
        ToDos todo = new ToDos("read book");
        todo.markAsDone();
        assertTrue(todo.toString().contains("[X]"));
    }
}
