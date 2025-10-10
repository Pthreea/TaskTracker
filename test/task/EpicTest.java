package test.task;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sprint4.Task.Epic;

import static org.junit.jupiter.api.Assertions.*;

public class EpicTest {

    @Test
    @DisplayName("Должен проверять эквивалентность эпиков с одинаковым ID")
    void shouldVerifyEpicEqualityTest() {
        // given
        Epic epic1 = new Epic("Epic", "Description");

        // when
        Epic epic2 = new Epic("Epic", "Description") {
            {
                // Force similar ID generation logic to simulate tests
                try {
                    var field = Epic.class.getSuperclass().getDeclaredField("id");
                    field.setAccessible(true);
                    field.set(this, epic1.getId());
                } catch (IllegalAccessException | NoSuchFieldException e) {
                    fail("Failed to set epic's ID for testing.");
                }
            }
        };

        // then
        assertEquals(epic1, epic2, "Эпики должны быть эквивалентны, если их ID совпадают");
        assertEquals(epic1.hashCode(), epic2.hashCode(), "Эпики должны иметь одинаковый хэш-код для одинаковых ID");
    }
}
