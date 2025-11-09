package sprint6.manager;

import sprint6.task.Task;
import sprint6.task.Epic;
import sprint6.task.Subtask;
import sprint6.task.Status;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {
    protected final HistoryManager historyManager;
    private final Map<String, Task> tasks;
    private final Map<String, Epic> epics;
    private final Map<String, Subtask> subtasks;
    private final Set<Task> prioritizedTasks; // TreeSet для хранения отсортированных задач

    public InMemoryTaskManager() {
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subtasks = new HashMap<>();
        historyManager = Managers.getDefaultHistory();


        prioritizedTasks = new TreeSet<>((t1, t2) -> {
            if (t1.getStartTime() == null && t2.getStartTime() == null) {
                return t1.getId().compareTo(t2.getId());
            }
            if (t1.getStartTime() == null) {
                return 1;
            }
            if (t2.getStartTime() == null) {
                return -1;
            }
            return t1.getStartTime().compareTo(t2.getStartTime());
        });
    }

    @Override
    public void addTask(Task task) {
        if (task != null) {
            if (validateTaskTime(task)) {
                tasks.put(task.getId(), task);
                if (task.getStartTime() != null) {
                    prioritizedTasks.add(task);
                }
            } else {
                throw new IllegalStateException("Задача пересекается по времени с существующими задачами");
            }
        }
    }

    @Override
    public void addEpic(Epic epic) {
        if (epic != null) {
            epics.put(epic.getId(), epic);
            updateEpicStatus(epic);
            updateEpicTimeFields(epic);
        }
    }

    @Override
    public void addSubtask(Subtask subtask) {
        if (subtask != null && epics.containsKey(subtask.getEpicId())) {
            if (validateTaskTime(subtask)) {
                subtasks.put(subtask.getId(), subtask);
                Epic epic = epics.get(subtask.getEpicId());
                epic.addSubtaskId(subtask.getId());
                updateEpicStatus(epic);
                updateEpicTimeFields(epic);
                if (subtask.getStartTime() != null) {
                    prioritizedTasks.add(subtask);
                }
            } else {
                throw new IllegalStateException("Подзадача пересекается по времени с существующими задачами");
            }
        }
    }

    @Override
    public Task getTaskById(String id) {
        Task task = tasks.get(id);
        if (task != null) {
            historyManager.add(task);
        }
        return task;
    }

    @Override
    public Epic getEpicById(String id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            historyManager.add(epic);
        }
        return epic;
    }

    @Override
    public Subtask getSubtaskById(String id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            historyManager.add(subtask);
        }
        return subtask;
    }

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public List<Subtask> getEpicSubtasks(String epicId) {
        Epic epic = epics.get(epicId);
        if (epic == null) {
            return Collections.emptyList();
        }
        return epic.getSubtaskIds().stream()
                .map(subtasks::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public void updateTask(Task task) {
        if (task != null && tasks.containsKey(task.getId())) {
            Task oldTask = tasks.get(task.getId());
            if (validateTaskTime(task) || (oldTask.getStartTime() != null && task.getStartTime() != null &&
                    oldTask.getStartTime().equals(task.getStartTime()) &&
                    oldTask.getDuration().equals(task.getDuration()))) {

                if (oldTask.getStartTime() != null) {
                    prioritizedTasks.remove(oldTask);
                }

                tasks.put(task.getId(), task);

                if (task.getStartTime() != null) {
                    prioritizedTasks.add(task);
                }
            } else {
                throw new IllegalStateException("Задача пересекается по времени с существующими задачами");
            }
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epic != null && epics.containsKey(epic.getId())) {
            Epic existingEpic = epics.get(epic.getId());
            epic.getSubtaskIds().addAll(existingEpic.getSubtaskIds());
            epics.put(epic.getId(), epic);
            updateEpicStatus(epic);
            updateEpicTimeFields(epic);
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtask != null && subtasks.containsKey(subtask.getId())) {
            Subtask oldSubtask = subtasks.get(subtask.getId());
            if (validateTaskTime(subtask) || (oldSubtask.getStartTime() != null && subtask.getStartTime() != null &&
                    oldSubtask.getStartTime().equals(subtask.getStartTime()) &&
                    oldSubtask.getDuration().equals(subtask.getDuration()))) {

                if (oldSubtask.getStartTime() != null) {
                    prioritizedTasks.remove(oldSubtask);
                }

                subtasks.put(subtask.getId(), subtask);

                if (subtask.getStartTime() != null) {
                    prioritizedTasks.add(subtask);
                }

                Epic epic = epics.get(subtask.getEpicId());
                if (epic != null) {
                    updateEpicStatus(epic);
                    updateEpicTimeFields(epic);
                }
            } else {
                throw new IllegalStateException("Подзадача пересекается по времени с существующими задачами");
            }
        }
    }

    @Override
    public void deleteTaskById(String id) {
        Task task = tasks.remove(id);
        if (task != null && task.getStartTime() != null) {
            prioritizedTasks.remove(task);
        }
        historyManager.remove(id);
    }

    @Override
    public void deleteEpicById(String id) {
        Epic epic = epics.remove(id);
        if (epic != null) {
            for (String subtaskId : epic.getSubtaskIds()) {
                Subtask subtask = subtasks.remove(subtaskId);
                if (subtask != null && subtask.getStartTime() != null) {
                    prioritizedTasks.remove(subtask);
                }
                historyManager.remove(subtaskId);
            }
            historyManager.remove(id);
        }
    }

    @Override
    public void deleteSubtaskById(String id) {
        Subtask subtask = subtasks.remove(id);
        if (subtask != null) {
            if (subtask.getStartTime() != null) {
                prioritizedTasks.remove(subtask);
            }

            Epic epic = epics.get(subtask.getEpicId());
            if (epic != null) {
                epic.removeSubtaskId(id);
                updateEpicStatus(epic);
                updateEpicTimeFields(epic);
            }
            historyManager.remove(id);
        }
    }

    @Override
    public void deleteAllTasks() {
        tasks.values().forEach(task -> {
            if (task.getStartTime() != null) {
                prioritizedTasks.remove(task);
            }
            historyManager.remove(task.getId());
        });
        tasks.clear();
    }

    @Override
    public void deleteAllEpics() {
        epics.values().forEach(epic -> historyManager.remove(epic.getId()));

        subtasks.values().forEach(subtask -> {
            if (subtask.getStartTime() != null) {
                prioritizedTasks.remove(subtask);
            }
            historyManager.remove(subtask.getId());
        });

        subtasks.clear();
        epics.clear();
    }

    @Override
    public void deleteAllSubtasks() {
        subtasks.values().forEach(subtask -> {
            if (subtask.getStartTime() != null) {
                prioritizedTasks.remove(subtask);
            }
            historyManager.remove(subtask.getId());
        });

        subtasks.clear();

        epics.values().forEach(epic -> {
            epic.clearSubtaskIds();
            updateEpicStatus(epic);
            updateEpicTimeFields(epic);
        });
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    private boolean checkTasksOverlap(Task task1, Task task2) {
        if (task1.getStartTime() == null || task2.getStartTime() == null) {
            return false;
        }

        LocalDateTime start1 = task1.getStartTime();
        LocalDateTime end1 = task1.getEndTime();
        LocalDateTime start2 = task2.getStartTime();
        LocalDateTime end2 = task2.getEndTime();

        return !(end1.isBefore(start2) || start1.isAfter(end2));
    }

    private boolean validateTaskTime(Task newTask) {
        if (newTask.getStartTime() == null) {
            return true;
        }


        return getPrioritizedTasks().stream()
                .filter(existingTask -> !existingTask.getId().equals(newTask.getId())) // исключаем саму задачу при обновлении
                .noneMatch(existingTask -> checkTasksOverlap(newTask, existingTask));
    }

    private void updateEpicStatus(Epic epic) {
        if (epic.getSubtaskIds().isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
        }

        boolean allDone = true;
        boolean allNew = true;

        for (String subtaskId : epic.getSubtaskIds()) {
            Subtask subtask = subtasks.get(subtaskId);
            if (subtask == null) continue;

            Status subtaskStatus = subtask.getStatus();
            if (subtaskStatus != Status.DONE) {
                allDone = false;
            }
            if (subtaskStatus != Status.NEW) {
                allNew = false;
            }
        }

        if (allDone) {
            epic.setStatus(Status.DONE);
        } else if (allNew) {
            epic.setStatus(Status.NEW);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }

    private void updateEpicTimeFields(Epic epic) {
        List<Subtask> epicSubtasks = getEpicSubtasks(epic.getId());

        if (epicSubtasks.isEmpty()) {
            epic.setStartTime(null);
            epic.setDuration(Duration.ZERO);
            epic.setEndTime(null);
            return;
        }

        LocalDateTime earliestStart = epicSubtasks.stream()
                .map(Subtask::getStartTime)
                .filter(Objects::nonNull)
                .min(LocalDateTime::compareTo)
                .orElse(null);

        LocalDateTime latestEnd = epicSubtasks.stream()
                .map(Subtask::getEndTime)
                .filter(Objects::nonNull)
                .max(LocalDateTime::compareTo)
                .orElse(null);

        Duration totalDuration = epicSubtasks.stream()
                .map(Subtask::getDuration)
                .reduce(Duration.ZERO, Duration::plus);

        epic.setStartTime(earliestStart);
        epic.setDuration(totalDuration);
        epic.setEndTime(latestEnd);
    }
}