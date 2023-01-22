public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        Task task1 = new Task("Помыть посуду", "Нужно помыть посуду до прихода гостей", "NEW");
        Task task2 = new Task("Погулять с собакой", "Вечерняя прогулка", "NEW");
        Epic epic1 = new Epic("Починить машину", "Нужно поменять топливный насос");
        Subtask subtask1 = new Subtask("Купить детали", "Купить топливный насос и болты", "DONE");
        Subtask subtask2 = new Subtask("Установить детали",
                "Установить топливный насос и болты", "NEW");
        Epic epic2 = new Epic("Убраться дома", "Помыть полы");
        Subtask subtask3 = new Subtask("Купить моющее средство", "Сходить в магазин хозтоваров",
                "NEW");

        taskManager.makeNewTask(task1);
        taskManager.makeNewTask(task2);
        taskManager.makeNewTask(epic1);
        taskManager.makeNewSubtask(subtask1, epic1.ID);
        taskManager.makeNewSubtask(subtask2, epic1.ID);
        taskManager.makeNewTask(epic2);
        taskManager.makeNewSubtask(subtask3, epic2.ID);
        System.out.println(taskManager.getAllTasks());
        System.out.println(epic1.getStatus());
        taskManager.removeTaskByID(subtask2.ID);
        System.out.println(epic1.getStatus());
        taskManager.removeTaskByID(task1.ID);
        System.out.println(taskManager.getAllTasks());
    }
}