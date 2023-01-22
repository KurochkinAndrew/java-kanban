import java.util.HashMap;
import java.util.ArrayList;

public class TaskManager {
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();
    int newID = 0;

    ArrayList getAllTasks() {
        ArrayList<String> allTasks = new ArrayList<>();
        for (Task task : tasks.values()) {
            allTasks.add(task.name);
        }
        for (Epic task : epics.values()) {
            allTasks.add(task.name);
        }
        for (Subtask task : subtasks.values()) {
            allTasks.add(task.name);
        }
        return allTasks;
    }

    void removeAllTasks() {
        tasks.clear();
        epics.clear();
        subtasks.clear();
    }

    void makeNewTask(Object task) {
        if (task.getClass() == Task.class) {
            tasks.put(newID, (Task) task);
            tasks.get(newID).ID = newID;
            newID++;
        } else if (task.getClass() == Epic.class) {
            epics.put(newID, (Epic) task);
            epics.get(newID).ID = newID;
            newID++;
        }
    }

    Object getTask(int ID) {
        Object result = new Object();
        if (tasks.containsKey(ID)) {
            result = tasks.get(ID);
        } else if (epics.containsKey(ID)) {
            result = epics.get(ID);
        } else if (subtasks.containsKey(ID)) {
            result = subtasks.get(ID);
        }
        return result;
    }

    void makeNewSubtask(Object task, int epicID) {
        if (task.getClass() == Subtask.class) {
            subtasks.put(newID, (Subtask) task);
            epics.get(epicID).addSubtask(newID, task);
            subtasks.get(newID).ID = newID;
            subtasks.get(newID).setEpicOwnerID(epicID);
            newID++;
            epics.get(epicID).setStatus(calculateStatusOfEpic(epicID));
        }
    }

    void refreshTask(Object task, int ID) {
        if (tasks.containsKey(ID)) {
            tasks.put(ID, (Task) task);
        } else if (epics.containsKey(ID)) {
            epics.put(ID, (Epic) task);
        } else if (subtasks.containsKey(ID)) {
            subtasks.put(ID, (Subtask) task);
            epics.get(subtasks.get(ID).getEpicOwnerID()).addSubtask(ID, (Subtask) task);
            epics.get(subtasks.get(ID).getEpicOwnerID()).
                    setStatus(calculateStatusOfEpic(subtasks.get(ID).getEpicOwnerID()));
        }
    }

    void removeTaskByID(int ID) {
        if (tasks.containsKey(ID)) {
            tasks.remove(ID);
        } else if (epics.containsKey(ID)) {
            epics.remove(ID);
        } else if (subtasks.containsKey(ID)) {
            epics.get(subtasks.get(ID).getEpicOwnerID()).removeSubtask(ID);
            epics.get(subtasks.get(ID).getEpicOwnerID()).
                    setStatus(calculateStatusOfEpic(subtasks.get(ID).getEpicOwnerID()));
            subtasks.remove(ID);
        }
    }

    ArrayList getSubtasksOfEpic(int ID) {
        ArrayList<String> subtasksOfEpic = new ArrayList<>();
        for (Subtask task : epics.get(ID).getSubtasks().values()) {
            subtasksOfEpic.add(task.name);
        }
        return subtasksOfEpic;
    }

    String calculateStatusOfEpic(int ID) {
        String status = "IN_PROGRESS";
        boolean isAllSubtasksDone = true;
        boolean isAllSubtasksNew = true;
        if (epics.get(ID).getSubtasks().isEmpty()) {
            isAllSubtasksDone = false;
        }
        for (Subtask subtask : epics.get(ID).getSubtasks().values()) {
            if (subtask.status.equals("DONE")) {
                isAllSubtasksDone = isAllSubtasksDone & true;
            } else {
                isAllSubtasksDone = isAllSubtasksDone & false;
            }
        }
        for (Subtask subtask : epics.get(ID).getSubtasks().values()) {
            if (subtask.status.equals("NEW")) {
                isAllSubtasksNew = isAllSubtasksNew & true;
            } else {
                isAllSubtasksNew = isAllSubtasksNew & false;
            }
        }
        if (isAllSubtasksDone) {
            status = "DONE";
        } else if (isAllSubtasksNew) {
            status = "NEW";
        }
        return status;
    }
}

