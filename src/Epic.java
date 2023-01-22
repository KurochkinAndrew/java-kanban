import java.util.HashMap;

public class Epic extends Task {
    private String status = "NEW";
    int ID;

    private HashMap<Integer, Subtask> subtasks = new HashMap<>();

    public Epic(String name, String description) {
        this.name = name;
        this.description = description;
    }

    void addSubtask(int ID, Object subtask) {
        subtasks.put(ID, (Subtask) subtask);
    }

    void removeSubtask(int ID) {
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
