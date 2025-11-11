package sprint6.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sprint6.task.Task;
import sprint6.task.Status;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryHistoryManagerTest {
    private HistoryManager historyManager;

    @BeforeEach
    void setUp() {
        historyManager = Managers.getDefaultHistory();
    }

    @Test
    void addTaskToHistory() {
        Task task = new Task("Test", "Description", Status.NEW);
        task.setId("1");
        historyManager.add(task);

        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size(), "История должна содержать одну задачу");
        assertEquals(task, history.get(0), "Задача в истории должна совпадать с добавленной");
    }

    @Test
    void emptyHistory() {
        List<Task> history = historyManager.getHistory();
        assertTrue(history.isEmpty(), "История должна быть пустой");
    }

    @Test
    void noDuplicatesInHistory() {
        Task task = new Task("Test", "Description", Status.NEW);
        task.setId("1");

        historyManager.add(task);
        historyManager.add(task);

        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size(), "В истории не должно быть дубликатов");
    }

    @Test
    void removeTaskFromHistoryStart() {
        Task task1 = new Task("Task 1", "Description", Status.NEW);
        task1.setId("1");
        Task task2 = new Task("Task 2", "Description", Status.NEW);
        task2.setId("2");
        Task task3 = new Task("Task 3", "Description", Status.NEW);
        task3.setId("3");

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        historyManager.remove("1"); // Удаляем с начала

        List<Task> history = historyManager.getHistory();
        assertEquals(2, history.size(), "История должна содержать две задачи");
        assertEquals(task2, history.get(0), "Первой задачей должна быть task2");
        assertEquals(task3, history.get(1), "Второй задачей должна быть task3");
    }

    @Test
    void removeTaskFromHistoryMiddle() {
        Task task1 = new Task("Task 1", "Description", Status.NEW);
        task1.setId("1");
        Task task2 = new Task("Task 2", "Description", Status.NEW);
        task2.setId("2");
        Task task3 = new Task("Task 3", "Description", Status.NEW);
        task3.setId("3");

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        historyManager.remove("2"); // Удаляем из середины

        List<Task> history = historyManager.getHistory();
        assertEquals(2, history.size(), "История должна содержать две задачи");
        assertEquals(task1, history.get(0), "Первой задачей должна быть task1");
        assertEquals(task3, history.get(1), "Второй задачей должна быть task3");
    }

    @Test
    void removeTaskFromHistoryEnd() {
        Task task1 = new Task("Task 1", "Description", Status.NEW);
        task1.setId("1");
        Task task2 = new Task("Task 2", "Description", Status.NEW);
        task2.setId("2");
        Task task3 = new Task("Task 3", "Description", Status.NEW);
        task3.setId("3");

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        historyManager.remove("3"); // Удаляем с конца

        List<Task> history = historyManager.getHistory();
        assertEquals(2, history.size(), "История должна содержать две задачи");
        assertEquals(task1, history.get(0), "Первой задачей должна быть task1");
        assertEquals(task2, history.get(1), "Второй задачей должна быть task2");
    }
    @Test
    void removeNonExistentTaskFromHistory() {
        Task task = new Task("Test", "Description", Status.NEW);
        task.setId("1");

        historyManager.add(task);
        historyManager.remove("999"); // Удаляем несуществующую задачу

        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size(), "История не должна измениться");
        assertEquals(task, history.get(0), "Задача должна остаться в истории");
    }

    @Test
    void removeTaskFromEmptyHistory() {
        // Не должно быть исключений
        assertDoesNotThrow(() -> historyManager.remove("1"), "Удаление из пустой истории не должно вызывать исключения");
        assertTrue(historyManager.getHistory().isEmpty(), "История должна оставаться пустой");
    }
}