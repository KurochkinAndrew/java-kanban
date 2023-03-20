package manager;

import manager.History.HistoryManager;
import manager.History.InMemoryHistoryManager;
import java.nio.file.Paths;

public class Managers {
    public static FileBackedTasksManager getDefault() {
        return new FileBackedTasksManager(Paths.get(System.getProperty("user.dir"),"/Saves.txt"));
    }


    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
