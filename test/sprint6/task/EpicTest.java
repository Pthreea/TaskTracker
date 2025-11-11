package sprint6.task;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    @Test
    void createEpic() {
        Epic epic = new Epic("Test Epic", "Description");

        assertEquals("Test Epic", epic.getName(), "Имя эпика должно совпадать");
        assertEquals("Description", epic.getDescription(), "Описание эпика должно совпадать");
        assertEquals(Status.NEW, epic.getStatus(), "Начальный статус эпика должен быть NEW");
        assertTrue(epic.getSubtaskIds().isEmpty(), "Список подзадач должен быть пустым");
    }

    @Test
    void addSubtaskId() {
        Epic epic = new Epic("Test Epic", "Description");

        epic.addSubtaskId("sub-1");

        assertEquals(1, epic.getSubtaskIds().size(), "Размер списка подзадач должен быть 1");
        assertTrue(epic.getSubtaskIds().contains("sub-1"), "Список подзадач должен содержать добавленный ID");
    }

    @Test
    void removeSubtaskId() {
        Epic epic = new Epic("Test Epic", "Description");

        epic.addSubtaskId("sub-1");
        epic.addSubtaskId("sub-2");

        epic.removeSubtaskId("sub-1");

        assertEquals(1, epic.getSubtaskIds().size(), "Размер списка подзадач должен быть 1");
        assertFalse(epic.getSubtaskIds().contains("sub-1"), "Список подзадач не должен содержать удаленный ID");
        assertTrue(epic.getSubtaskIds().contains("sub-2"), "Список подзадач должен сохранить другие ID");
    }

    @Test
    void clearSubtaskIds() {
        Epic epic = new Epic("Test Epic", "Description");

        epic.addSubtaskId("sub-1");
        epic.addSubtaskId("sub-2");

        epic.clearSubtaskIds();

        assertTrue(epic.getSubtaskIds().isEmpty(), "Список подзадач должен быть пустым после очистки");
    }

    @Test
    void getEndTimeWithoutSubtasks() {
        Epic epic = new Epic("Test Epic", "Description");

        assertNull(epic.getEndTime(), "Время окончания должно быть null, если нет подзадач");
    }
}
