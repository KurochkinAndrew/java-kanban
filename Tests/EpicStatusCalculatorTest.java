package Tests;

import manager.FileBackedTasksManager;
import manager.Managers;
import manager.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.*;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EpicStatusCalculatorTest {
    FileBackedTasksManager manager;
    Epic epic1 = new Epic("", "");
    Subtask subtask1, subtask2, subtask3, subtask4, subtask5, subtask6;

    @BeforeEach
    void beforeEach(){
        manager = new FileBackedTasksManager();
        epic1 = new Epic("", "");
        manager.makeNewTask(epic1);
        subtask1 = new Subtask("", "", Status.NEW, LocalDateTime.now(), Duration.ofSeconds(1));
        subtask2 = new Subtask("", "", Status.NEW, LocalDateTime.now().plus(Duration.ofSeconds(2)),
                Duration.ofSeconds(1));
        subtask3 = new Subtask("", "", Status.DONE, LocalDateTime.now().plus(Duration.ofSeconds(3)),
                Duration.ofSeconds(1));
        subtask4 = new Subtask("", "", Status.DONE, LocalDateTime.now().plus(Duration.ofSeconds(4)),
                Duration.ofSeconds(1));
        subtask5 = new Subtask("", "", Status.IN_PROGRESS,
                LocalDateTime.now().plus(Duration.ofSeconds(5)), Duration.ofMinutes(1));
        subtask6 = new Subtask("", "", Status.IN_PROGRESS,
                LocalDateTime.now().plus(Duration.ofSeconds(6)), Duration.ofSeconds(1));
    }

    @Test
    void shouldBeNewWhenEpicIsEmpty(){
        assertEquals(Status.NEW,epic1.getStatus());
    }

    @Test
    void shouldBeNewWhenAllSubtasksOfEpicIsNew(){
        manager.makeNewSubtask(subtask1, epic1.getID());
        manager.makeNewSubtask(subtask2, epic1.getID());
        assertEquals(Status.NEW,epic1.getStatus());
    }

    @Test
    void shouldBeDoneWhenAllSubtasksOfEpicAreDone(){
        manager.makeNewSubtask(subtask3, epic1.getID());
        manager.makeNewSubtask(subtask4, epic1.getID());
        assertEquals(Status.DONE,epic1.getStatus());
    }

    @Test
    void shouldBeInProgressWhenSubtasksOfEpicAreDoneAndNew(){
        manager.makeNewSubtask(subtask3, epic1.getID());
        manager.makeNewSubtask(subtask1, epic1.getID());
        assertEquals(Status.IN_PROGRESS,epic1.getStatus());
    }

    @Test
    void shouldBeInProgressWhenAllSubtasksOfEpicAreInProgress(){
        manager.makeNewSubtask(subtask5, epic1.getID());
        manager.makeNewSubtask(subtask6, epic1.getID());
        assertEquals(Status.IN_PROGRESS,epic1.getStatus());
    }
}