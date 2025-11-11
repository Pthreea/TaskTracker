package sprint6.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sprint6.task.Epic;
import sprint6.task.Status;
import sprint6.task.Subtask;
import sprint6.task.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {
    protected T manager;
    protected LocalDateTime now = LocalDateTime.now();

    @BeforeEach
    public abstract void setUp();

    @Test
    void addNewTask() {
        Task task = new Task("Test Task", "Description", Status.NEW);
        manager.addTask(task);

        Task savedTask = manager.getTaskById(task.getId());
        assertNotNull(savedTask, "Задача не найдена");
        assertEquals(task, savedTask, "Задачи не совпадают");
    }

    @Test
    void addTaskWithTime() {
        Task task = new Task("Test Task", "Description", Status.NEW);
        task.setStartTime(now);
        task.setDuration(Duration.ofMinutes(30));
        manager.addTask(task);

        Task savedTask = manager.getTaskById(task.getId());
        assertEquals(now, savedTask.getStartTime(), "Время начала не совпадает");
        assertEquals(Duration.ofMinutes(30), savedTask.getDuration(), "Продолжительность не совпадает");
        assertEquals(now.plusMinutes(30), savedTask.getEndTime(), "Время окончания рассчитано неверно");
    }

    @Test
    void addNewEpic() {
        Epic epic = new Epic("Test Epic", "Description");
        manager.addEpic(epic);

        Epic savedEpic = manager.getEpicById(epic.getId());
        assertNotNull(savedEpic, "Эпик не найден");
        assertEquals(epic, savedEpic, "Эпики не совпадают");
        assertEquals(Status.NEW, savedEpic.getStatus(), "У эпика должен быть статус NEW");
    }

    @Test
    void addNewSubtask() {
        Epic epic = new Epic("Test Epic", "Description");
        manager.addEpic(epic);

        Subtask subtask = new Subtask("Test Subtask", "Description", epic.getId());
        manager.addSubtask(subtask);

        Subtask savedSubtask = manager.getSubtaskById(subtask.getId());
        assertNotNull(savedSubtask, "Подзадача не найдена");
        assertEquals(subtask, savedSubtask, "Подзадачи не совпадают");
        assertEquals(epic.getId(), savedSubtask.getEpicId(), "ID эпика не совпадает");

        List<Subtask> epicSubtasks = manager.getEpicSubtasks(epic.getId());
        assertEquals(1, epicSubtasks.size(), "Неверное количество подзадач у эпика");
    }

    @Test
    void updateTask() {
        Task task = new Task("Test Task", "Description", Status.NEW);
        manager.addTask(task);

        task.setStatus(Status.IN_PROGRESS);
        manager.updateTask(task);

        Task updatedTask = manager.getTaskById(task.getId());
        assertEquals(Status.IN_PROGRESS, updatedTask.getStatus(), "Статус задачи не обновился");
    }

    @Test
    void updateEpic() {
        Epic epic = new Epic("Test Epic", "Description");
        manager.addEpic(epic);

        epic.setDescription("Updated Description");
        manager.updateEpic(epic);

        Epic updatedEpic = manager.getEpicById(epic.getId());
        assertEquals("Updated Description", updatedEpic.getDescription(), "Описание эпика не обновилось");
    }

    @Test
    void updateSubtask() {
        Epic epic = new Epic("Test Epic", "Description");
        manager.addEpic(epic);

        Subtask subtask = new Subtask("Test Subtask", "Description", epic.getId());
        manager.addSubtask(subtask);

        subtask.setStatus(Status.DONE);
        manager.updateSubtask(subtask);

        Subtask updatedSubtask = manager.getSubtaskById(subtask.getId());
        assertEquals(Status.DONE, updatedSubtask.getStatus(), "Статус подзадачи не обновился");

        Epic updatedEpic = manager.getEpicById(epic.getId());
        assertEquals(Status.DONE, updatedEpic.getStatus(), "Статус эпика должен быть DONE");
    }

    @Test
    void deleteTask() {
        Task task = new Task("Test Task", "Description", Status.NEW);
        manager.addTask(task);

        manager.deleteTaskById(task.getId());

        assertNull(manager.getTaskById(task.getId()), "Задача не удалилась");
        assertTrue(manager.getHistory().isEmpty(), "История не очистилась после удаления задачи");
    }

    @Test
    void deleteEpicWithSubtasks() {
        Epic epic = new Epic("Test Epic", "Description");
        manager.addEpic(epic);

        Subtask subtask = new Subtask("Test Subtask", "Description", epic.getId());
        manager.addSubtask(subtask);

        manager.deleteEpicById(epic.getId());

        assertNull(manager.getEpicById(epic.getId()), "Эпик не удалился");
        assertNull(manager.getSubtaskById(subtask.getId()), "Подзадача не удалилась вместе с эпиком");
        assertTrue(manager.getHistory().isEmpty(), "История не очистилась после удаления эпика");
    }

    @Test
    void deleteSubtask() {
        Epic epic = new Epic("Test Epic", "Description");
        manager.addEpic(epic);

        Subtask subtask = new Subtask("Test Subtask", "Description", epic.getId());
        manager.addSubtask(subtask);

        manager.deleteSubtaskById(subtask.getId());

        assertNull(manager.getSubtaskById(subtask.getId()), "Подзадача не удалилась");
        assertTrue(manager.getEpicSubtasks(epic.getId()).isEmpty(), "Подзадача осталась в списке эпика");
        assertEquals(Status.NEW, manager.getEpicById(epic.getId()).getStatus(), "Статус эпика должен стать NEW");
    }

    @Test
    void epicStatusCalculationAllNew() {
        Epic epic = new Epic("Test Epic", "Description");
        manager.addEpic(epic);

        Subtask subtask1 = new Subtask("Subtask 1", "Description", epic.getId());
        Subtask subtask2 = new Subtask("Subtask 2", "Description", epic.getId());

        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);

        assertEquals(Status.NEW, manager.getEpicById(epic.getId()).getStatus(), "Статус эпика должен быть NEW");
    }

    @Test
    void epicStatusCalculationAllDone() {
        Epic epic = new Epic("Test Epic", "Description");
        manager.addEpic(epic);

        Subtask subtask1 = new Subtask("Subtask 1", "Description", epic.getId());
        Subtask subtask2 = new Subtask("Subtask 2", "Description", epic.getId());

        subtask1.setStatus(Status.DONE);
        subtask2.setStatus(Status.DONE);

        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);

        assertEquals(Status.DONE, manager.getEpicById(epic.getId()).getStatus(), "Статус эпика должен быть DONE");
    }

    @Test
    void epicStatusCalculationMixed() {
        Epic epic = new Epic("Test Epic", "Description");
        manager.addEpic(epic);

        Subtask subtask1 = new Subtask("Subtask 1", "Description", epic.getId());
        Subtask subtask2 = new Subtask("Subtask 2", "Description", epic.getId());

        subtask1.setStatus(Status.NEW);
        subtask2.setStatus(Status.DONE);

        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);

        assertEquals(Status.IN_PROGRESS, manager.getEpicById(epic.getId()).getStatus(), "Статус эпика должен быть IN_PROGRESS");
    }

    @Test
    void epicStatusCalculationInProgress() {
        Epic epic = new Epic("Test Epic", "Description");
        manager.addEpic(epic);

        Subtask subtask1 = new Subtask("Subtask 1", "Description", epic.getId());
        Subtask subtask2 = new Subtask("Subtask 2", "Description", epic.getId());

        subtask1.setStatus(Status.IN_PROGRESS);
        subtask2.setStatus(Status.IN_PROGRESS);

        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);

        assertEquals(Status.IN_PROGRESS, manager.getEpicById(epic.getId()).getStatus(), "Статус эпика должен быть IN_PROGRESS");
    }

    @Test
    void epicTimeCalculation() {
        Epic epic = new Epic("Test Epic", "Description");
        manager.addEpic(epic);

        Subtask subtask1 = new Subtask("Subtask 1", "Description", epic.getId());
        subtask1.setStartTime(now);
        subtask1.setDuration(Duration.ofMinutes(30));

        Subtask subtask2 = new Subtask("Subtask 2", "Description", epic.getId());
        subtask2.setStartTime(now.plusHours(2));
        subtask2.setDuration(Duration.ofMinutes(45));

        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);

        Epic updatedEpic = manager.getEpicById(epic.getId());
        assertEquals(now, updatedEpic.getStartTime(), "Время начала эпика должно быть равно времени начала самой ранней подзадачи");
        assertEquals(Duration.ofMinutes(75), updatedEpic.getDuration(), "Продолжительность эпика должна быть суммой продолжительностей подзадач");
        assertEquals(now.plusHours(2).plusMinutes(45), updatedEpic.getEndTime(), "Время окончания эпика должно быть равно времени окончания самой поздней подзадачи");
    }

    @Test
    void prioritizedTasksOrder() {
        Task task3 = new Task("Task 3", "Description", Status.NEW);
        task3.setStartTime(now.plusHours(3));
        task3.setDuration(Duration.ofMinutes(30));

        Task task1 = new Task("Task 1", "Description", Status.NEW);
        task1.setStartTime(now.plusHours(1));
        task1.setDuration(Duration.ofMinutes(30));

        Task task2 = new Task("Task 2", "Description", Status.NEW);
        task2.setStartTime(now.plusHours(2));
        task2.setDuration(Duration.ofMinutes(30));

        manager.addTask(task3);
        manager.addTask(task1);
        manager.addTask(task2);

        List<Task> prioritizedTasks = manager.getPrioritizedTasks();
        assertEquals(3, prioritizedTasks.size(), "Неверное количество задач в списке по приоритету");
        assertEquals(task1.getId(), prioritizedTasks.get(0).getId(), "Неверный порядок задач");
        assertEquals(task2.getId(), prioritizedTasks.get(1).getId(), "Неверный порядок задач");
        assertEquals(task3.getId(), prioritizedTasks.get(2).getId(), "Неверный порядок задач");
    }

    @Test
    void tasksWithNoTimeInPrioritizedTasks() {
        Task taskWithTime = new Task("Task with time", "Description", Status.NEW);
        taskWithTime.setStartTime(now);
        taskWithTime.setDuration(Duration.ofMinutes(30));

        Task taskWithoutTime = new Task("Task without time", "Description", Status.NEW);

        manager.addTask(taskWithTime);
        manager.addTask(taskWithoutTime);

        List<Task> prioritizedTasks = manager.getPrioritizedTasks();
        assertEquals(1, prioritizedTasks.size(), "Задача без времени не должна попадать в список приоритетов");
        assertEquals(taskWithTime.getId(), prioritizedTasks.get(0).getId(), "В списке приоритетов должна быть только задача с временем");
    }

    @Test
    void overlappingTasksValidation() {
        Task task1 = new Task("Task 1", "Description", Status.NEW);
        task1.setStartTime(now);
        task1.setDuration(Duration.ofMinutes(30));

        Task task2 = new Task("Task 2", "Description", Status.NEW);
        task2.setStartTime(now.plusMinutes(15)); // пересечение с task1
        task2.setDuration(Duration.ofMinutes(30));

        manager.addTask(task1);

        assertThrows(IllegalStateException.class, () -> manager.addTask(task2), "Должно выбрасываться исключение при добавлении пересекающейся задачи");
    }
}