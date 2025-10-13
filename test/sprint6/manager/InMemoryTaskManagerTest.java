package sprint6.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sprint6.manager.TaskManager;
import sprint6.manager.Managers;
import sprint6.task.Task;
import sprint6.task.Epic;
import sprint6.task.Subtask;
import sprint6.task.Status;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    private TaskManager taskManager;

    @BeforeEach
    void setUp() {
        taskManager = Managers.getDefault();
    }

    @Test
    void shouldCreateTask() {
        Task task = new Task("Test", "Description", Status.NEW);
        taskManager.addTask(task);

        Task savedTask = taskManager.getTaskById(task.getId());
        assertNotNull(savedTask);
        assertEquals(task, savedTask);
    }

    @Test
    void shouldCreateEpicWithSubtasks() {
        Epic epic = new Epic("Epic", "Description");
        taskManager.addEpic(epic);

        Subtask subtask = new Subtask("Subtask", "Description", epic.getId());
        taskManager.addSubtask(subtask);

        assertEquals(1, taskManager.getEpicSubtasks(epic.getId()).size());
    }

    @Test
    void shouldUpdateEpicStatus() {
        Epic epic = new Epic("Epic", "Description");
        taskManager.addEpic(epic);

        Subtask subtask = new Subtask("Subtask", "Description", epic.getId());
        taskManager.addSubtask(subtask);

        subtask.setStatus(Status.DONE);
        taskManager.updateSubtask(subtask);

        assertEquals(Status.DONE, taskManager.getEpicById(epic.getId()).getStatus());
    }

    @Test
    void shouldRemoveTaskFromHistory() {
        Task task = new Task("Test", "Description", Status.NEW);
        taskManager.addTask(task);
        taskManager.getTaskById(task.getId());
        taskManager.deleteTaskById(task.getId());

        assertTrue(taskManager.getHistory().isEmpty());
    }
}
