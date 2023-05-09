package Tests;


import Http.KVServer;
import manager.HttpTaskManager;
import manager.Managers;
import manager.Status;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.*;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskManagerTest {
    private HttpTaskManager manager;
    private KVServer kvServer;
    private Task task1, task2, task3;
    private Epic epic1, epic2;
    private Subtask subtask1, subtask2;

    @BeforeEach
    void beforeEach() throws IOException, InterruptedException {
        task1 = new Task("TestName1", "TestDescription1", Status.NEW,
                LocalDateTime.now().plus(Duration.ofSeconds(1)), Duration.ofSeconds(1));
        task2 = new Task("TestName2", "TestDescription2", Status.DONE,
                LocalDateTime.now().plus(Duration.ofSeconds(2)), Duration.ofSeconds(1));
        task3 = new Task("TestName2", "TestDescription2", Status.DONE);
        epic1 = new Epic("TestName3", "TestDescription3");
        epic2 = new Epic("TestName4", "TestDescription4");
        subtask1 = new Subtask("TestName5", "TestDescription5", Status.NEW, 0,
                LocalDateTime.now().plus(Duration.ofSeconds(3)), Duration.ofSeconds(1));
        subtask2 = new Subtask("TestName6", "TestDescription6", Status.DONE, 0,
                LocalDateTime.now().plus(Duration.ofSeconds(4)), Duration.ofSeconds(1));
        kvServer = new KVServer();
        kvServer.start();
        manager = Managers.getDefault();
    }

    @AfterEach
    void afterEach(){
        kvServer.stop();
    }

    @Test
    void shouldGetAllTasks() {
        manager.makeNewTask(task1);
        manager.makeNewTask(task2);
        ArrayList tasks = manager.getAllTasks();
        assertEquals(2, tasks.size());
        assertEquals(task1.getName(), tasks.get(0));
        assertEquals(task2.getName(), tasks.get(1));
    }

    @Test
    void shouldGetAllEpics() {
        manager.makeNewTask(epic1);
        manager.makeNewTask(epic2);
        ArrayList tasks = manager.getAllEpics();
        assertEquals(2, tasks.size());
        assertEquals(epic1.getName(), tasks.get(0));
        assertEquals(epic2.getName(), tasks.get(1));
    }

    @Test
    void shouldGetAllSubtasks() {
        manager.makeNewTask(epic1);
        manager.makeNewSubtask(subtask1, epic1.getID());
        manager.makeNewSubtask(subtask2, epic1.getID());
        ArrayList subtasks = manager.getAllSubtasks();
        assertEquals(2, subtasks.size());
        assertEquals(subtask1.getName(), subtasks.get(0));
        assertEquals(subtask2.getName(), subtasks.get(1));
    }

    @Test
    void shouldRemoveAllTasks() {
        manager.makeNewTask(task1);
        manager.makeNewTask(task2);
        manager.removeAllTasks();
        assertEquals(0, manager.getMapOfTasks().size());
    }

    @Test
    void shouldRemoveAllEpics() {
        manager.makeNewTask(epic1);
        manager.makeNewSubtask(subtask1, epic1.getID());
        manager.makeNewSubtask(subtask2, epic1.getID());
        manager.removeAllEpics();
        assertEquals(0, manager.getMapOfEpics().size());
        assertEquals(0, manager.getMapOfSubtasks().size());
    }

    @Test
    void shouldRemoveAllSubtasks() {
        manager.makeNewTask(epic1);
        manager.makeNewSubtask(subtask1, epic1.getID());
        manager.makeNewSubtask(subtask2, epic1.getID());
        manager.removeAllSubtasks();
        assertEquals(0, manager.getMapOfSubtasks().size());
    }

    @Test
    void shouldMakeNewTask() {
        manager.makeNewTask(task1);
        manager.makeNewTask(epic1);
        assertEquals(1, manager.getMapOfTasks().size());
        assertTrue(manager.getMapOfTasks().containsValue(task1));
        assertEquals(1, manager.getMapOfTasks().size());
        assertTrue(manager.getMapOfEpics().containsValue(epic1));
    }

    @Test
    void shouldMakeNewSubtask() {
        manager.makeNewTask(epic1);
        manager.makeNewSubtask(subtask1, epic1.getID());
        assertEquals(1, manager.getMapOfSubtasks().size());
        assertTrue(manager.getMapOfSubtasks().containsValue(subtask1));
        assertEquals(epic1.getID(), subtask1.getEpicOwnerID());
    }

    @Test
    void shouldGetTaskById() {
        manager.makeNewTask(task1);
        assertEquals(task1, manager.getTaskByID(task1.getID()));
    }

    @Test
    void shouldGetEpicById() {
        manager.makeNewTask(epic1);
        assertEquals(epic1, manager.getEpicByID(epic1.getID()));
    }

    @Test
    void shouldGetSubtaskById() {
        manager.makeNewTask(epic1);
        manager.makeNewSubtask(subtask1, epic1.getID());
        assertEquals(subtask1, manager.getSubtaskByID(subtask1.getID()));
    }

    @Test
    void shouldRefreshTask() {
        manager.makeNewTask(task1);
        manager.refreshTask(task2, task1.getID());
        assertTrue(manager.getMapOfTasks().containsValue(task2));
        assertFalse(manager.getMapOfTasks().containsValue(task1));
    }

    @Test
    void shouldRefreshSubTask() {
        manager.makeNewTask(epic1);
        manager.makeNewSubtask(subtask1, epic1.getID());
        manager.refreshSubtask(subtask2, subtask1.getID());
        assertTrue(manager.getMapOfSubtasks().containsValue(subtask2));
        assertFalse(manager.getMapOfSubtasks().containsValue(subtask1));
        assertTrue(epic1.getSubtasks().containsValue(subtask2));
        assertFalse(epic1.getSubtasks().containsValue(subtask1));
    }

    @Test
    void shouldRefreshEpic() {
        manager.makeNewTask(epic1);
        manager.refreshEpic(epic2, epic1.getID());
        assertTrue(manager.getMapOfEpics().containsValue(epic2));
        assertFalse(manager.getMapOfEpics().containsValue(epic1));
    }

    @Test
    void shouldRemoveEpicByID() {
        manager.makeNewTask(epic1);
        manager.removeEpicByID(epic1.getID());
        assertFalse(manager.getMapOfEpics().containsValue(epic1));
    }

    @Test
    void shouldRemoveTaskByID() {
        manager.makeNewTask(task1);
        manager.removeTaskByID(task1.getID());
        assertFalse(manager.getMapOfTasks().containsValue(task1));
    }

    @Test
    void shouldRemoveSubtaskByID() {
        manager.makeNewTask(epic1);
        manager.makeNewSubtask(subtask1, epic1.getID());
        manager.removeSubtaskByID(subtask1.getID());
        assertFalse(manager.getMapOfSubtasks().containsValue(subtask1));
        assertFalse(epic1.getSubtasks().containsValue(subtask1));
    }

    @Test
    void shouldGetSubtasksOfEpic() {
        manager.makeNewTask(epic1);
        manager.makeNewSubtask(subtask1, epic1.getID());
        assertEquals(subtask1.getName(), manager.getSubtasksOfEpic(epic1.getID()).get(0));
    }

    @Test
    void shouldGetPrioritizedTasks(){
        manager.makeNewTask(task3);
        manager.makeNewTask(task1);
        manager.makeNewTask(task2);
        TreeSet<Task> prioritizedTasks = manager.getPrioritizedTasks();
        assertEquals(task1, prioritizedTasks.toArray()[0]);
        assertEquals(task2, prioritizedTasks.toArray()[1]);
        assertEquals(task3, prioritizedTasks.toArray()[2]);
    }

    @Test
    void testSaveToServerAndLoadFromServer(){
        manager.makeNewTask(task1);
        manager.makeNewTask(task2);
        manager.makeNewTask(epic1);
        TreeSet<Task> expected = manager.getPrioritizedTasks();
        manager.removeAll();
        manager.loadFromServer();
        TreeSet<Task> actual = manager.getPrioritizedTasks();
        assertEquals(expected, actual);
    }

}
