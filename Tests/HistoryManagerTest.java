package Tests;

import manager.*;
import manager.History.HistoryManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HistoryManagerTest {
    private HistoryManager historyManager;
    private TaskManager manager;
    private Task task1 = new Task("TestName1", "TestDescription1", Status.NEW,
            LocalDateTime.now().plus(Duration.ofSeconds(1)), Duration.ofSeconds(1));
    private Task task2 = new Task("TestName2", "TestDescription2", Status.DONE,
            LocalDateTime.now().plus(Duration.ofSeconds(2)), Duration.ofSeconds(1));
    private Task task3 = new Task("TestName3", "TestDescription3", Status.IN_PROGRESS,
            LocalDateTime.now().plus(Duration.ofSeconds(3)), Duration.ofSeconds(1));
    private List<Task> expected;

    @BeforeEach
    void beforeEach() {
        historyManager = Managers.getDefaultHistory();
        manager = new InMemoryTaskManager(historyManager);
        manager.makeNewTask(task1);
        manager.makeNewTask(task2);
        manager.makeNewTask(task3);
    }

    @Test
    void getHistoryTest() {
        assertEquals(0, historyManager.getHistory().size());
        manager.getTaskByID(task1.getID());
        manager.getTaskByID(task1.getID());
        assertEquals(1, historyManager.getHistory().size());
        manager.getTaskByID(task2.getID());
        assertEquals(2, historyManager.getHistory().size());
        ArrayList history = historyManager.getHistory();
        assertEquals(task1, history.get(0));
        assertEquals(task2, history.get(1));
        manager.getTaskByID(task1.getID());
        history = historyManager.getHistory();
        assertEquals(task1, history.get(1));
        assertEquals(task2, history.get(0));
    }

    @Test
    void removeFromHistoryTest() {
        manager.getTaskByID(task1.getID());
        manager.getTaskByID(task2.getID());
        manager.getTaskByID(task3.getID());
        historyManager.remove(task3.getID());
        ArrayList history = historyManager.getHistory();
        expected = Arrays.asList(task1, task2);
        assertEquals(expected, history);
        manager.getTaskByID(task1.getID());
        manager.getTaskByID(task2.getID());
        manager.getTaskByID(task3.getID());
        historyManager.remove(task1.getID());
        history = historyManager.getHistory();
        expected = Arrays.asList(task2, task3);
        assertEquals(expected, history);
        manager.getTaskByID(task1.getID());
        manager.getTaskByID(task2.getID());
        manager.getTaskByID(task3.getID());
        historyManager.remove(task2.getID());
        history = historyManager.getHistory();
        expected = Arrays.asList(task1, task3);
        assertEquals(expected, history);
    }

    @Test
    void addTest() {
        historyManager.add(task1);
        historyManager.add(task2);
        ArrayList history = historyManager.getHistory();
        expected = Arrays.asList(task1, task2);
        assertEquals(expected, history);
    }
}
