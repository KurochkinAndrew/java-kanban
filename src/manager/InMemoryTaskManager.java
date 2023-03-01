package manager;

import manager.History.HistoryManager;
import tasks.*;

import java.util.HashMap;
import java.util.ArrayList;

public class InMemoryTaskManager implements TaskManager {
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();
    HistoryManager historyManager = Managers.getDefaultHistory();
    private int newID = 0;

    @Override
    public ArrayList getAllTasks() {
        ArrayList<String> allTasks = new ArrayList<>();
        for (Task task : tasks.values()) {
            allTasks.add(task.getName());
        }
        return allTasks;
    }

    @Override
    public void getHistory() {
        System.out.println("История последних просмотренных задач:");
        for (Task task : historyManager.getHistory()) {
            System.out.println(task.getName());
        }
    }

    @Override
    public ArrayList getAllEpics() {
        ArrayList<String> allTasks = new ArrayList<>();
        for (Epic task : epics.values()) {
            allTasks.add(task.getName());
        }
        return allTasks;
    }

    @Override
    public ArrayList getAllSubtasks() {
        ArrayList<String> allTasks = new ArrayList<>();
        for (Subtask task : subtasks.values()) {
            allTasks.add(task.getName());
        }
        return allTasks;
    }

    @Override
    public void removeAllTasks() {
        tasks.clear();

    }

    @Override
    public void removeAllEpics() {
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void removeAllSubtasks() {
        subtasks.clear();
    }

    @Override
    public void makeNewTask(Object task) {
        if (task.getClass() == Task.class) {
            tasks.put(newID, (Task) task);
            tasks.get(newID).setID(newID);
            newID++;
        } else if (task.getClass() == Epic.class) {
            epics.put(newID, (Epic) task);
            epics.get(newID).setID(newID);
            epics.get(newID).setStatus(Status.NEW);
            newID++;
        }
    }

    @Override
    public Task getTaskByID(int ID) {
        historyManager.add(tasks.get(ID));
        return tasks.get(ID);
    }

    @Override
    public Epic getEpicByID(int ID) {
        historyManager.add(epics.get(ID));
        return epics.get(ID);
    }

    @Override
    public Subtask getSubtaskByID(int ID) {
        historyManager.add(subtasks.get(ID));
        return subtasks.get(ID);
    }

    @Override
    public void makeNewSubtask(Subtask task, int epicID) {
        subtasks.put(newID, task);
        epics.get(epicID).addSubtask(newID, task);
        subtasks.get(newID).setID(newID);
        subtasks.get(newID).setEpicOwnerID(epicID);
        newID++;
        epics.get(epicID).setStatus(calculateStatusOfEpic(epicID));
    }

    @Override
    public void refreshTask(Task task, int ID) {
        if (tasks.containsKey(ID)) {
            tasks.put(ID, task);
        }
    }

    @Override
    public void refreshEpic(Epic task, int ID) {
        if (epics.containsKey(ID)) {
            epics.put(ID, task);
        }
    }

    @Override
    public void refreshSubtask(Subtask task, int ID) {
        if (subtasks.containsKey(ID)) {
            subtasks.put(ID, task);
            epics.get(subtasks.get(ID).getEpicOwnerID()).addSubtask(ID, task);
            epics.get(subtasks.get(ID).getEpicOwnerID()).
                    setStatus(calculateStatusOfEpic(subtasks.get(ID).getEpicOwnerID()));
        }
    }

    @Override
    public void removeTaskByID(int ID) {
        if (tasks.containsKey(ID)) {
            historyManager.remove(ID);
            tasks.remove(ID);
        }
    }

    @Override
    public void removeEpicByID(int ID) {
        ArrayList<Subtask> subtasksNeedToRemove = new ArrayList<>();
        if (epics.containsKey(ID)) {
            for (Subtask task : epics.get(ID).getSubtasks().values()) {
                for (Subtask subtask : subtasks.values()) {
                    if (task.getID() == subtask.getID()) {
                        historyManager.remove(task.getID());
                        subtasksNeedToRemove.add(subtasks.get(task.getID()));
                    }
                }
            }
            for (Subtask subtask : subtasksNeedToRemove) {
                historyManager.remove(subtask.getID());
                subtasks.remove(subtask.getID());
            }
            subtasksNeedToRemove.clear();
            historyManager.remove(ID);
            epics.remove(ID);
        }
    }

    @Override
    public void removeSubtaskByID(int ID) {
        if (subtasks.containsKey(ID)) {
            epics.get(subtasks.get(ID).getEpicOwnerID()).removeSubtask(ID);
            epics.get(subtasks.get(ID).getEpicOwnerID()).
                    setStatus(calculateStatusOfEpic(subtasks.get(ID).getEpicOwnerID()));
            historyManager.remove(ID);
            subtasks.remove(ID);
        }
    }

    @Override
    public ArrayList getSubtasksOfEpic(int ID) {
        ArrayList<String> subtasksOfEpic = new ArrayList<>();
        for (Subtask task : epics.get(ID).getSubtasks().values()) {
            subtasksOfEpic.add(task.getName());
        }
        return subtasksOfEpic;
    }

    @Override
    public Status calculateStatusOfEpic(int ID) {
        Status status = Status.IN_PROGRESS;
        boolean isAllSubtasksDone = true;
        boolean isAllSubtasksNew = true;
        if (epics.get(ID).getSubtasks().isEmpty()) {
            isAllSubtasksDone = false;
        }
        for (Subtask subtask : epics.get(ID).getSubtasks().values()) {
            if (subtask.getStatus().equals(status.DONE)) {
                isAllSubtasksDone = isAllSubtasksDone & true;
            } else {
                isAllSubtasksDone = isAllSubtasksDone & false;
            }
        }
        for (Subtask subtask : epics.get(ID).getSubtasks().values()) {
            if (subtask.getStatus().equals(status.NEW)) {
                isAllSubtasksNew = isAllSubtasksNew & true;
            } else {
                isAllSubtasksNew = isAllSubtasksNew & false;
            }
        }
        if (isAllSubtasksDone) {
            status = status.DONE;
        } else if (isAllSubtasksNew) {
            status = status.NEW;
        }
        return status;
    }
}

