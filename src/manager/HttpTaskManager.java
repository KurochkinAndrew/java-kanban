package manager;

import Http.KVServer;
import Http.KVTaskClient;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import org.w3c.dom.NameList;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.TreeSet;

public class HttpTaskManager extends FileBackedTasksManager{
    private String url;
    private KVTaskClient kvTaskClient;
    private Gson gson = new Gson();

    public HttpTaskManager(String url) throws IOException, InterruptedException {
       this.url = url;
        kvTaskClient = new KVTaskClient(url);
    }

    @Override
    public void makeNewTask(Object Task) {
        super.makeNewTask(Task);
        saveToServer();
    }

    @Override
    public void makeNewSubtask(Subtask task, int epicID) {
        super.makeNewSubtask(task, epicID);
        saveToServer();
    }

    @Override
    public Task getTaskByID(int ID) {
        Task task = super.getTaskByID(ID);
        saveToServer();
        return task;
    }

    @Override
    public Epic getEpicByID(int ID) {
        Epic task = super.getEpicByID(ID);
        saveToServer();
        return task;
    }

    @Override
    public Subtask getSubtaskByID(int ID) {
        Subtask task = super.getSubtaskByID(ID);
        saveToServer();
        return task;
    }

    public void saveToServer(){
        try {
            kvTaskClient.put("condition", gson.toJson(getPrioritizedTasks()));
            kvTaskClient.put("history", gson.toJson(historyToString(historyManager)));
        } catch (IOException | InterruptedException e){
            System.out.println(e.getMessage());
        }
    }


    public void loadFromServer(){
        try {
            String condition = kvTaskClient.load("condition");
            String history = gson.fromJson(kvTaskClient.load("history"), String.class);
            System.out.println(history);
            removeAll();
            Type listType = new TypeToken<ArrayList<Task>>(){}.getType();
            ArrayList<Task> tasks = gson.fromJson(condition, listType);
            for (Task task: tasks){
                if (task.getClass() == Subtask.class){
                    Subtask subtask = (Subtask) task;
                    makeNewSubtask(subtask, subtask.getEpicOwnerID());
                } else {
                    makeNewTask(task);
                }
            }
            for (int ID: historyFromString(history)){
                if (getTaskByIdWithoutHistoryAdd(ID) != null) {
                    historyManager.add(getTaskByIdWithoutHistoryAdd(ID));
                } else if (getSubtaskByIdWithoutHistoryAdd(ID) != null) {
                    historyManager.add(getSubtaskByIdWithoutHistoryAdd(ID));
                } else {
                    historyManager.add(getEpicByIdWithoutHistoryAdd(ID));
                }
            }
        } catch (IOException | InterruptedException e){
            System.out.println(e.getMessage());
        }

    }
}
