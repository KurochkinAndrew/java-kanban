package manager;

import manager.History.HistoryManager;
import tasks.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class FileBackedTasksManager extends InMemoryTaskManager {
    private String saveFilePath;

    public FileBackedTasksManager(String saveFilePath) {
        this.saveFilePath = saveFilePath;
    }

    @Override
    public void makeNewTask(Object Task) {
        super.makeNewTask(Task);
        save();
    }

    @Override
    public void makeNewSubtask(Subtask task, int epicID) {
        super.makeNewSubtask(task, epicID);
        save();
    }

    @Override
    public Task getTaskByID(int ID) {
        Task task = super.getTaskByID(ID);
        save();
        return task;
    }

    @Override
    public Epic getEpicByID(int ID) {
        Epic task = super.getEpicByID(ID);
        save();
        return task;
    }

    @Override
    public Subtask getSubtaskByID(int ID) {
        Subtask task = super.getSubtaskByID(ID);
        save();
        return task;
    }

    public Task taskFromString(String taskString) {
        String[] splitTaskString = taskString.split(",");
        if (splitTaskString.length > 1) {
            if ((splitTaskString[1].equals("SUBTASK"))) {
                return new Subtask(splitTaskString[2], splitTaskString[4],
                        Status.valueOf(splitTaskString[3]), TypeOfTask.valueOf(splitTaskString[1]),
                        Integer.parseInt(splitTaskString[5]));
            } else if (splitTaskString[1].equals("TASK")) {
                return new Task(splitTaskString[2], splitTaskString[4],
                        Status.valueOf(splitTaskString[3]), TypeOfTask.valueOf(splitTaskString[1]));
            } else if (splitTaskString[1].equals("EPIC")) {
                return new Epic(splitTaskString[2], splitTaskString[4],
                        Status.valueOf(splitTaskString[3]), TypeOfTask.valueOf(splitTaskString[1]));
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public void loadFromFile() {
        try (BufferedReader fileReader = new BufferedReader(new FileReader(saveFilePath))) {
            while (fileReader.ready()) {
                String s = fileReader.readLine();
                Task task = taskFromString(s);
                if (task != null) {
                    if (task.getType() == TypeOfTask.SUBTASK) {
                        Subtask subtask = (Subtask) task;
                        super.makeNewSubtask(subtask, subtask.getEpicOwnerID());
                    } else {
                        super.makeNewTask(task);
                    }
                } else if (s.contains("История")) {
                    for (int ID : historyFromString(s)) {
                        if (getTaskByIdWithoutHistoryAdd(ID) != null){
                            historyManager.add(getTaskByIdWithoutHistoryAdd(ID));
                        } else if(getSubtaskByIdWithoutHistoryAdd(ID) != null){
                            historyManager.add(getSubtaskByIdWithoutHistoryAdd(ID));
                        } else {
                            historyManager.add(getEpicByIdWithoutHistoryAdd(ID));
                        }
                    }
                }
            }
            for (Epic epic: getMapOfEpics().values()){
                for (Subtask subtask: getMapOfSubtasks().values()){
                    if (subtask.getEpicOwnerID() == epic.getID()){
                        epic.addSubtask(subtask.getID(), subtask);
                    }
                }
            }
        } catch (IOException e) {
            }
        }

    public void save() {
        try (FileWriter writer = new FileWriter(saveFilePath)) {
            writer.write("//id,type,name,status,description,epic");
            writer.write(System.getProperty("line.separator"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        try (FileWriter writer = new FileWriter(saveFilePath, true)) {
            for (Task task : getTreeSetOfTasks()) {
                writer.write(task.toString());
                writer.write(System.getProperty("line.separator"));
            }
            writer.write(System.getProperty("line.separator"));
            writer.write("//История,");
            writer.write(historyToString(historyManager));
        } catch (IOException e) {
        }
    }

    public static String historyToString(HistoryManager manager) {
        String historyString = "";
        for (Task task : manager.getHistory()) {
            historyString += task.getID();
            if (task != manager.getHistory().get(manager.getHistory().size() - 1)) {
                historyString += ",";
            }
        }
        return historyString;
    }

    public static List<Integer> historyFromString(String historyString) {
        ArrayList<Integer> historyList = new ArrayList<>();
        for (String s : historyString.split(",")) {
            if (!(s.contains("/"))) {
                historyList.add(Integer.parseInt(s));
            }
        }
        return historyList;
    }
}

