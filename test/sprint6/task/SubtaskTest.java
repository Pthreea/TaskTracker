package sprint6.task;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {

    @Test
    void createSubtask() {
        Subtask subtask = new Subtask("Test Subtask", "Description", "epic-1");

        assertEquals("Test Subtask", subtask.getName(), "Имя подзадачи должно совпадать");
        assertEquals("Description", subtask.getDescription(), "Описание подзадачи должно совпадать");
        assertEquals(Status.NEW, subtask.getStatus(), "Начальный статус подзадачи должен быть NEW");
        assertEquals("epic-1", subtask.getEpicId(), "ID эпика должен совпадать");
    }

    @Test
    void getEpicId() {
        Subtask subtask = new Subtask("Test Subtask", "Description", "epic-1");

        assertEquals("epic-1", subtask.getEpicId(), "ID эпика должен совпадать");
    }
}