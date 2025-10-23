package sprint6.manager;

import sprint6.task.Task;
import sprint6.task.Epic;
import sprint6.task.Subtask;
import sprint6.task.Status;

import java.io.*;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTaskManager(File file) {
        super();
        this.file = file;
    }

    private void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("id,type,name,status,description,epic\n");

            for (Task task : getAllTasks()) {
                writer.write(toString(task) + "\n");
            }
            for (Epic epic : getAllEpics()) {
                writer.write(toString(epic) + "\n");
            }
            for (Subtask subtask : getAllSubtasks()) {
                writer.write(toString(subtask) + "\n");
            }

            writer.write("\n");
            writer.write(historyToString(getHistory()));

        } catch (IOException e) {
            throw new ManagerSaveException("Could not save to file: " + e.getMessage());
        }
    }

    private String toString(Task task) {
        String type = task instanceof Epic ? "EPIC" :
                task instanceof Subtask ? "SUBTASK" : "TASK";

        String epicId = task instanceof Subtask ? "," + ((Subtask) task).getEpicId() : "";

        return String.join(",",
                task.getId(),
                type,
                task.getName(),
                task.getStatus().toString(),
                task.getDescription() + epicId
        );
    }

    private static Task fromString(String value) {
        String[] parts = value.split(",");
        String id = parts[0];
        String type = parts[1];
        String name = parts[2];
        Status status = Status.valueOf(parts[3]);
        String description = parts[4];

        Task task;
        switch (type) {
            case "TASK":
                task = new Task(name, description, status);
                break;
            case "EPIC":
                task = new Epic(name, description);
                task.setStatus(status);
                break;
            case "SUBTASK":
                String epicId = parts[5];
                task = new Subtask(name, description, epicId);
                task.setStatus(status);
                break;
            default:
                throw new IllegalArgumentException("Unknown task type: " + type);
        }
        task.setId(id);
        return task;
    }

    private String historyToString(List<Task> history) {
        StringBuilder sb = new StringBuilder();
        for (Task task : history) {
            sb.append(task.getId()).append(",");
        }
        return sb.length() > 0 ? sb.substring(0, sb.length() - 1) : "";
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            if (line == null) {
                return manager;
            }

            while ((line = reader.readLine()) != null) {
                if (line.isEmpty()) {
                    break;
                }
                Task task = fromString(line);
                if (task instanceof Epic) {
                    manager.addEpic((Epic) task);
                } else if (task instanceof Subtask) {
                    manager.addSubtask((Subtask) task);
                } else {
                    manager.addTask(task);
                }
            }

            
            String historyLine = reader.readLine();
            if (historyLine != null && !historyLine.isEmpty()) {
                for (String id : historyLine.split(",")) {
                    Task task = manager.getTaskById(id);
                    if (task == null) {
                        task = manager.getEpicById(id);
                    }
                    if (task == null) {
                        task = manager.getSubtaskById(id);
                    }
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Could not load from file: " + e.getMessage());
        }
        return manager;
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void deleteTaskById(String id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteEpicById(String id) {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public void deleteSubtaskById(String id) {
        super.deleteSubtaskById(id);
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }

    @Override
    public Task getTaskById(String id) {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public Epic getEpicById(String id) {
        Epic epic = super.getEpicById(id);
        save();
        return epic;
    }

    @Override
    public Subtask getSubtaskById(String id) {
        Subtask subtask = super.getSubtaskById(id);
        save();
        return subtask;
    }
}

class ManagerSaveException extends RuntimeException {
    public ManagerSaveException(String message) {
        super(message);
    }
}
