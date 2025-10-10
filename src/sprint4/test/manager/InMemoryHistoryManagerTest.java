package sprint4.test.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sprint4.manager.HistoryManager;
import sprint4.manager.Managers;
import sprint4.task.Task;
import sprint4.task.Status;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    private HistoryManager historyManager;

    @BeforeEach
    void setUp() {
        historyManager = Managers.getDefaultHistory();
    }

    @Test
    void shouldAddTaskToHistory() {
        Task task = new Task("Test", "Description", Status.NEW);
        historyManager.add(task);

        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size());
        assertEquals(task, history.get(0));
    }

    @Test
    void shouldNotDuplicateTaskInHistory() {
        Task task = new Task("Test", "Description", Status.NEW);
        historyManager.add(task);
        historyManager.add(task);

        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size());
    }

    @Test
    void shouldRemoveTaskFromHistory() {
        Task task = new Task("Test", "Description", Status.NEW);
        historyManager.add(task);
        historyManager.remove(task.getId());

        List<Task> history = historyManager.getHistory();
        assertTrue(history.isEmpty());
    }
}