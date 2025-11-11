package sprint6;

import sprint6.manager.FileBackedTaskManager;
import sprint6.manager.TaskManager;
import sprint6.task.Epic;
import sprint6.task.Status;
import sprint6.task.Subtask;
import sprint6.task.Task;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public static void main(String[] args) {
        System.out.println("Запуск программы управления задачами");
        File file = new File("tasks.csv");
        TaskManager manager = new FileBackedTaskManager(file);
        LocalDateTime now = LocalDateTime.now();

        System.out.println("Создание задач");


        Task task = new Task("Задача", "Описание задачи", Status.NEW);
        task.setId("task-1");
        task.setStartTime(now);
        task.setDuration(Duration.ofMinutes(30));
        manager.addTask(task);
        System.out.println("- Создана задача: " + task.getName() +
                " (начало: " + task.getStartTime().format(FORMATTER) + ")");


        Epic epic = new Epic("Эпик", "Описание эпика");
        epic.setId("epic-1");
        manager.addEpic(epic);
        System.out.println("- Создан эпик: " + epic.getName());

        Subtask subtask = new Subtask("Подзадача", "Описание подзадачи", epic.getId());
        subtask.setId("subtask-1");
        subtask.setStartTime(now.plusHours(1));
        subtask.setDuration(Duration.ofMinutes(45));
        manager.addSubtask(subtask);
        System.out.println("- Создана подзадача: " + subtask.getName() +
                " (начало: " + subtask.getStartTime().format(FORMATTER) + ")");

        System.out.println("Получение задач и формирование истории");
        manager.getTaskById(task.getId());
        manager.getEpicById(epic.getId());
        System.out.println("- Размер истории: " + manager.getHistory().size());

        System.out.println("Задачи по приоритету");
        manager.getPrioritizedTasks().forEach(t ->
                System.out.println("- " + t.getName() + " (начало: " +
                        t.getStartTime().format(FORMATTER) + ")")
        );

        System.out.println("Загрузка из файла");
        TaskManager loadedManager = FileBackedTaskManager.loadFromFile(file);
        System.out.println("- Задач: " + loadedManager.getAllTasks().size());
        System.out.println("- Эпиков: " + loadedManager.getAllEpics().size());
        System.out.println("- Подзадач: " + loadedManager.getAllSubtasks().size());
        System.out.println("- История: " + loadedManager.getHistory().size());

        System.out.println("Тест пересечения задач");
        try {
            Task overlappingTask = new Task("Пересекающаяся задача", "Описание", Status.NEW);
            overlappingTask.setId("task-2");
            overlappingTask.setStartTime(now.plusMinutes(15));
            overlappingTask.setDuration(Duration.ofMinutes(30));
            loadedManager.addTask(overlappingTask);
            System.out.println("- Задача добавлена (ошибка!)");
        } catch (IllegalStateException e) {
            System.out.println("- Ошибка: Задачи пересекаются по времени");
        }

        System.out.println("Программа завершена");
    }
}
