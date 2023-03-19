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
        manager.getTaskByID(task2.getID());
        manager.removeTaskByID(task2.getID());
        FileBackedTasksManager manager2 = Managers.getDefault();
        manager2.loadFromFile();
        manager2.getHistory();
        //Я имел в виду, что в первой строке файла я должен описать, что содержит в себе каждое поле:
        // "id,type,name,status,description,epic", но как кто-то должен понять, что в последней строке написана
        //именно история вызовов, если в строке с описанием полей я никак не могу прописать это

        //А программа почему-то работает у меня на пк, я убрал из пути два слэша, может теперь везде запустится
        //И папка out генерируется автоматически после запуска программы, как я понял её нельзя отчистить,
        //ведь она всегда будет заполняться

    }

}