package manager;

import tasks.*;

import java.util.ArrayList;

public interface TaskManager {
    ArrayList getAllTasks();

    ArrayList getAllEpics();

    ArrayList getAllSubtasks();

    void removeAllTasks();

    void removeAllEpics();

    void removeAllSubtasks();

    void makeNewTask(Object task);

    Task getTaskByID(int ID);

    Epic getEpicByID(int ID);

    Subtask getSubtaskByID(int ID);

    void makeNewSubtask(Subtask task, int epicID);

    void refreshTask(Task task, int ID);

    void refreshEpic(Epic task, int ID);

    void refreshSubtask(Subtask task, int ID);

    void removeTaskByID(int ID);

    void removeEpicByID(int ID);

    void removeSubtaskByID(int ID);

    ArrayList getSubtasksOfEpic(int ID);

    Status calculateStatusOfEpic(int ID);

    void getHistory();


}
