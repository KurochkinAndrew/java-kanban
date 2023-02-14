package manager;

import tasks.*;

import java.util.LinkedList;

public class InMemoryHistoryManager implements HistoryManager {
    private LinkedList<Task> history = new LinkedList<>();

    @Override
    public LinkedList<Task> getHistory() {
        return history;
    }

    @Override
    public void add(Task task) {
        if (history.size() >= 10) {
            history.removeFirst();
        }
        history.add(task);
    }


}
