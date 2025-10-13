package sprint6.manager;

import sprint6.task.Task;
import sprint6.task.Epic;
import sprint6.task.Subtask;

import java.util.List;

public interface TaskManager {
    void addTask(Task task);
    void addEpic(Epic epic);
    void addSubtask(Subtask subtask);

    Task getTaskById(String id);
    Epic getEpicById(String id);
    Subtask getSubtaskById(String id);

    List<Task> getAllTasks();
    List<Epic> getAllEpics();
    List<Subtask> getAllSubtasks();
    List<Subtask> getEpicSubtasks(String epicId);

    void updateTask(Task task);
    void updateEpic(Epic epic);
    void updateSubtask(Subtask subtask);

    void deleteTaskById(String id);
    void deleteEpicById(String id);
    void deleteSubtaskById(String id);

    void deleteAllTasks();
    void deleteAllEpics();
    void deleteAllSubtasks();

    List<Task> getHistory();
}