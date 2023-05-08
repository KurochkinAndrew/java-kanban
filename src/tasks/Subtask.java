package tasks;

import manager.Status;

import java.time.Duration;
import java.time.LocalDateTime;

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
    public Subtask(String name, String description, Status status, int epicOwnerID,
                   LocalDateTime startTime, Duration duration) {
        super(name, description, status, startTime, duration);
        this.epicOwnerID = epicOwnerID;
        super.setType(TypeOfTask.SUBTASK);
    }

    public Subtask(String name, String description, Status status, int epicOwnerID,
                   LocalDateTime startTime, Duration duration, int ID) {
        super(name, description, status, startTime, duration);
        this.epicOwnerID = epicOwnerID;
        super.setType(TypeOfTask.SUBTASK);
        setID(ID);
    }

    public Subtask(String name, String description, Status status,
                   LocalDateTime startTime, Duration duration) {
        super(name, description, status, startTime, duration);
        super.setType(TypeOfTask.SUBTASK);
    }

    public Subtask(String name, String description, Status status, TypeOfTask type, int epicOwnerID,
                   LocalDateTime startTime, Duration duration) {
        super(name, description, status, type, startTime, duration);
        this.epicOwnerID = epicOwnerID;
        super.setType(TypeOfTask.SUBTASK);
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
