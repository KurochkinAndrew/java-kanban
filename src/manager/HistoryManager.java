package manager;
import tasks.*;

import java.util.ArrayList;

public interface HistoryManager {
    public void add(Task task);
    public ArrayList getHistory();
}
