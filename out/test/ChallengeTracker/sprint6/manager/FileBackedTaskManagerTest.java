package sprint6.manager;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sprint6.task.Task;
import sprint6.task.Status;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {
    private File tempFile;

    @Override
    @BeforeEach
    public void setUp() {
        try {
            tempFile = File.createTempFile("tasks-test", ".csv");
            manager = new FileBackedTaskManager(tempFile);
        } catch (IOException e) {
            fail("Could not create temp file: " + e.getMessage());
        }
    }

    @AfterEach
    void tearDown() {
        tempFile.delete();
    }

    @Test
    void saveAndLoadEmptyTaskManager() {
        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);
        assertTrue(loadedManager.getAllTasks().isEmpty(), "Список задач должен быть пустым");
        assertTrue(loadedManager.getAllEpics().isEmpty(), "Список эпиков должен быть пустым");
        assertTrue(loadedManager.getAllSubtasks().isEmpty(), "Список подзадач должен быть пустым");
    }

    @Test
    void saveAndLoadTasksWithHistory() {
        Task task = new Task("Test Task", "Description", Status.NEW);
        manager.addTask(task);

        // Добавляем задачу в историю
        manager.getTaskById(task.getId());

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);
        assertEquals(1, loadedManager.getHistory().size(), "История должна содержать одну задачу");
        assertEquals(task.getId(), loadedManager.getHistory().get(0).getId(), "ID задачи в истории должен совпадать");
    }

    @Test
    void exceptionWhenSavingToReadOnlyFile() {
        tempFile.setReadOnly();

        Task task = new Task("Test Task", "Description", Status.NEW);

        assertThrows(ManagerSaveException.class, () -> manager.addTask(task), "Должно выбрасываться исключение при сохранении в файл только для чтения");
    }

    @Test
    void exceptionWhenLoadingFromNonexistentFile() {
        File nonExistentFile = new File("non-existent-file.csv");

        assertThrows(ManagerSaveException.class, () -> FileBackedTaskManager.loadFromFile(nonExistentFile), "Должно выбрасываться исключение при загрузке из несуществующего файла");
    }
}