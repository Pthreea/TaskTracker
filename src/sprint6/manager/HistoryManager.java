package sprint6.manager;

import sprint6.task.Task;
import java.util.List;

public interface HistoryManager {
    void add(Task task);
    void remove(String id);
    List<Task> getHistory();
}
