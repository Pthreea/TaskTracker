package sprint4.Task;

import java.util.Objects;

public class Task {
    private static int idCounter = 0;
    private final String id;
    private String name;
    private String description;
    private Status status;

    public Task(String name, String description, Status aNew) {
        this.id = generateId();
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
    }

    private static String generateId() {
        return "TASK-" + (++idCounter);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Task task = (Task) obj;
        return Objects.equals(id, task.id);
    }
}


