package manager;

import manager.History.HistoryManager;
import manager.History.InMemoryHistoryManager;

import java.io.IOException;
import java.nio.file.Paths;

public class Managers {
    public static HttpTaskManager getDefault() {
        try {
            return new HttpTaskManager("http://localhost:8078/");
        } catch (IOException | InterruptedException e){
            return null;
        }
    }


    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
