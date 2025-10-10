package test.task;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sprint4.Task.Subtask;

import static org.junit.jupiter.api.Assertions.*;

public class SubtaskTest {

    @Test
    @DisplayName("Должен проверять эквивалентность подзадач с одинаковым ID")
    void shouldValidateSubtaskEqualityTest() {
        // given
        Subtask subtask1 = new Subtask("Subtask", "Description", "Epic-1");

        // when
        Subtask subtask2 = new Subtask("Subtask", "Description", "Epic-1") {
            {
                // Force similar ID generation logic to simulate tests
                try {
                    var field = Subtask.class.getSuperclass().getDeclaredField("id");
                    field.setAccessible(true);
                    field.set(this, subtask1.getId());
                } catch (IllegalAccessException | NoSuchFieldException e) {
                    fail("Failed to set subtask's ID for testing.");
                }
            }
        };

        // then
        assertEquals(subtask1, subtask2, "Подзадачи должны быть эквивалентны, если их ID совпадают");
        assertEquals(subtask1.hashCode(), subtask2.hashCode(), "Подзадачи должны иметь одинаковый хэш-код для одинаковых ID");
    }
}