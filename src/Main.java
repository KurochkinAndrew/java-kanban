import manager.*;
import tasks.*;


public class Main {
    public static void main(String[] args) {
        FileBackedTasksManager manager = Managers.getDefault();
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


        FileBackedTasksManager manager2 = Managers.getDefault();
        manager2.loadFromFile();
        manager2.getHistory();

    }

}