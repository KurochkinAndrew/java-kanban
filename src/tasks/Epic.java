package tasks;

import java.util.HashMap;

public class Epic extends Task {
    private String status;

    private HashMap<Integer, Subtask> subtasks = new HashMap<>();

    public Epic(String name, String description) {
        super(name, description);
    }

    public void addSubtask(int subtaskID, Object subtask) {
        subtasks.put(subtaskID, (Subtask) subtask);
    }

    public void removeSubtask(int ID) {
        subtasks.remove(ID);
    }

    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

}
