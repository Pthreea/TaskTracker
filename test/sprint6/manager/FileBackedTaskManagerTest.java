package sprint6.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import sprint6.exceptions.ManagerSaveException;
import sprint6.task.Epic;
import sprint6.task.Status;
import sprint6.task.Subtask;
import sprint6.task.Task;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest {
    private File tempFile;
    private FileBackedTaskManager manager;

    @BeforeEach
    void setUp() throws IOException {
        tempFile = File.createTempFile("tasks-test", ".csv");
        manager = new FileBackedTaskManager(tempFile);
    }

    @AfterEach
    void tearDown() {
        tempFile.delete();
    }

    @Test
    void shouldSaveAndLoadEmptyTaskManager() {
        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);

        assertTrue(loadedManager.getAllTasks().isEmpty());
        assertTrue(loadedManager.getAllEpics().isEmpty());
        assertTrue(loadedManager.getAllSubtasks().isEmpty());
        assertTrue(loadedManager.getHistory().isEmpty());
    }

    @Test
    void shouldSaveAndLoadTaskManager() {
        Task task = new Task("Test Task", "Description", Status.NEW);
        Epic epic = new Epic("Test Epic", "Description");
        manager.addTask(task);
        manager.addEpic(epic);
        Subtask subtask = new Subtask("Test Subtask", "Description", epic.getId());
        manager.addSubtask(subtask);

        manager.getTaskById(task.getId()); // добавляем в историю

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);

        assertEquals(1, loadedManager.getAllTasks().size());
        assertEquals(1, loadedManager.getAllEpics().size());
        assertEquals(1, loadedManager.getAllSubtasks().size());
        assertEquals(1, loadedManager.getHistory().size());
    }

    @Test
    void shouldSaveAndLoadHistory() {
        Task task = new Task("Test Task", "Description", Status.NEW);
        manager.addTask(task);
        manager.getTaskById(task.getId());

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);
        List<Task> history = loadedManager.getHistory();

        assertEquals(1, history.size());
        assertEquals(task.getId(), history.get(0).getId());
    }

    @Test
    void shouldHandleEmptyFile() {
        tempFile.delete();
        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);

        assertNotNull(loadedManager);
        assertTrue(loadedManager.getAllTasks().isEmpty());
    }

    @Test
    void shouldThrowManagerSaveException() {
        tempFile.setReadOnly();
        Task task = new Task("Test Task", "Description", Status.NEW);

        assertThrows(ManagerSaveException.class, () -> manager.addTask(task));
    }
}