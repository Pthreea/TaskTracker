package sprint4.task;

public class Subtask extends Task {
    private String epicId;

    public Subtask(String name, String description, String epicId) {
        super(name, description, Status.NEW);
        this.epicId = epicId;
    }

    public String getEpicId() {
        return epicId;
    }
}