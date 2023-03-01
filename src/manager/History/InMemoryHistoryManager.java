package manager.History;

import tasks.*;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryHistoryManager implements HistoryManager {

    private CustomLinkedList historyLinkedList = new CustomLinkedList();

    @Override
    public void remove(int id) {
        historyLinkedList.removeNode(historyLinkedList.nodes.get(id));
    }

    @Override
    public ArrayList<Task> getHistory() {
        return historyLinkedList.getTasks();
    }

    @Override
    public void add(Task task) {

        historyLinkedList.removeNode(new Node(null, task, null));
        historyLinkedList.linkLast(task);

    }

    class CustomLinkedList {
        HashMap<Integer, Node> nodes = new HashMap<>();
        private Node head;
        private Node tail;

        public void linkLast(Task element) {
            final Node oldTail = tail;
            final Node newNode = new Node(oldTail, element, null);
            tail = newNode;
            nodes.put(newNode.data.getID(), newNode);
            if (oldTail == null) head = newNode;
            else oldTail.next = newNode;
        }

        public ArrayList<Task> getTasks() {
            ArrayList<Task> tasks = new ArrayList<>();
            Node node = head;
            while (node != null) {
                tasks.add(node.data);
                node = node.next;
            }
            return tasks;
        }

        public void removeNode(Node node) {
            if (node != null) {
                if (nodes.containsKey(node.data.getID())) {
                    if (nodes.get(node.data.getID()).prev != null) {
                        nodes.get(node.data.getID()).prev.next = nodes.get(node.data.getID()).next;
                    } else {
                        head = nodes.get(node.data.getID()).next;
                    }
                    if (nodes.get(node.data.getID()).next != null) {
                        nodes.get(node.data.getID()).next.prev = nodes.get(node.data.getID()).prev;
                    } else {
                        tail = nodes.get(node.data.getID()).prev;
                    }
                }
            }
        }


    }


}
