import manager.*;
import tasks.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.TreeSet;


public class Main {
    public static void main(String[] args) {
        FileBackedTasksManager manager = Managers.getDefault();
        Task task1 = new Task("Помыть посуду", "Нужно помыть посуду до прихода гостей", Status.NEW, LocalDateTime.of(2023,1,1,0,18), Duration.ofSeconds(150));
        Task task2 = new Task("Погулять с собакой", "Вечерняя прогулка", Status.NEW,  LocalDateTime.of(2023,1,1,0,20), Duration.ofSeconds(150));
        Epic epic1 = new Epic("Починить машину", "Нужно поменять топливный насос");
        Subtask subtask1 = new Subtask("Купить детали", "Купить топливный насос и болты",
                Status.DONE,  LocalDateTime.of(2023,1,1,0,30), Duration.ofSeconds(150));
        Subtask subtask2 = new Subtask("Установить детали",
                "Установить топливный насос и болты", Status.NEW, LocalDateTime.of(2023,1,1,0,20), Duration.ofSeconds(150));
        Epic epic2 = new Epic("Убраться дома", "Помыть полы");
        Subtask subtask3 = new Subtask("Купить моющее средство", "Сходить в магазин хозтоваров",
                Status.NEW, LocalDateTime.of(2023,1,1,0,40), Duration.ofSeconds(150));
        //manager.loadFromFile();
        manager.makeNewTask(epic1);
        for (Task task: manager.getPrioritizedTasks()){
            System.out.println(task);
        }
        System.out.println("/");
        manager.makeNewSubtask(subtask1, epic1.getID());
        for (Task task: manager.getPrioritizedTasks()){
            System.out.println(task);
        }
        System.out.println("/");
        manager.makeNewSubtask(subtask2, epic1.getID());
        for (Task task: manager.getPrioritizedTasks()){
            System.out.println(task);
        }
        System.out.println("/");
        manager.makeNewSubtask(subtask3, epic1.getID());
        for (Task task: manager.getPrioritizedTasks()){
            System.out.println(task);
        }
        System.out.println("/");
    }

}