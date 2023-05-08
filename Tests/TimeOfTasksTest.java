package Tests;

import manager.FileBackedTasksManager;
import manager.Managers;
import manager.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;
import java.time.LocalDateTime;

public class TimeOfTasksTest {
    FileBackedTasksManager fileBackedTasksManager;
    Task task1 = new Task("TestName1", "TestDescription1", Status.NEW,
            LocalDateTime.of(2023, 1, 1, 0, 0), Duration.ofSeconds(10));
    Epic epic1 = new Epic("TestName2", "TestDescription2");
    Subtask subtask1 = new Subtask("TestName3", "TestDescription3", Status.NEW, 0,
            LocalDateTime.of(2023, 1, 1, 0, 1), Duration.ofSeconds(17));
    Subtask subtask2 = new Subtask("TestName4", "TestDescription4", Status.NEW, 0,
            LocalDateTime.of(2023, 1, 1, 0, 2), Duration.ofSeconds(13));
    Subtask subtask3 = new Subtask("TestName5", "TestDescription5", Status.NEW, 0,
            LocalDateTime.of(2023, 1, 1, 0, 3), Duration.ofSeconds(20));

    @BeforeEach
    void beforeEach() {
        fileBackedTasksManager = new FileBackedTasksManager();
    }

    @Test
    void getStartTimeOfTaskAndSubtaskTest(){
        fileBackedTasksManager.makeNewTask(task1);
        fileBackedTasksManager.makeNewTask(epic1);
        fileBackedTasksManager.makeNewSubtask(subtask1, epic1.getID());
        assertEquals(LocalDateTime.of(2023, 1, 1, 0, 0), task1.getStartTime());
        assertEquals(LocalDateTime.of(2023, 1, 1, 0, 1), subtask1.getStartTime());
    }

    @Test
    void getDurationOfTaskAndSubtaskTest(){
        fileBackedTasksManager.makeNewTask(task1);
        fileBackedTasksManager.makeNewTask(epic1);
        fileBackedTasksManager.makeNewSubtask(subtask1, epic1.getID());
        assertEquals(Duration.ofSeconds(10), task1.getDuration());
        assertEquals(Duration.ofSeconds(17), subtask1.getDuration());
    }

    @Test
    void getEndTimeOfTaskAndSubtaskTest(){
        fileBackedTasksManager.makeNewTask(task1);
        fileBackedTasksManager.makeNewTask(epic1);
        fileBackedTasksManager.makeNewSubtask(subtask1, epic1.getID());
        assertEquals(LocalDateTime.of(2023, 1, 1, 0, 0).plus(Duration.ofSeconds(10)),
                task1.getEndTime());
        assertEquals(LocalDateTime.of(2023, 1, 1, 0, 1).plus(Duration.ofSeconds(17)),
                subtask1.getEndTime());
    }

    @Test
    void timeOfEpicTest(){
        fileBackedTasksManager.makeNewTask(epic1);
        fileBackedTasksManager.makeNewSubtask(subtask1, epic1.getID());
        fileBackedTasksManager.makeNewSubtask(subtask2, epic1.getID());
        fileBackedTasksManager.makeNewSubtask(subtask3, epic1.getID());
        assertEquals(subtask1.getStartTime(), epic1.getStartTime());
        assertEquals(subtask3.getEndTime(), epic1.getEndTime());
        assertEquals(subtask1.getDuration().plus(subtask2.getDuration()).plus(subtask3.getDuration()),
                epic1.getDuration());
    }

    @Test
    void timeOfEpicShouldBeNullWhenItHasNoSubtasks(){
        fileBackedTasksManager.makeNewTask(epic1);
        assertEquals(null, epic1.getStartTime());
        assertEquals(null, epic1.getEndTime());
        assertEquals(null, epic1.getDuration());
    }

}
