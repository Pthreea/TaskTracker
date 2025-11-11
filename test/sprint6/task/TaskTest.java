package sprint6.task;

import org.junit.jupiter.api.Test;
import java.time.Duration;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    void createTask() {
        Task task = new Task("Test Task", "Description", Status.NEW);

        assertEquals("Test Task", task.getName(), "Имя задачи должно совпадать");
        assertEquals("Description", task.getDescription(), "Описание задачи должно совпадать");
        assertEquals(Status.NEW, task.getStatus(), "Статус задачи должен совпадать");
    }

    @Test
    void createTaskWithTime() {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.ofMinutes(30);

        Task task = new Task("Test Task", "Description", Status.NEW, duration, now);

        assertEquals(now, task.getStartTime(), "Время начала должно совпадать");
        assertEquals(duration, task.getDuration(), "Продолжительность должна совпадать");
        assertEquals(now.plus(duration), task.getEndTime(), "Время окончания должно быть рассчитано правильно");
    }

    @Test
    void setAndGetId() {
        Task task = new Task("Test Task", "Description", Status.NEW);

        task.setId("task-1");

        assertEquals("task-1", task.getId(), "ID задачи должен совпадать");
    }

    @Test
    void setAndGetStatus() {
        Task task = new Task("Test Task", "Description", Status.NEW);

        task.setStatus(Status.IN_PROGRESS);

        assertEquals(Status.IN_PROGRESS, task.getStatus(), "Статус задачи должен обновиться");
    }

    @Test
    void setAndGetDescription() {
        Task task = new Task("Test Task", "Description", Status.NEW);

        task.setDescription("Updated Description");

        assertEquals("Updated Description", task.getDescription(), "Описание задачи должно обновиться");
    }

    @Test
    void setAndGetStartTime() {
        Task task = new Task("Test Task", "Description", Status.NEW);
        LocalDateTime now = LocalDateTime.now();

        task.setStartTime(now);

        assertEquals(now, task.getStartTime(), "Время начала должно обновиться");
    }

    @Test
    void setAndGetDuration() {
        Task task = new Task("Test Task", "Description", Status.NEW);
        Duration duration = Duration.ofMinutes(45);

        task.setDuration(duration);

        assertEquals(duration, task.getDuration(), "Продолжительность должна обновиться");
    }

    @Test
    void getEndTimeWithNoStartTime() {
        Task task = new Task("Test Task", "Description", Status.NEW);
        Duration duration = Duration.ofMinutes(30);

        task.setDuration(duration);

        assertNull(task.getEndTime(), "Время окончания должно быть null, если время начала не задано");
    }

    @Test
    void getEndTimeWithNoDuration() {
        Task task = new Task("Test Task", "Description", Status.NEW);
        LocalDateTime now = LocalDateTime.now();

        task.setStartTime(now);

        assertEquals(now, task.getEndTime(), "Время окончания должно быть равно времени начала, если продолжительность равна нулю");
    }

    @Test
    void equalsAndHashCode() {
        Task task1 = new Task("Task", "Description", Status.NEW);
        Task task2 = new Task("Different name", "Different description", Status.DONE);

        task1.setId("1");
        task2.setId("1");

        assertEquals(task1, task2, "Задачи с одинаковыми ID должны быть равны");
        assertEquals(task1.hashCode(), task2.hashCode(), "Хеш-коды задач с одинаковыми ID должны совпадать");

        task2.setId("2");

        assertNotEquals(task1, task2, "Задачи с разными ID не должны быть равны");
    }
}
