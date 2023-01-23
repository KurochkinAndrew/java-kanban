package tasks;

public class Subtask extends Task {

    private int epicOwnerID;

    public Subtask(String name, String description, String status) {
        super(name, description, status);
    }

    public int getEpicOwnerID() {
        return epicOwnerID;
    }

    public void setEpicOwnerID(int epicOwnerID) {
        this.epicOwnerID = epicOwnerID;
    }
}
