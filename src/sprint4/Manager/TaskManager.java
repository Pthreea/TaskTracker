package sprint4.Manager;

import sprint4.Task.Task;
import sprint4.Task.Epic;
import sprint4.Task.Subtask;

import java.util.List;

public interface TaskManager {

    void addTask(Task task);

    void addEpic(Epic epic);

    void addSubtask(Subtask subtask);

    void deleteAllTasks();

    void deleteAllEpics();

    void deleteAllSubtasks();

    Task getTaskById(String id);

    Epic getEpicById(String id);

    Subtask getSubtaskById(String id);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubtask(Subtask subtask);

    void deleteTaskById(String id);

    void deleteEpicById(String id);

    void deleteSubtaskById(String id);

    List<Task> getAllTasks();

    List<Epic> getAllEpics();

    List<Subtask> getAllSubtasks();

    List<Subtask> getAllSubtasksForEpic(String epicId);

    List<Task> getHistory();
}