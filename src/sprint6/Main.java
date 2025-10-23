package sprint6;

import sprint6.manager.FileBackedTaskManager;
import sprint6.task.Epic;
import sprint6.task.Status;
import sprint6.task.Subtask;
import sprint6.task.Task;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        File file = new File("tasks.csv");
        FileBackedTaskManager manager = new FileBackedTaskManager(file);

        // Создаем задачи
        Task task = new Task("Task 1", "Description 1", Status.NEW);
        manager.addTask(task);

        Epic epic = new Epic("Epic 1", "Epic Description 1");
        manager.addEpic(epic);

        Subtask subtask = new Subtask("Subtask 1", "Subtask Description 1", epic.getId());
        manager.addSubtask(subtask);

        // Получаем задачи для создания истории
        manager.getTaskById(task.getId());
        manager.getEpicById(epic.getId());
        manager.getSubtaskById(subtask.getId());

        // Создаем новый менеджер из того же файла
        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(file);

        // Проверяем, что все задачи загружены
        System.out.println("Loaded tasks: " + loadedManager.getAllTasks().size());
        System.out.println("Loaded epics: " + loadedManager.getAllEpics().size());
        System.out.println("Loaded subtasks: " + loadedManager.getAllSubtasks().size());
        System.out.println("Loaded history: " + loadedManager.getHistory().size());
    }
}