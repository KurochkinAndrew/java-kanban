import manager.*;
import tasks.*;


public class Main {
    public static void main(String[] args) {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        Task task1 = new Task("Помыть посуду", "Нужно помыть посуду до прихода гостей", Status.NEW);
        Task task2 = new Task("Погулять с собакой", "Вечерняя прогулка", Status.NEW);
        Epic epic1 = new Epic("Починить машину", "Нужно поменять топливный насос");
        Subtask subtask1 = new Subtask("Купить детали", "Купить топливный насос и болты",
                Status.DONE);
        Subtask subtask2 = new Subtask("Установить детали",
                "Установить топливный насос и болты", Status.NEW);
        Epic epic2 = new Epic("Убраться дома", "Помыть полы");
        Subtask subtask3 = new Subtask("Купить моющее средство", "Сходить в магазин хозтоваров",
                Status.NEW);

        taskManager.makeNewTask(task1);
        taskManager.makeNewTask(task2);
        taskManager.makeNewTask(epic1);
        taskManager.makeNewSubtask(subtask1, epic1.getID());
        taskManager.makeNewSubtask(subtask2, epic1.getID());
        taskManager.makeNewTask(epic2);
        taskManager.makeNewSubtask(subtask3, epic2.getID());
        taskManager.getEpicByID(epic2.getID());
        for (int i = 0; i<8; i++){
            taskManager.getTaskByID(task1.getID());
        }
        taskManager.getTaskByID(task2.getID());
        taskManager.getTaskByID(task2.getID());
        taskManager.getHistory();
    }
}