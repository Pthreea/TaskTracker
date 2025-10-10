package sprint4;

import sprint4.Manager.TaskManager;
import sprint4.Manager.Managers;
import sprint4.Task.Task;
import sprint4.Task.Subtask;
import sprint4.Task.Epic;
import sprint4.Task.Status;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();

        // Создание задач
        Task task1 = new Task("Task 1", "Описание для Task 1", Status.NEW);
        Task task2 = new Task("Task 2", "Описание для Task 2", Status.IN_PROGRESS);
        taskManager.addTask(task1);
        taskManager.addTask(task2);

        // Создание эпиков
        Epic epic1 = new Epic("Epic 1", "Описание для Epic 1");
        Epic epic2 = new Epic("Epic 2", "Описание для Epic 2");
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);

        // Создание подзадач и связывание с эпиками
        Subtask subtask1 = new Subtask("Subtask 1", "Описание для Subtask 1", epic1.getId());
        Subtask subtask2 = new Subtask("Subtask 2", "Описание для Subtask 2", epic1.getId());
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);

        // Получение и печать задач для демонстрации работы программы
        System.out.println("Все задачи:");
        taskManager.getAllTasks().forEach(System.out::println);

        System.out.println("Все эпики:");
        taskManager.getAllEpics().forEach(System.out::println);

        System.out.println("Все подзадачи:");
        taskManager.getAllSubtasks().forEach(System.out::println);

        // Использование методов получения для записи в историю просмотра
        taskManager.getTaskById(task1.getId());
        taskManager.getEpicById(epic1.getId());
        taskManager.getSubtaskById(subtask1.getId());

        // Вывод истории просмотра
        System.out.println("История просмотров:");
        taskManager.getHistory().forEach(System.out::println);
    }
}