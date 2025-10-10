package sprint4.manager;

import sprint4.task.Task;
import java.util.List;

public interface HistoryManager {
    void add(Task task);
    void remove(String id);
    List<Task> getHistory();
}
