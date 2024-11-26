public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        // Создание задач
        Task task1 = new Task("Задача 1", "Описание 1", TaskStatus.NEW);
        Task task2 = new Task("Задача 2", "Описание 2", TaskStatus.NEW);

        taskManager.addTask(task1);
        taskManager.addTask(task2);

        // Создание эпиков
        Epic epic1 = new Epic("Эпик 1", "Описание 1");
        Epic epic2 = new Epic("Эпик 2", "Описание 2");

        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);

        // Создание подзадач
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание  1", TaskStatus.NEW, epic1.getId());
        Subtask subtask2 = new Subtask("Подзадача 2", "Описани 2", TaskStatus.NEW, epic1.getId());
        Subtask subtask3 = new Subtask("Подзадача 3", "Описание 3", TaskStatus.NEW, epic2.getId());

        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        taskManager.addSubtask(subtask3);

        // Вывод всех задач
        System.out.println("Список задач:");
        System.out.println(taskManager.getAllTasks());
        System.out.println("Список эпиков:");
        System.out.println(taskManager.getAllEpics());
        System.out.println("Список подзадач:");
        System.out.println(taskManager.getAllSubtasks());

        // Изменение статусов
        task1.setStatus(TaskStatus.IN_PROGRESS);
        subtask1.setStatus(TaskStatus.DONE);
        subtask2.setStatus(TaskStatus.DONE);
        subtask3.setStatus(TaskStatus.IN_PROGRESS);

        taskManager.updateTask(task1);
        taskManager.updateSubtask(subtask1);
        taskManager.updateSubtask(subtask2);
        taskManager.updateSubtask(subtask3);

        // Проверка статусов эпиков
        System.out.println("\nПосле изменения статусов:");
        System.out.println("Список задач:");
        System.out.println(taskManager.getAllTasks());
        System.out.println("Список эпиков:");
        System.out.println(taskManager.getAllEpics());
        System.out.println("Список подзадач:");
        System.out.println(taskManager.getAllSubtasks());

        // Удаление задачи и эпика
        taskManager.removeTask(task1.getId());
        taskManager.removeEpic(epic1.getId());

        System.out.println("\nПосле удаления:");
        System.out.println("Список задач:");
        System.out.println(taskManager.getAllTasks());
        System.out.println("Список эпиков:");
        System.out.println(taskManager.getAllEpics());
        System.out.println("Список подзадач:");
        System.out.println(taskManager.getAllSubtasks());
    }
}