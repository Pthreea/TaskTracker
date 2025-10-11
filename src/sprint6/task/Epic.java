package sprint6.task;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<String> subtaskIds;

    public Epic(String name, String description) {
        super(name, description, Status.NEW);
        this.subtaskIds = new ArrayList<>();
    }

    public List<String> getSubtaskIds() {
        return subtaskIds;
    }

    public void addSubtaskId(String subtaskId) {
        subtaskIds.add(subtaskId);
    }

    public void removeSubtaskId(String subtaskId) {
        subtaskIds.remove(subtaskId);
    }

    public void clearSubtaskIds() {
        subtaskIds.clear();
    }
}