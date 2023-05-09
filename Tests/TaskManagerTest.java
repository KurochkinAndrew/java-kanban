package Tests;


import manager.FileBackedTasksManager;
import manager.Managers;
import manager.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

public class TaskManagerTest {
    private FileBackedTasksManager manager;
    private Task task1 = new Task("TestName1", "TestDescription1", Status.NEW,
            LocalDateTime.now().plus(Duration.ofSeconds(1)), Duration.ofSeconds(1));
    private Task task2 = new Task("TestName2", "TestDescription2", Status.DONE,
            LocalDateTime.now().plus(Duration.ofSeconds(2)), Duration.ofSeconds(1));
    private Task task3 = new Task("TestName2", "TestDescription2", Status.DONE);
    private Epic epic1 = new Epic("TestName3", "TestDescription3");
    private Epic epic2 = new Epic("TestName4", "TestDescription4");
    private Subtask subtask1 = new Subtask("TestName5", "TestDescription5", Status.NEW, 0,
            LocalDateTime.now().plus(Duration.ofSeconds(3)), Duration.ofSeconds(1));
    private Subtask subtask2 = new Subtask("TestName6", "TestDescription6", Status.DONE, 0,
            LocalDateTime.now().plus(Duration.ofSeconds(4)), Duration.ofSeconds(1));

    @BeforeEach
    void beforeEach() {
        manager = new FileBackedTasksManager();
    }

    @Test
    void getAllTasksTest() {
        manager.makeNewTask(task1);
        manager.makeNewTask(task2);
        ArrayList tasks = manager.getAllTasks();
        assertEquals(2, tasks.size());
        assertEquals(task1.getName(), tasks.get(0));
        assertEquals(task2.getName(), tasks.get(1));
    }

    @Test
    void getAllEpicsTest() {
        manager.makeNewTask(epic1);
        manager.makeNewTask(epic2);
        ArrayList tasks = manager.getAllEpics();
        assertEquals(2, tasks.size());
        assertEquals(epic1.getName(), tasks.get(0));
        assertEquals(epic2.getName(), tasks.get(1));
    }

    @Test
    void getAllSubtasksTest() {
        manager.makeNewTask(epic1);
        manager.makeNewSubtask(subtask1, epic1.getID());
        manager.makeNewSubtask(subtask2, epic1.getID());
        ArrayList tasks = manager.getAllSubtasks();
        assertEquals(2, tasks.size());
        assertEquals(subtask1.getName(), tasks.get(0));
        assertEquals(subtask2.getName(), tasks.get(1));
    }

    @Test
    void removeAllTasksTest() {
        manager.makeNewTask(task1);
        manager.makeNewTask(task2);
        manager.removeAllTasks();
        assertEquals(0, manager.getMapOfTasks().size());
    }

    @Test
    void removeAllEpicsTest() {
        manager.makeNewTask(epic1);
        manager.makeNewSubtask(subtask1, epic1.getID());
        manager.makeNewSubtask(subtask2, epic1.getID());
        manager.removeAllEpics();
        assertEquals(0, manager.getMapOfEpics().size());
        assertEquals(0, manager.getMapOfSubtasks().size());
    }

    @Test
    void removeAllSubtasksTest() {
        manager.makeNewTask(epic1);
        manager.makeNewSubtask(subtask1, epic1.getID());
        manager.makeNewSubtask(subtask2, epic1.getID());
        manager.removeAllSubtasks();
        assertEquals(0, manager.getMapOfSubtasks().size());
    }

    @Test
    void makeNewTaskTest() {
        manager.makeNewTask(task1);
        manager.makeNewTask(epic1);
        assertEquals(1, manager.getMapOfTasks().size());
        assertTrue(manager.getMapOfTasks().containsValue(task1));
        assertEquals(1, manager.getMapOfTasks().size());
        assertTrue(manager.getMapOfEpics().containsValue(epic1));
    }

    @Test
    void makeNewSubtaskTest() {
        manager.makeNewTask(epic1);
        manager.makeNewSubtask(subtask1, epic1.getID());
        assertEquals(1, manager.getMapOfSubtasks().size());
        assertTrue(manager.getMapOfSubtasks().containsValue(subtask1));
        assertEquals(epic1.getID(), subtask1.getEpicOwnerID());
    }

    @Test
    void getTaskByIdTest() {
        manager.makeNewTask(task1);
        assertEquals(task1, manager.getTaskByID(task1.getID()));
    }

    @Test
    void getEpicByIdTest() {
        manager.makeNewTask(epic1);
        assertEquals(epic1, manager.getEpicByID(epic1.getID()));
    }

    @Test
    void getSubtaskByIdTest() {
        manager.makeNewTask(epic1);
        manager.makeNewSubtask(subtask1, epic1.getID());
        assertEquals(subtask1, manager.getSubtaskByID(subtask1.getID()));
    }

    @Test
    void refreshTaskTest() {
        manager.makeNewTask(task1);
        manager.refreshTask(task2, task1.getID());
        assertTrue(manager.getMapOfTasks().containsValue(task2));
        assertFalse(manager.getMapOfTasks().containsValue(task1));
    }

    @Test
    void refreshSubTaskTest() {
        manager.makeNewTask(epic1);
        manager.makeNewSubtask(subtask1, epic1.getID());
        manager.refreshSubtask(subtask2, subtask1.getID());
        assertTrue(manager.getMapOfSubtasks().containsValue(subtask2));
        assertFalse(manager.getMapOfSubtasks().containsValue(subtask1));
        assertTrue(epic1.getSubtasks().containsValue(subtask2));
        assertFalse(epic1.getSubtasks().containsValue(subtask1));
    }

    @Test
    void refreshEpicTest() {
        manager.makeNewTask(epic1);
        manager.refreshEpic(epic2, epic1.getID());
        assertTrue(manager.getMapOfEpics().containsValue(epic2));
        assertFalse(manager.getMapOfEpics().containsValue(epic1));
    }

    @Test
    void removeEpicByIDTest() {
        manager.makeNewTask(epic1);
        manager.removeEpicByID(epic1.getID());
        assertFalse(manager.getMapOfEpics().containsValue(epic1));
    }

    @Test
    void removeTaskByIDTest() {
        manager.makeNewTask(task1);
        manager.removeTaskByID(task1.getID());
        assertFalse(manager.getMapOfTasks().containsValue(task1));
    }

    @Test
    void removeSubtaskByIDTest() {
        manager.makeNewTask(epic1);
        manager.makeNewSubtask(subtask1, epic1.getID());
        manager.removeSubtaskByID(subtask1.getID());
        assertFalse(manager.getMapOfSubtasks().containsValue(subtask1));
        assertFalse(epic1.getSubtasks().containsValue(subtask1));
    }

    @Test
    void getSubtasksOfEpicTest() {
        manager.makeNewTask(epic1);
        manager.makeNewSubtask(subtask1, epic1.getID());
        assertEquals(subtask1.getName(), manager.getSubtasksOfEpic(epic1.getID()).get(0));
    }

    @Test
    void getPrioritizedTasksTest(){
        manager.makeNewTask(task3);
        manager.makeNewTask(task1);
        manager.makeNewTask(task2);
        TreeSet<Task> prioritizedTasks = manager.getPrioritizedTasks();
        assertEquals(task1, prioritizedTasks.toArray()[0]);
        assertEquals(task2, prioritizedTasks.toArray()[1]);
        assertEquals(task3, prioritizedTasks.toArray()[2]);
    }

}
