package sprint4.manager;

import sprint4.task.Task;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DoublyLinkedList {
    private Node head;
    private Node tail;
    private Map<String, Node> nodeMap;

    private static class Node {
        Task task;
        Node prev;
        Node next;

        Node(Task task) {
            this.task = task;
        }
    }

    public DoublyLinkedList() {
        this.nodeMap = new HashMap<>();
    }

    public void add(Task task) {
        remove(task.getId()); // Удаляем предыдущую версию, если она существует

        Node node = new Node(task);
        if (tail == null) {
            head = node;
        } else {
            tail.next = node;
            node.prev = tail;
        }
        tail = node;
        nodeMap.put(task.getId(), node);
    }

    public void remove(String id) {
        Node node = nodeMap.remove(id);
        if (node == null) {
            return;
        }

        if (node.prev != null) {
            node.prev.next = node.next;
        } else {
            head = node.next;
        }

        if (node.next != null) {
            node.next.prev = node.prev;
        } else {
            tail = node.prev;
        }
    }

    public List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        Node current = head;
        while (current != null) {
            tasks.add(current.task);
            current = current.next;
        }
        return tasks;
    }
}