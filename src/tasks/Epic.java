package tasks;

import java.util.HashMap;


public class Epic extends Task {

    private HashMap<Integer, Subtask> subtasks = new HashMap<>();

    public Epic(String name, String description) {
        super(name, description);
    }

    public void addSubtask(int subtaskID, Subtask subtask) {
        subtasks.put(subtaskID, subtask);
    }

    public void removeSubtask(int ID) {
        subtasks.remove(ID);
    }

    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }


}
