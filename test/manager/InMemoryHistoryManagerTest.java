package test.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sprint4.Manager.InMemoryHistoryManager;
import sprint4.Manager.HistoryManager;
import sprint4.Task.Task;
import sprint4.Task.Status;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryHistoryManagerTest {

    private HistoryManager historyManager;

    @BeforeEach
    public void setUp() {
        historyManager = new InMemoryHistoryManager();
    }

    @Test
    @DisplayName("Должен добавлять задачу в историю и проверять, что история не пустая")
    void shouldAddAndVerifyHistoryTest() {
        Task task = new Task("History Task", "Task to add to history", Status.NEW);
        historyManager.add(task);
        final List<Task> history = historyManager.getHistory();

        assertNotNull(history, "После добавления задачи, история не должна быть пустой.");
        assertEquals(1, history.size(), "Неверное количество записей в истории.");
        assertEquals(task, history.get(0), "Указанная задача в истории неверная.");
    }
}