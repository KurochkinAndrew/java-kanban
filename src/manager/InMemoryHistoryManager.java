package manager;

import tasks.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {
    private ArrayList<Task> history = new ArrayList<>();

    public ArrayList getHistory() {
        return history;
    }

    public void add(Task task){

    };


}
