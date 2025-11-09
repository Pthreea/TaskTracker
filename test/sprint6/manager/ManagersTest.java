package sprint6.manager;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

    @Test
    void getDefaultReturnsInMemoryTaskManager() {
        TaskManager manager = Managers.getDefault();

        assertNotNull(manager, "Менеджер задач не должен быть null");
        assertTrue(manager instanceof InMemoryTaskManager, "Должен возвращаться InMemoryTaskManager");
    }

    @Test
    void getDefaultHistoryReturnsInMemoryHistoryManager() {
        HistoryManager historyManager = Managers.getDefaultHistory();

        assertNotNull(historyManager, "Менеджер истории не должен быть null");
        assertTrue(historyManager instanceof InMemoryHistoryManager, "Должен возвращаться InMemoryHistoryManager");
    }
}
