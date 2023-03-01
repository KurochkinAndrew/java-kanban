package manager.History;

import tasks.*;

import java.util.ArrayList;
import java.util.LinkedList;

public interface HistoryManager {
    void add(Task task);

    void remove(int id);

    ArrayList<Task> getHistory();

}
