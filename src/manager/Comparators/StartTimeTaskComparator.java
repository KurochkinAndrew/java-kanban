package manager.Comparators;

import tasks.Task;

import java.time.LocalDateTime;
import java.util.Comparator;

public class StartTimeTaskComparator implements Comparator<Task> {
    @Override
    public int compare(Task task1, Task task2) {
        if (task1.getStartTime() != null && task2.getStartTime() != null) {
            return task1.getStartTime().compareTo(task2.getStartTime());
        } else if ((task1.getStartTime() != null) && (task2.getStartTime() == null)){
            return -1;
        } else {
            return 1;
        }
    }
}
