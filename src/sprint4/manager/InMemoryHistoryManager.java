package sprint4.manager;

import sprint4.task.Task;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final DoublyLinkedList history = new DoublyLinkedList();

    @Override
    public void add(Task task) {
        if (task != null) {
            history.add(task);
        }
    }

    @Override
    public void remove(String id) {
        history.remove(id);
    }

    @Override
    public List<Task> getHistory() {
        return history.getTasks();
    }
}
