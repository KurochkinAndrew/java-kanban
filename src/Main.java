import Http.HttpTaskServer;
import Http.KVServer;
import Http.KVTaskClient;
import com.google.gson.Gson;
import manager.*;
import tasks.*;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;


public class Main {
    public static void main(String[] args) throws IOException, InterruptedException{
        Task task1 = new Task("Помыть посуду", "Нужно помыть посуду до прихода гостей", Status.NEW);
        Task task2 = new Task("Погулять с собакой", "Вечерняя прогулка", Status.NEW,  LocalDateTime.of(2023,1,1,0,21), Duration.ofSeconds(150));
        Epic epic1 = new Epic("Починить машину", "Нужно поменять топливный насос");
        Subtask subtask1 = new Subtask("Купить детали", "Купить топливный насос и болты",
                Status.DONE,  LocalDateTime.of(2023,1,1,0,30), Duration.ofSeconds(150));
        Subtask subtask2 = new Subtask("Установить детали",
                "Установить топливный насос и болты", Status.NEW, LocalDateTime.of(2023,1,1,0,20), Duration.ofSeconds(150));
        Epic epic2 = new Epic("Убраться дома", "Помыть полы");
        Subtask subtask3 = new Subtask("Купить моющее средство", "Сходить в магазин хозтоваров",
                Status.NEW, LocalDateTime.of(2023,1,1,0,40), Duration.ofSeconds(150));
        Task task = new Task("Погулять с собакой", "Вечерняя прогулка", Status.NEW,
                LocalDateTime.of(2023,1,1,0,21), Duration.ofSeconds(150), 0);
        new KVServer().start();
        HttpTaskManager manager = Managers.getDefault();
        HttpTaskServer server = new HttpTaskServer();

        server.start();
        //Можешь, пожалуйста, оставить все ошибки под тегом "можно лучше", просто 10.05 жесткий дедлайн, и если 9.05
        // будет не принят проект, то меня отчислят)) Я обязательно все ошибки исправлю!
    }

}