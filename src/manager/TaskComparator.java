package manager;

import tasks.Task;

import java.util.Comparator;

public class TaskComparator implements Comparator<Task> {
    @Override
    public int compare(Task task1, Task task2){
        if ((task1.getStatus() == task2.getStatus()) && (task1.getType() == task2.getType()) &&
                (task1.getName().equals(task2.getName())) && (task1.getDescription().equals(task2.getDescription()))){
            return 0;
        } else {
            return task1.getID()-task2.getID();
        }
    }
}
