package manager;

import manager.History.HistoryManager;
import manager.History.InMemoryHistoryManager;

import java.io.IOException;
import java.nio.file.Paths;

public class Managers {
    public static HttpTaskManager getDefault() throws IOException, InterruptedException {
            return new HttpTaskManager("http://localhost:8078/");
    }


    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
