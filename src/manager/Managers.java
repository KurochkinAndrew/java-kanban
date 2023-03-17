package manager;

import manager.History.HistoryManager;
import manager.History.InMemoryHistoryManager;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Managers {
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }
    public static FileBackedTasksManager getFileManager(){return new FileBackedTasksManager("src\\Saves.txt");}

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
