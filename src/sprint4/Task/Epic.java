package sprint4.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Epic extends Task {
    private List<String> subtaskIds;

    public Epic(String name, String description) {
        super(name, description, Status.NEW);
        this.subtaskIds = new ArrayList<>();
    }

    public void addSubtaskId(String subtaskId) {
        subtaskIds.add(subtaskId);
    }

    public List<String> getSubtaskIds() {
        return subtaskIds;
    }

    public void clearSubtaskIds() {
        subtaskIds.clear();
    }

    public void updateStatus(Map<String, Subtask> allSubtasks) {
        boolean allDone = true;
        boolean anyInProgress = false;

        for (String subtaskId : subtaskIds) {
            Subtask subtask = allSubtasks.get(subtaskId);
            if (subtask != null) {
                if (subtask.getStatus() != Status.DONE) {
                    allDone = false;
                }
                if (subtask.getStatus() == Status.IN_PROGRESS) {
                    anyInProgress = true;
                }
            }
        }

        setStatus(allDone ? Status.DONE : (anyInProgress ? Status.IN_PROGRESS : Status.NEW));
    }
}