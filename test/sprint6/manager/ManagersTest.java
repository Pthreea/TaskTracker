package sprint6.test.manager;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sprint6.manager.Managers;
import sprint6.manager.TaskManager;
import sprint6.manager.HistoryManager;

import static org.junit.jupiter.api.Assertions.*;

public class ManagersTest {

    @Test
    @DisplayName("Убедитесь, что Managers возвращает проинициализированные экземпляры менеджеров")
    void shouldReturnInitializedInstancesTest() {
        TaskManager taskManager = Managers.getDefault();
        assertNotNull(taskManager, "Не удалось создать экземпляр TaskManager");

        HistoryManager historyManager = Managers.getDefaultHistory();
        assertNotNull(historyManager, "Не удалось создать экземпляр HistoryManager");
    }
}
