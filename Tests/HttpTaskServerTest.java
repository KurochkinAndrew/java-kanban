import Http.HttpTaskServer;
import com.google.gson.Gson;
import manager.FileBackedTasksManager;
import static org.junit.jupiter.api.Assertions.*;
import manager.Status;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.TreeSet;

public class HttpTaskServerTest {
    HttpTaskServer httpTaskServer;
    FileBackedTasksManager manager;
    HttpClient client = HttpClient.newHttpClient();
    URI uri;
    Gson gson = new Gson();

    Task task;
    Subtask subtask;
    Epic epic;

    @BeforeEach
    void beforeEach(){
        task = new Task("Погулять с собакой", "Вечерняя прогулка", Status.NEW,
                LocalDateTime.of(2023,1,1,0,21), Duration.ofSeconds(150), 0);
        subtask = new Subtask("Купить детали", "Купить топливный насос и болты",
                Status.DONE,0,  LocalDateTime.of(2023,1,1,0,30),
                Duration.ofSeconds(150), 1);
        epic =  new Epic("Починить машину", "Нужно поменять топливный насос", 0, Status.NEW);
        httpTaskServer = new HttpTaskServer();
        manager = httpTaskServer.getManager();
        httpTaskServer.start();
    }

    @AfterEach
    void afterEach(){
        manager.clearSaveFile();
        httpTaskServer.stop();
    }

    @Test
    void postTaskTest() throws InterruptedException, IOException {
        URI uri = URI.create("http://localhost:8080/tasks/task/?id=0");
        HttpRequest.BodyPublisher bodyPublisher =
                HttpRequest.BodyPublishers.ofString(gson.toJson(task));
        HttpRequest request = HttpRequest.newBuilder()
                .POST(bodyPublisher)
                .uri(uri)
                .build();
       client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        assertEquals(task.toString(), manager.getTaskByID(0).toString());
    }

    @Test
    void getTaskTest() throws InterruptedException, IOException {
        manager.makeNewTask(task);
        URI uri = URI.create("http://localhost:8080/tasks/task/?id=0");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();
        String body = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8)).body();
        assertEquals(gson.toJson(task), body);
    }

    @Test
    void deleteTaskTest() throws InterruptedException, IOException {
        manager.makeNewTask(task);
        URI uri = URI.create("http://localhost:8080/tasks/task/?id=0");
        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .uri(uri)
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8)).body();
        assertEquals(0, manager.getAllTasks().size());
    }

    @Test
    void postEpicTest() throws InterruptedException, IOException {
        URI uri = URI.create("http://localhost:8080/tasks/epic/?id=0");
        HttpRequest.BodyPublisher bodyPublisher =
                HttpRequest.BodyPublishers.ofString(gson.toJson(epic));
        HttpRequest request = HttpRequest.newBuilder()
                .POST(bodyPublisher)
                .uri(uri)
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        assertEquals(epic.toString(), manager.getEpicByID(0).toString());
    }

    @Test
    void getEpicTest() throws InterruptedException, IOException {
        manager.makeNewTask(epic);
        URI uri = URI.create("http://localhost:8080/tasks/epic/?id=0");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();
        String body = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8)).body();
        assertEquals(gson.toJson(epic), body);
    }

    @Test
    void deleteEpicTest() throws InterruptedException, IOException {
        manager.makeNewTask(epic);
        URI uri = URI.create("http://localhost:8080/tasks/epic/?id=0");
        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .uri(uri)
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8)).body();
        assertEquals(0, manager.getAllEpics().size());
    }

    @Test
    void postSubtaskTest() throws InterruptedException, IOException {
        manager.makeNewTask(epic);
        URI uri = URI.create("http://localhost:8080/tasks/subtask/?id=1");
        HttpRequest.BodyPublisher bodyPublisher =
                HttpRequest.BodyPublishers.ofString(gson.toJson(subtask));
        HttpRequest request = HttpRequest.newBuilder()
                .POST(bodyPublisher)
                .uri(uri)
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        assertEquals(subtask.toString(), manager.getSubtaskByID(1).toString());
    }

    @Test
    void getSubtaskTest() throws InterruptedException, IOException {
        manager.makeNewTask(epic);
        manager.makeNewSubtask(subtask, epic.getID());
        URI uri = URI.create("http://localhost:8080/tasks/subtask/?id=1");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();
        String body = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8)).body();
        assertEquals(gson.toJson(subtask), body);
    }

    @Test
    void deleteSubtaskTest() throws InterruptedException, IOException {
        manager.makeNewTask(epic);
        manager.makeNewSubtask(subtask, epic.getID());
        URI uri = URI.create("http://localhost:8080/tasks/subtask/?id=1");
        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .uri(uri)
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8)).body();
        assertEquals(0, manager.getAllSubtasks().size());
    }

    @Test
    void getSubtasksOfEpicTest() throws InterruptedException, IOException {
        manager.makeNewTask(epic);
        manager.makeNewSubtask(subtask, epic.getID());
        manager.makeNewSubtask(subtask, epic.getID());
        URI uri = URI.create("http://localhost:8080/tasks/subtask/epic/?id=0");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();
        String body = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8)).body();
        ArrayList<Task> expected = manager.getSubtasksOfEpic(epic.getID());
        assertEquals(gson.toJson(expected), body);
    }

    @Test
    void getHistoryTest() throws InterruptedException, IOException {
        manager.makeNewTask(epic);
        manager.makeNewSubtask(subtask, epic.getID());
        manager.makeNewTask(task);
        manager.getTaskByID(task.getID());
        manager.getEpicByID(epic.getID());
        manager.getSubtaskByID(subtask.getID());
        ArrayList<Integer> expected = manager.getHistoryAsList();
        URI uri = URI.create("http://localhost:8080/tasks/history/");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();
        String body = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8)).body();
        assertEquals(gson.toJson(expected), body);
    }

    @Test
    void getPrioritizedTasksTest() throws InterruptedException, IOException {
        manager.makeNewTask(epic);
        manager.makeNewSubtask(subtask, epic.getID());
        manager.makeNewTask(task);
        TreeSet<Task> expected = manager.getPrioritizedTasks();
        URI uri = URI.create("http://localhost:8080/tasks/");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();
        String body = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8)).body();
        assertEquals(gson.toJson(expected), body);
    }

}
