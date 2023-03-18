package manager;

import manager.History.HistoryManager;
import manager.History.InMemoryHistoryManager;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Managers {
    public static FileBackedTasksManager getDefault() {
        return new FileBackedTasksManager(Paths.get("C:\\Users\\Вячеслав Украинцевв" +
                "\\IdeaProjects\\java-kanban\\src\\Saves.txt"));
    }


    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
