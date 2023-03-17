package tasks;

import manager.Status;

public class Task implements Comparable<Task>{
    private String name;
    private String description;
    private Status status;

    private int ID;
    protected TypeOfTask type = TypeOfTask.TASK;

    public Task(String name, String description, Status status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public Task(String name, String description, Status status, TypeOfTask type) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.type = type;
    }

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
    }
    public void setType(TypeOfTask type){
        this.type = type;
    }
    public TypeOfTask getType(){
        return type;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
    @Override
    public String toString(){
        return String.format("%s,%s,%s,%s,%s,", ID, type, name, status, description);
    }
    @Override
    public int compareTo(Task task){
        return this.ID - task.ID;
    }
}
