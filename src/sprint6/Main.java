package sprint6;

import sprint6.manager.TaskManager;
import sprint6.manager.Managers;
import sprint6.task.Task;
import sprint6.task.Epic;
import sprint6.task.Subtask;
import sprint6.task.Status;

import java.util.List;

public class Main {
    private static TaskManager taskManager;

    private static void printHistory(String actionDescription) {
        System.out.println("\n" + actionDescription);
        System.out.println("История просмотров:");
        List<Task> history = taskManager.getHistory();
        if (history.isEmpty()) {
            System.out.println("История пуста");
        } else {
            for (Task task : history) {
                System.out.println("- " + task.getId() + ": " + task.getName());
            }
        }
        System.out.println("------------------------");
    }

    public static void main(String[] args) {
        taskManager = Managers.getDefault();

        // 1. Создаем две задачи
        Task task1 = new Task("Задача 1", "Описание задачи 1", Status.NEW);
        Task task2 = new Task("Задача 2", "Описание задачи 2", Status.IN_PROGRESS);
        taskManager.addTask(task1);
        taskManager.addTask(task2);

        // 2. Создаем эпик с тремя подзадачами
        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1");
        taskManager.addEpic(epic1);

        Subtask subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1", epic1.getId());
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание подзадачи 2", epic1.getId());
        Subtask subtask3 = new Subtask("Подзадача 3", "Описание подзадачи 3", epic1.getId());

        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        taskManager.addSubtask(subtask3);

        // 3. Создаем эпик без подзадач
        Epic epic2 = new Epic("Эпик 2", "Описание эпика 2");
        taskManager.addEpic(epic2);

        // 4. Просматриваем задачи в разном порядке
        taskManager.getTaskById(task1.getId());
        taskManager.getEpicById(epic1.getId());
        taskManager.getSubtaskById(subtask1.getId());
        printHistory("После первой серии просмотров");

        taskManager.getTaskById(task2.getId());
        taskManager.getSubtaskById(subtask2.getId());
        taskManager.getTaskById(task1.getId()); // Повторный просмотр task1
        printHistory("После повторного просмотра task1");

        // 5. Удаляем задачу
        taskManager.deleteTaskById(task1.getId());
        printHistory("После удаления задачи 1");

        // 6. Удаляем эпик с подзадачами
        taskManager.deleteEpicById(epic1.getId());
        printHistory("После удаления эпика 1");

        // Итоговое состояние
        System.out.println("\nИтоговое состояние:");
        System.out.println("Задачи: " + taskManager.getAllTasks().size());
        System.out.println("Эпики: " + taskManager.getAllEpics().size());
        System.out.println("Подзадачи: " + taskManager.getAllSubtasks().size());
    }
}