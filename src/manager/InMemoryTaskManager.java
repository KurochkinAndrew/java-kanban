package manager;

import manager.Comparators.StartTimeTaskComparator;
import manager.Comparators.TaskComparator;
import manager.History.HistoryManager;
import tasks.*;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.TreeSet;

public class InMemoryTaskManager implements TaskManager {
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private TreeSet<Task> prioritizedTasks = new TreeSet<>(new StartTimeTaskComparator());
    protected HistoryManager historyManager = Managers.getDefaultHistory();
    private int newID = 0;

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    public InMemoryTaskManager() {
    }

    public void removefromhistory(int ID) {
        historyManager.remove(ID);
    }

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

    public ArrayList getHistoryAsList() {
        ArrayList<String> history = new ArrayList<>();
        for (Task task : historyManager.getHistory()) {
           history.add(task.getName());
        }
        return history;
    }

    @Override
    public ArrayList getAllEpics() {
        ArrayList<String> allTasks = new ArrayList<>();
        for (Epic task : epics.values()) {
            allTasks.add(task.getName());
        }
        return allTasks;
    }

    public HashMap<Integer, Epic> getMapOfEpics() {
        return epics;
    }

    public HashMap<Integer, Subtask> getMapOfSubtasks() {
        return subtasks;
    }

    public HashMap<Integer, Task> getMapOfTasks() {
        return tasks;
    }

    @Override
    public ArrayList getAllSubtasks() {
        ArrayList<String> allTasks = new ArrayList<>();
        for (Subtask task : subtasks.values()) {
            allTasks.add(task.getName());
        }
        return allTasks;
    }

    public TreeSet<Task> getTreeSetOfTasks() {
        TreeSet<Task> treeSet = new TreeSet<Task>(new TaskComparator());
        treeSet.addAll(tasks.values());
        treeSet.addAll(epics.values());
        treeSet.addAll(subtasks.values());
        return treeSet;
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

    public void removeAll(){
        tasks.clear();
        epics.clear();
        subtasks.clear();
        newID = 0;
    }

    @Override
    public void makeNewTask(Object task) {
        Task t = (Task) task;
        if (!isOverlappedInTime(t)) {
            if (task.getClass() == Task.class) {
                tasks.put(newID, (Task) task);
                prioritizedTasks.add((Task) task);
                tasks.get(newID).setID(newID);
                newID++;
            } else if (task.getClass() == Epic.class) {
                epics.put(newID, (Epic) task);
                prioritizedTasks.add((Task) task);
                epics.get(newID).setID(newID);
                epics.get(newID).setStatus(Status.NEW);
                newID++;
            }
        } else {
            System.out.println("Задачи не могут пересекаться по времени. Задача \"" + t.getName()
                    + "\" не создана.");
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
    public TreeSet<Task> getPrioritizedTasks() {
        return prioritizedTasks;
    }


    public Task getTaskByIdWithoutHistoryAdd(int ID) {
        return tasks.get(ID);
    }


    public Epic getEpicByIdWithoutHistoryAdd(int ID) {
        return epics.get(ID);
    }


    public Subtask getSubtaskByIdWithoutHistoryAdd(int ID) {
        return subtasks.get(ID);
    }

    @Override
    public void makeNewSubtask(Subtask task, int epicID) {
        prioritizedTasks.remove(epics.get(epicID));
            if (!isOverlappedInTime(task)) {
                subtasks.put(newID, task);
                prioritizedTasks.add(task);
                epics.get(epicID).addSubtask(newID, task);
                subtasks.get(newID).setID(newID);
                subtasks.get(newID).setEpicOwnerID(epicID);
                newID++;
                epics.get(epicID).setStatus(calculateStatusOfEpic(epicID));
            } else {
                System.out.println("Задачи не могут пересекаться по времени. Задача \"" + task.getName()
                        + "\" не создана.");
            }
            prioritizedTasks.add(epics.get(epicID));
    }

    @Override
    public void refreshTask(Task task, int ID) {
        if (!isOverlappedInTime(task)) {
            if (tasks.containsKey(ID)) {
                tasks.put(ID, task);
                task.setID(ID);
            }
        } else {
            System.out.println("Задачи не могут пересекаться по времени. Задача \"" + task.getName()
                    + "\" не создана.");
        }
    }

    @Override
    public void refreshEpic(Epic task, int ID) {
        if (!isOverlappedInTime(task)) {
            if (epics.containsKey(ID)) {
                epics.put(ID, task);
                task.setID(ID);
            }
        } else {
            System.out.println("Задачи не могут пересекаться по времени. Задача \"" + task.getName()
                    + "\" не создана.");
        }
    }

    @Override
    public void refreshSubtask(Subtask task, int ID) {
        if (!isOverlappedInTime(task)) {
            if (subtasks.containsKey(ID)) {
                subtasks.put(ID, task);
                epics.get(subtasks.get(ID).getEpicOwnerID()).addSubtask(ID, task);
                epics.get(subtasks.get(ID).getEpicOwnerID()).
                        setStatus(calculateStatusOfEpic(subtasks.get(ID).getEpicOwnerID()));
            }
        } else {
            System.out.println("Задачи не могут пересекаться по времени. Задача \"" + task.getName()
                    + "\" не создана.");
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

    protected boolean isOverlappedInTime(Task task) {
        if (task.getStartTime() != null) {
            for (Task t2 : getTreeSetOfTasks()) {
                if (t2.getStartTime() == null) {
                    continue;
                }
                boolean startTimeOverlap = (task.getStartTime().isAfter(t2.getStartTime()) &&
                        task.getStartTime().isBefore(t2.getEndTime()));
                boolean endTimeOverlap = task.getEndTime().isAfter(t2.getStartTime()) &&
                        task.getEndTime().isBefore(t2.getEndTime());
                if (startTimeOverlap || endTimeOverlap) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }
}

