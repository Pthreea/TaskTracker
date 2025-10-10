package test.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sprint4.Manager.InMemoryTaskManager;
import sprint4.Manager.TaskManager;
import sprint4.Task.Task;
import sprint4.Task.Epic;
import sprint4.Task.Subtask;
import sprint4.Task.Status;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskManagerTest {

    private static final String TASK_NOT_FOUND_MSG = "Задача не найдена.";
    private static final String TASK_COUNT_MISMATCH_MSG = "Неверное количество задач.";

    private TaskManager taskManager;

    @BeforeEach
    public void setUp() {
        taskManager = new InMemoryTaskManager();
    }

    @Test
    @DisplayName("Должен создать новую задачу и удостовериться, что она добавлена")
    void shouldAddAndRetrieveTaskTest() {
        Task task = new Task("Test addNewTask", "Test addNewTask description", Status.NEW);
        taskManager.addTask(task);
        final Task savedTask = taskManager.getTaskById(task.getId());

        assertNotNull(savedTask, TASK_NOT_FOUND_MSG);
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getAllTasks();
        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), TASK_COUNT_MISMATCH_MSG);
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    @DisplayName("Должен корректно добавлять и находить эпики и подзадачи по ID")
    void shouldAddAndRetrieveEpicsAndSubtasksTest() {
        Epic epic = new Epic("Epic 1", "Description for Epic 1");
        taskManager.addEpic(epic);
        assertEquals(epic, taskManager.getEpicById(epic.getId()));

        Subtask subtask = new Subtask("Subtask", "Description", epic.getId());
        taskManager.addSubtask(subtask);
        assertEquals(subtask, taskManager.getSubtaskById(subtask.getId()));
    }
}
