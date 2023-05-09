package Tests;

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
    private HttpTaskServer httpTaskServer;
    private FileBackedTasksManager manager;
    private HttpClient client = HttpClient.newHttpClient();
    private Gson gson = new Gson();


    @BeforeEach
    void beforeEach() {
        httpTaskServer = new HttpTaskServer();
        manager = httpTaskServer.getManager();
        httpTaskServer.start();
    }

    @AfterEach
    void afterEach() {
        manager.clearSaveFile();
        httpTaskServer.stop();
    }

    @Test
    void shouldPostTask() throws InterruptedException, IOException {
        Task task = new Task("Погулять с собакой", "Вечерняя прогулка", Status.NEW,
                LocalDateTime.of(2023, 1, 1, 0, 21), Duration.ofSeconds(150), 0);
        URI uri = URI.create("http://localhost:8080/tasks/task/?id=0");
        HttpRequest.BodyPublisher bodyPublisher =
                HttpRequest.BodyPublishers.ofString(gson.toJson(task));
        HttpRequest request = HttpRequest.newBuilder()
                .POST(bodyPublisher)
                .uri(uri)
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        assertEquals(task.toString(), manager.getTaskByID(0).toString());
        assertEquals(200, response.statusCode());

        bodyPublisher = HttpRequest.BodyPublishers.ofString(task.toString());
        request = HttpRequest.newBuilder()
                .POST(bodyPublisher)
                .uri(uri)
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        assertEquals(400, response.statusCode());
    }

    @Test
    void shouldGetTask() throws InterruptedException, IOException {
        Task task = new Task("Погулять с собакой", "Вечерняя прогулка", Status.NEW,
                LocalDateTime.of(2023, 1, 1, 0, 21), Duration.ofSeconds(150), 0);
        manager.makeNewTask(task);
        URI uri = URI.create("http://localhost:8080/tasks/task/?id=0");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        String body = response.body();
        assertEquals(gson.toJson(task), body);
        assertEquals(200, response.statusCode());

        uri = URI.create("http://localhost:8080/tasks/task/?id=10");
        request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        assertEquals(404, response.statusCode());

        uri = URI.create("http://localhost:8080/tasks/task/?id=adasf");
        request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        assertEquals(400, response.statusCode());
    }

    @Test
    void shouldDeleteTask() throws InterruptedException, IOException {
        Task task = new Task("Погулять с собакой", "Вечерняя прогулка", Status.NEW,
                LocalDateTime.of(2023, 1, 1, 0, 21), Duration.ofSeconds(150), 0);
        manager.makeNewTask(task);
        URI uri = URI.create("http://localhost:8080/tasks/task/?id=0");
        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .uri(uri)
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        assertEquals(0, manager.getAllTasks().size());
        assertEquals(200, response.statusCode());

        uri = URI.create("http://localhost:8080/tasks/task/?id=10");
        request = HttpRequest.newBuilder()
                .DELETE()
                .uri(uri)
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        assertEquals(404, response.statusCode());

        uri = URI.create("http://localhost:8080/tasks/task/?id=adasf");
        request = HttpRequest.newBuilder()
                .DELETE()
                .uri(uri)
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        assertEquals(400, response.statusCode());
    }

    @Test
    void shouldPostEpic() throws InterruptedException, IOException {
        Epic epic = new Epic("Починить машину", "Нужно поменять топливный насос", 0, Status.NEW);
        URI uri = URI.create("http://localhost:8080/tasks/epic/?id=0");
        HttpRequest.BodyPublisher bodyPublisher =
                HttpRequest.BodyPublishers.ofString(gson.toJson(epic));
        HttpRequest request = HttpRequest.newBuilder()
                .POST(bodyPublisher)
                .uri(uri)
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        assertEquals(epic.toString(), manager.getEpicByID(0).toString());
        assertEquals(200, response.statusCode());

        bodyPublisher = HttpRequest.BodyPublishers.ofString(epic.toString());
        request = HttpRequest.newBuilder()
                .POST(bodyPublisher)
                .uri(uri)
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        assertEquals(400, response.statusCode());
    }

    @Test
    void shouldGetEpic() throws InterruptedException, IOException {
        Epic epic = new Epic("Починить машину", "Нужно поменять топливный насос", 0, Status.NEW);
        manager.makeNewTask(epic);
        URI uri = URI.create("http://localhost:8080/tasks/epic/?id=0");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        String body = response.body();
        assertEquals(gson.toJson(epic), body);
        assertEquals(200, response.statusCode());

        uri = URI.create("http://localhost:8080/tasks/epic/?id=15");
        request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        assertEquals(404, response.statusCode());

        uri = URI.create("http://localhost:8080/tasks/epic/?id=!@2");
        request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        assertEquals(400, response.statusCode());
    }

    @Test
    void shouldDeleteEpic() throws InterruptedException, IOException {
        Epic epic = new Epic("Починить машину", "Нужно поменять топливный насос", 0, Status.NEW);
        manager.makeNewTask(epic);
        URI uri = URI.create("http://localhost:8080/tasks/epic/?id=0");
        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .uri(uri)
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        assertEquals(0, manager.getAllEpics().size());
        assertEquals(200, response.statusCode());

        uri = URI.create("http://localhost:8080/tasks/epic/?id=9");
        request = HttpRequest.newBuilder()
                .DELETE()
                .uri(uri)
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        assertEquals(404, response.statusCode());

        uri = URI.create("http://localhost:8080/tasks/task/?id=+-2");
        request = HttpRequest.newBuilder()
                .DELETE()
                .uri(uri)
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        assertEquals(400, response.statusCode());
    }

    @Test
    void shouldPostSubtask() throws InterruptedException, IOException {
        Epic epic = new Epic("Починить машину", "Нужно поменять топливный насос", 0, Status.NEW);
        Subtask subtask = new Subtask("Купить детали", "Купить топливный насос и болты",
                Status.DONE, 0, LocalDateTime.of(2023, 1, 1, 0, 30),
                Duration.ofSeconds(150), 1);
        manager.makeNewTask(epic);
        URI uri = URI.create("http://localhost:8080/tasks/subtask/?id=1");
        HttpRequest.BodyPublisher bodyPublisher =
                HttpRequest.BodyPublishers.ofString(gson.toJson(subtask));
        HttpRequest request = HttpRequest.newBuilder()
                .POST(bodyPublisher)
                .uri(uri)
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        assertEquals(subtask.toString(), manager.getSubtaskByID(1).toString());
        assertEquals(200, response.statusCode());

        bodyPublisher = HttpRequest.BodyPublishers.ofString(subtask.toString());
        request = HttpRequest.newBuilder()
                .POST(bodyPublisher)
                .uri(uri)
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        assertEquals(400, response.statusCode());
    }

    @Test
    void shouldGetSubtask() throws InterruptedException, IOException {
        Epic epic = new Epic("Починить машину", "Нужно поменять топливный насос", 0, Status.NEW);
        Subtask subtask = new Subtask("Купить детали", "Купить топливный насос и болты",
                Status.DONE, 0, LocalDateTime.of(2023, 1, 1, 0, 30),
                Duration.ofSeconds(150), 1);
        manager.makeNewTask(epic);
        manager.makeNewSubtask(subtask, epic.getID());
        URI uri = URI.create("http://localhost:8080/tasks/subtask/?id=1");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        String body = response.body();
        assertEquals(gson.toJson(subtask), body);
        assertEquals(200, response.statusCode());

        uri = URI.create("http://localhost:8080/tasks/subtask/?id=4");
        request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        assertEquals(404, response.statusCode());

        uri = URI.create("http://localhost:8080/tasks/epic/?id=l2a");
        request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        assertEquals(400, response.statusCode());
    }

    @Test
    void shouldDeleteSubtask() throws InterruptedException, IOException {
        Epic epic = new Epic("Починить машину", "Нужно поменять топливный насос", 0, Status.NEW);
        Subtask subtask = new Subtask("Купить детали", "Купить топливный насос и болты",
                Status.DONE, 0, LocalDateTime.of(2023, 1, 1, 0, 30),
                Duration.ofSeconds(150), 1);
        manager.makeNewTask(epic);
        manager.makeNewSubtask(subtask, epic.getID());
        URI uri = URI.create("http://localhost:8080/tasks/subtask/?id=1");
        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .uri(uri)
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        assertEquals(0, manager.getAllSubtasks().size());
        assertEquals(200, response.statusCode());

        uri = URI.create("http://localhost:8080/tasks/subtask/?id=20");
        request = HttpRequest.newBuilder()
                .DELETE()
                .uri(uri)
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        assertEquals(404, response.statusCode());

        uri = URI.create("http://localhost:8080/tasks/subtask/?id=zxc");
        request = HttpRequest.newBuilder()
                .DELETE()
                .uri(uri)
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        assertEquals(400, response.statusCode());
    }

    @Test
    void shouldGetSubtasksOfEpic() throws InterruptedException, IOException {
        Epic epic = new Epic("Починить машину", "Нужно поменять топливный насос", 0, Status.NEW);
        Subtask subtask = new Subtask("Купить детали", "Купить топливный насос и болты",
                Status.DONE, 0, LocalDateTime.of(2023, 1, 1, 0, 30),
                Duration.ofSeconds(150), 1);
        manager.makeNewTask(epic);
        manager.makeNewSubtask(subtask, epic.getID());
        manager.makeNewSubtask(subtask, epic.getID());
        URI uri = URI.create("http://localhost:8080/tasks/subtask/epic/?id=0");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        String body = response.body();
        ArrayList<Task> expected = manager.getSubtasksOfEpic(epic.getID());
        assertEquals(gson.toJson(expected), body);
        assertEquals(200, response.statusCode());

        uri = URI.create("http://localhost:8080/tasks/subtask/epic/?id=12");
        request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        assertEquals(404, response.statusCode());

        uri = URI.create("http://localhost:8080/tasks/subtask/epic/?id=azq1");
        request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        assertEquals(400, response.statusCode());

        uri = URI.create("http://localhost:8080/tasks/subtask/epic/?id=10");
        request = HttpRequest.newBuilder()
                .DELETE()
                .uri(uri)
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        assertEquals(405, response.statusCode());
    }

    @Test
    void shouldGetHistory() throws InterruptedException, IOException {
        Epic epic = new Epic("Починить машину", "Нужно поменять топливный насос", 0, Status.NEW);
        Subtask subtask = new Subtask("Купить детали", "Купить топливный насос и болты",
                Status.DONE, 0, LocalDateTime.of(2023, 1, 1, 0, 30),
                Duration.ofSeconds(150), 1);
        Task task = new Task("Погулять с собакой", "Вечерняя прогулка", Status.NEW,
                LocalDateTime.of(2023, 1, 1, 0, 21), Duration.ofSeconds(150), 0);
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
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        String body = response.body();
        assertEquals(gson.toJson(expected), body);
        assertEquals(200, response.statusCode());

        uri = URI.create("http://localhost:8080/tasks/history/");
        request = HttpRequest.newBuilder()
                .DELETE()
                .uri(uri)
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        assertEquals(405, response.statusCode());
    }

    @Test
    void shouldGetPrioritizedTasks() throws InterruptedException, IOException {
        Epic epic = new Epic("Починить машину", "Нужно поменять топливный насос", 0, Status.NEW);
        Subtask subtask = new Subtask("Купить детали", "Купить топливный насос и болты",
                Status.DONE, 0, LocalDateTime.of(2023, 1, 1, 0, 30),
                Duration.ofSeconds(150), 1);
        Task task = new Task("Погулять с собакой", "Вечерняя прогулка", Status.NEW,
                LocalDateTime.of(2023, 1, 1, 0, 21), Duration.ofSeconds(150), 0);
        manager.makeNewTask(epic);
        manager.makeNewSubtask(subtask, epic.getID());
        manager.makeNewTask(task);
        TreeSet<Task> expected = manager.getPrioritizedTasks();
        URI uri = URI.create("http://localhost:8080/tasks/");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        String body = response.body();
        assertEquals(gson.toJson(expected), body);
        assertEquals(200, response.statusCode());

        uri = URI.create("http://localhost:8080/tasks/");
        request = HttpRequest.newBuilder()
                .DELETE()
                .uri(uri)
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        assertEquals(405, response.statusCode());
    }

}
