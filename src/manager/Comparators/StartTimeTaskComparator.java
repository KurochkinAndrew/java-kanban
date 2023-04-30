package manager.Comparators;

import tasks.Task;

import java.util.Comparator;

public class StartTimeTaskComparator implements Comparator<Task> {
    @Override
    public int compare(Task task1, Task task2) {
        if (task1.getStartTime() != null && task2.getStartTime() != null) {
            if ((task1.getStatus() == task2.getStatus()) && (task1.getType() == task2.getType()) &&
                    (task1.getName().equals(task2.getName())) &&
                    (task1.getDescription().equals(task2.getDescription())) &&
                    (task1.getStartTime().isEqual(task2.getStartTime()))) {
                return 0;
            } else {
                if (task1.getStartTime().isAfter(task2.getStartTime())) {
                    return 1;
                } else {
                    return -1;
                }
            }
        } else if ((task1.getStartTime() != null) && (task2.getStartTime() == null)){
            return -1;
        } else {
            return 1;
        }
    }
}
