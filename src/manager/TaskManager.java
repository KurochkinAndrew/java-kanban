package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.HashMap;
import java.util.ArrayList;

public class TaskManager {
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();
    int newID = 0;

    public ArrayList getAllTasks() {
        ArrayList<String> allTasks = new ArrayList<>();
        for (Task task : tasks.values()) {
            allTasks.add(task.getName());
        }
        return allTasks;
    }

    public ArrayList getAllEpics() {
        ArrayList<String> allTasks = new ArrayList<>();
        for (Epic task : epics.values()) {
            allTasks.add(task.getName());
        }
        return allTasks;
    }

    public ArrayList getAllSubtasks() {
        ArrayList<String> allTasks = new ArrayList<>();
        for (Subtask task : subtasks.values()) {
            allTasks.add(task.getName());
        }
        return allTasks;
    }

    public void removeAllTasks() {
        tasks.clear();
    }

    public void removeAllEpics() {
        epics.clear();
    }

    public void removeAllSubtasks() {
        subtasks.clear();
    }

    public void makeNewTask(Object task) {
        if (task.getClass() == Task.class) {
            tasks.put(newID, (Task) task);
            tasks.get(newID).setID(newID);
            newID++;
        } else if (task.getClass() == Epic.class) {
            epics.put(newID, (Epic) task);
            epics.get(newID).setID(newID);
            epics.get(newID).setStatus("NEW");
            newID++;
        }
    }

    public Task getTaskByID(int ID) {
        Task result = new Task("","");
        if (tasks.containsKey(ID)) {
            result = tasks.get(ID);
        }
        return result;
    }

    public Epic getEpicByID(int ID) {
        Epic result = new Epic("", "");
        if (epics.containsKey(ID)) {
            result = epics.get(ID);
        }
        return result;
    }

    public Subtask getSubtaskByID(int ID) {
        Subtask result = new Subtask("", "", "");
        if (subtasks.containsKey(ID)) {
            result = subtasks.get(ID);
        }
        return result;
    }

    public void makeNewSubtask(Subtask task, int epicID) {
        subtasks.put(newID, task);
        epics.get(epicID).addSubtask(newID, task);
        subtasks.get(newID).setID(newID);
        subtasks.get(newID).setEpicOwnerID(epicID);
        newID++;
        epics.get(epicID).setStatus(calculateStatusOfEpic(epicID));
    }

    public void refreshTask(Task task, int ID) {
        if (tasks.containsKey(ID)) {
            tasks.put(ID, task);
        }
    }

    public void refreshEpic(Epic task, int ID) {
        if (epics.containsKey(ID)) {
            epics.put(ID, task);
        }
    }

    public void refreshSubtask(Subtask task, int ID) {
        if (subtasks.containsKey(ID)) {
            subtasks.put(ID, task);
            epics.get(subtasks.get(ID).getEpicOwnerID()).addSubtask(ID, task);
            epics.get(subtasks.get(ID).getEpicOwnerID()).
                    setStatus(calculateStatusOfEpic(subtasks.get(ID).getEpicOwnerID()));
        }
    }


    public void removeTaskByID(int ID) {
        if (tasks.containsKey(ID)) {
            tasks.remove(ID);
        }
    }

    public void removeEpicByID(int ID) {
        if (epics.containsKey(ID)) {
            epics.remove(ID);
        }
    }

    public void removeSubtaskByID(int ID) {
        if (subtasks.containsKey(ID)) {
            epics.get(subtasks.get(ID).getEpicOwnerID()).removeSubtask(ID);
            epics.get(subtasks.get(ID).getEpicOwnerID()).
                    setStatus(calculateStatusOfEpic(subtasks.get(ID).getEpicOwnerID()));
            subtasks.remove(ID);
        }
    }

    public ArrayList getSubtasksOfEpic(int ID) {
        ArrayList<String> subtasksOfEpic = new ArrayList<>();
        for (Subtask task : epics.get(ID).getSubtasks().values()) {
            subtasksOfEpic.add(task.getName());
        }
        return subtasksOfEpic;
    }

    public String calculateStatusOfEpic(int ID) {
        String status = "IN_PROGRESS";
        boolean isAllSubtasksDone = true;
        boolean isAllSubtasksNew = true;
        if (epics.get(ID).getSubtasks().isEmpty()) {
            isAllSubtasksDone = false;
        }
        for (Subtask subtask : epics.get(ID).getSubtasks().values()) {
            if (subtask.getStatus().equals("DONE")) {
                isAllSubtasksDone = isAllSubtasksDone & true;
            } else {
                isAllSubtasksDone = isAllSubtasksDone & false;
            }
        }
        for (Subtask subtask : epics.get(ID).getSubtasks().values()) {
            if (subtask.getStatus().equals("NEW")) {
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

