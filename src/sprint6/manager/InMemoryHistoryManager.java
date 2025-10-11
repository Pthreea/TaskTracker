package sprint6.manager;

import sprint6.task.Task;
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
