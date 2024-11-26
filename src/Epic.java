import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private final List<Integer> subtaskIds; // Список идентификаторов подзадач

    // Конструктор
    public Epic(String name, String description) {
        super(name, description, TaskStatus.NEW);
        this.subtaskIds = new ArrayList<>();
    }

    // Получение списка идентификаторов подзадач
    public List<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    // Добавление подзадачи
    public void addSubtask(int subtaskId) {
        subtaskIds.add(subtaskId);
    }

    // Удаление подзадачи
    public void removeSubtask(int subtaskId) {
        subtaskIds.remove((Integer) subtaskId); // Используем Integer для удаления по значению
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                ", subtaskIds=" + subtaskIds +
                '}';
    }
}
