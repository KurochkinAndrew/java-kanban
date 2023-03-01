import manager.*;
import tasks.*;


public class Main {
    public static void main(String[] args) {
        TaskManager manager = Managers.getDefault();
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

        manager.makeNewTask(task1);
        manager.makeNewTask(task2);
        manager.makeNewTask(epic1);
        manager.makeNewSubtask(subtask1, epic1.getID());
        manager.makeNewSubtask(subtask2, epic1.getID());
        manager.makeNewTask(epic2);
        manager.makeNewSubtask(subtask3, epic2.getID());

        manager.getTaskByID(task1.getID());
        manager.getSubtaskByID(subtask2.getID());
        manager.getTaskByID(task1.getID());
        manager.getTaskByID(task2.getID()); //Вот последний таск
        manager.removeTaskByID(task2.getID()); //Тут удаляю его и никакой ошибки нет, может я что-то не так понял?
        manager.getHistory();

    }

}