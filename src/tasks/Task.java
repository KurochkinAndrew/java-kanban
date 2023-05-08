package tasks;

import manager.Status;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Task implements Comparable<Task>{
    private String name;
    private String description;
    private Status status;

    private Integer ID;
    protected TypeOfTask type = TypeOfTask.TASK;
    public static DateTimeFormatter startTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy|HH:mm");

    private LocalDateTime startTime;
    private Duration duration;

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

    public Task(String name, String description, Status status, LocalDateTime startTime, Duration duration) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Task(String name, String description, Status status, LocalDateTime startTime, Duration duration, int ID) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
        this.ID = ID;
    }

    public Task(String name, String description, Status status, TypeOfTask type, LocalDateTime startTime, Duration duration) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
        this.type = type;
    }

    public void setType(TypeOfTask type){
        this.type = type;
    }
    public TypeOfTask getType(){
        return type;
    }

    public Integer getID() {
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

    public LocalDateTime getEndTime(){
        return startTime.plus(duration);
    }

    public LocalDateTime getStartTime(){
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public Duration getDuration(){
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    @Override
    public String toString(){
        if (startTime == null || duration == null){
            return String.format("%s,%s,%s,%s,%s,", ID, type, name, status,
                    description);
        }
        return String.format("%s,%s,%s,%s,%s,%s,%s,", ID, type, name, status,
                description, startTime.format(startTimeFormatter), duration);
    }
    @Override
    public int compareTo(Task task){
        return this.ID - task.ID;
    }
}
