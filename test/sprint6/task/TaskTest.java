package sprint6.test.task;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sprint6.task.Task;
import sprint6.task.Status;

import static org.junit.jupiter.api.Assertions.*;

public class TaskTest {

    @Test
    @DisplayName("Должен проверять эквивалентность задач с одинаковым ID")
    void shouldVerifyTaskEqualityTest() {
        // given
        Task task1 = new Task("Task", "Description", Status.NEW);

        // when
        Task task2 = new Task("Task", "Description", Status.NEW) {
            {
                // Force similar ID generation logic to simulate tests
                try {
                    var field = Task.class.getDeclaredField("id");
                    field.setAccessible(true);
                    field.set(this, task1.getId());
                } catch (IllegalAccessException | NoSuchFieldException e) {
                    fail("Failed to set task's ID for testing.");
                }
            }
        };

        // then
        assertEquals(task1, task2, "Задачи должны быть эквивалентны, если их ID совпадают");
        assertEquals(task1.hashCode(), task2.hashCode(), "Задачи должны иметь одинаковый хэш-код для одинаковых ID");
    }
}
