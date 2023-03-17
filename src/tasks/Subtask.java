package tasks;

import manager.Status;

public class Subtask extends Task {
    private int epicOwnerID;

    public Subtask(String name, String description, Status status) {
        super(name, description, status);
        super.setType(TypeOfTask.SUBTASK);
    }
    public Subtask(String name, String description, Status status, int epicOwnerID) {
        super(name, description, status);
        this.epicOwnerID = epicOwnerID;
        super.setType(TypeOfTask.SUBTASK);
    }
    public Subtask(String name, String description, Status status, TypeOfTask type, int epicOwnerID) {
        super(name, description, status, type);
        this.epicOwnerID = epicOwnerID;
    }

    public int getEpicOwnerID() {
        return epicOwnerID;
    }

    public void setEpicOwnerID(int epicOwnerID) {
        this.epicOwnerID = epicOwnerID;
    }

    @Override
    public String toString(){
        return super.toString() + epicOwnerID;
    }
}
