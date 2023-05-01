package tasks;

import manager.Status;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;


public class Epic extends Task {

    private HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private LocalDateTime endTime;

    public Epic(String name, String description) {
        super(name, description);
        super.setType(TypeOfTask.EPIC);
    }

    public Epic(String name, String description, Status status, TypeOfTask type) {
        super(name, description, status, type);
    }


    public void addSubtask(int subtaskID, Subtask subtask) {
        subtasks.put(subtaskID, subtask);
        if (subtask.getStartTime() != null) {
            if (getDuration() != null) {
                setDuration(getDuration().plus(subtask.getDuration()));
            }
            if (getStartTime() == null) {
                setStartTime(subtask.getStartTime());
                setDuration(subtask.getDuration());
                endTime = subtask.getEndTime();
            } else if (getStartTime().isAfter(subtask.getStartTime())) {
                setStartTime(subtask.getStartTime());
            }
            if (endTime.isBefore(subtask.getEndTime())) {
                endTime = subtask.getEndTime();
            }
        }
    }

    public void removeSubtask(int ID) {
        subtasks.remove(ID);
    }

    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    @Override
    public LocalDateTime getEndTime(){
        return endTime;
    }

}
