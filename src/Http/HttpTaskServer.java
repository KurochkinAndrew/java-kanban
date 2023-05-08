package Http;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.MalformedJsonException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import manager.FileBackedTasksManager;
import manager.HttpTaskManager;
import manager.Managers;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class HttpTaskServer {
    private HttpServer server;
    public static FileBackedTasksManager manager;
    private static Gson gson = new Gson();

    public HttpTaskServer() {
        try {
            server = HttpServer.create(new InetSocketAddress(8080), 0);
            server.createContext("/tasks/task/", new TasksHandler());
            server.createContext("/tasks/epic/", new EpicsHandler());
            server.createContext("/tasks/subtask/", new SubtasksHandler());
            server.createContext("/tasks/subtask/epic/", new SubtasksOfEpicHandler());
            server.createContext("/tasks/", new PrioritizedTasksHandler());
            server.createContext("/tasks/history/", new HistoryHandler());
            manager = new FileBackedTasksManager();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void start(){
        server.start();
    }

    public void stop(){
        server.stop(1);
    }

    private static class TasksHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            Method method = getRequestMethod(exchange);

            switch (method) {
                case GET:
                    if (exchange.getRequestURI().getQuery() != null) {
                        Integer id = getIdFromQuery(exchange.getRequestURI().getQuery());
                        if (id == null) {
                            writeResponse(exchange, "Некорректный id", 400);
                        }
                        try {
                            Task task = manager.getTaskByID(id);
                            writeResponse(exchange, gson.toJson(task), 200);
                        } catch (NullPointerException e) {
                            writeResponse(exchange, "Task с id=" + id + " не найден", 404);
                        }
                    } else {
                        writeResponse(exchange, gson.toJson(manager.getAllTasks()), 200);
                    }
                    break;
                case POST:
                    String requestBody;
                    try (InputStream is = exchange.getRequestBody()) {
                        requestBody = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                        boolean isValid = requestBody.contains("name") & requestBody.contains("description") &
                                requestBody.contains("status") & !(requestBody == null || requestBody.isBlank());
                        if (!isValid) {
                            throw new NullPointerException();
                        }
                        Task task = gson.fromJson(requestBody, Task.class);
                        manager.makeNewTask(task);
                        writeResponse(exchange, "Task добавлен", 200);
                    } catch (JsonSyntaxException | MalformedJsonException | NullPointerException e) {
                        writeResponse(exchange,
                                "Некорректное тело запроса. Task не добавлен", 400);
                    }
                    break;
                case DELETE:
                    if (exchange.getRequestURI().getQuery() != null) {
                        Integer id = getIdFromQuery(exchange.getRequestURI().getQuery());
                        if (id == null) {
                            writeResponse(exchange, "Некорректный id", 400);
                        }
                        try {
                            manager.getTaskByIdWithoutHistoryAdd(id);
                            manager.removeTaskByID(id);
                            writeResponse(exchange, "Task удален", 200);
                        } catch (NullPointerException e) {
                            writeResponse(exchange, "Task с id=" + id + " не найден", 404);
                        }
                    } else {
                        manager.removeAllTasks();
                        writeResponse(exchange, "Все task удалены", 200);
                    }
                    break;
                case UNKNOWN:
                    writeResponse(exchange, "Метод не поддерживается", 405);
            }
        }
    }

        private static class EpicsHandler implements HttpHandler {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                Method method = getRequestMethod(exchange);
                switch (method) {
                    case GET:
                        if (exchange.getRequestURI().getQuery() != null) {
                            Integer id = getIdFromQuery(exchange.getRequestURI().getQuery());
                            if (id == null) {
                                writeResponse(exchange, "Некорректный id", 400);
                            }
                            try {
                                Epic epic = manager.getEpicByID(id);
                                writeResponse(exchange, gson.toJson(epic), 200);
                            } catch (NullPointerException e) {
                                writeResponse(exchange, "Epic с id=" + id + " не найден", 404);
                            }
                        } else {
                            writeResponse(exchange, gson.toJson(manager.getAllEpics()), 200);
                        }
                        break;
                    case POST:
                        String requestBody;
                        try (InputStream is = exchange.getRequestBody()) {
                            requestBody = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                            boolean isValid = requestBody.contains("name") & requestBody.contains("description")
                                    & !(requestBody == null || requestBody.isBlank());
                            if (!isValid) {
                                throw new NullPointerException();
                            }
                            Epic epic = gson.fromJson(requestBody, Epic.class);
                            manager.makeNewTask(epic);
                            writeResponse(exchange, "Epic добавлен", 200);
                        } catch (JsonSyntaxException | MalformedJsonException | NullPointerException e) {
                            writeResponse(exchange,
                                    "Некорректное тело запроса. Epic не добавлен", 400);
                        }
                        break;
                    case DELETE:
                        if (exchange.getRequestURI().getQuery() != null) {
                            Integer id = getIdFromQuery(exchange.getRequestURI().getQuery());
                            if (id == null) {
                                writeResponse(exchange, "Некорректный id", 400);
                            }
                            try {
                                manager.getEpicByIdWithoutHistoryAdd(id);
                                manager.removeEpicByID(id);
                                writeResponse(exchange, "Epic удален", 200);
                            } catch (NullPointerException e) {
                                writeResponse(exchange, "Epic с id=" + id + " не найден", 404);
                            }
                        } else {
                            manager.removeAllEpics();
                            writeResponse(exchange, "Все epic удалены", 200);
                        }
                        break;
                    case UNKNOWN:
                        writeResponse(exchange, "Метод не поддерживается", 405);
                }
            }
        }

    private static class SubtasksHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            Method method = getRequestMethod(exchange);
            switch (method) {
                case GET:
                    if (exchange.getRequestURI().getQuery() != null) {
                        Integer id = getIdFromQuery(exchange.getRequestURI().getQuery());
                        if (id == null) {
                            writeResponse(exchange, "Некорректный id", 400);
                        }
                        try {
                            Subtask subtask = manager.getSubtaskByID(id);
                            writeResponse(exchange, gson.toJson(subtask), 200);
                        } catch (NullPointerException e) {
                            writeResponse(exchange, "Subtask с id=" + id + " не найден", 404);
                        }
                    } else {
                        writeResponse(exchange, gson.toJson(manager.getAllSubtasks()), 200);
                    }
                    break;
                case POST:
                    String requestBody;
                    try (InputStream is = exchange.getRequestBody()) {
                        requestBody = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                        boolean isValid = requestBody.contains("name") & requestBody.contains("description")
                                & requestBody.contains("epicOwnerID") & !(requestBody == null || requestBody.isBlank());
                        if (!isValid) {
                            throw new NullPointerException();
                        }
                        Subtask subtask = gson.fromJson(requestBody, Subtask.class);
                        manager.makeNewSubtask(subtask, subtask.getEpicOwnerID());
                        writeResponse(exchange, "Subtask добавлен", 200);
                    } catch (JsonSyntaxException | MalformedJsonException | NullPointerException e) {
                        writeResponse(exchange,
                                "Некорректное тело запроса. Subtask не добавлен", 400);
                    }
                    break;
                case DELETE:
                    if (exchange.getRequestURI().getQuery() != null) {
                        Integer id = getIdFromQuery(exchange.getRequestURI().getQuery());
                        if (id == null) {
                            writeResponse(exchange, "Некорректный id", 400);
                        }
                        try {
                            manager.getSubtaskByIdWithoutHistoryAdd(id);
                            manager.removeSubtaskByID(id);
                            writeResponse(exchange, "Subtask удален", 200);
                        } catch (NullPointerException e) {
                            writeResponse(exchange, "Subtask с id=" + id + " не найден", 404);
                        }
                    } else {
                        manager.removeAllSubtasks();
                        writeResponse(exchange, "Все subtask удалены", 200);
                    }
                    break;
                case UNKNOWN:
                    writeResponse(exchange, "Метод не поддерживается", 405);
            }
        }
    }

    private static class PrioritizedTasksHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            Method method = getRequestMethod(exchange);
            if (method.equals(Method.GET)){
                writeResponse(exchange, gson.toJson(manager.getPrioritizedTasks()), 200);
            } else {
                writeResponse(exchange, "Метод не поддерживается", 405);
            }
        }
    }

    private static class HistoryHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            Method method = getRequestMethod(exchange);
            if (method.equals(Method.GET)){
                writeResponse(exchange, gson.toJson(manager.getHistoryAsList()), 200);
            } else {
                writeResponse(exchange, "Метод не поддерживается", 405);
            }
        }
    }

    private static class SubtasksOfEpicHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            Method method = getRequestMethod(exchange);
            Integer id = getIdFromQuery(exchange.getRequestURI().getQuery());
            if (method.equals(Method.GET) & id != null){
                try {
                    manager.getEpicByIdWithoutHistoryAdd(id);
                    writeResponse(exchange, gson.toJson(manager.getSubtasksOfEpic(id)), 200);
                } catch (NullPointerException e){
                    writeResponse(exchange, "Epic c id=" + id + " не найден", 404);
                }
            } else {
                if (id == null){
                    writeResponse(exchange, "Некорректный id", 400);
                }
                writeResponse(exchange, "Метод не поддерживается", 405);
            }
        }
    }


        private static Integer getIdFromQuery(String query){
            try {
                int id = Integer.parseInt(query.substring(query.indexOf('=') + 1));
                return id;
            } catch (Exception e){
                return null;
            }
        }

        private static void writeResponse(HttpExchange e,
                                   String responseString,
                                   int responseCode) throws IOException {
            e.sendResponseHeaders(responseCode, 0);
            if (!responseString.isBlank()) {
                try(OutputStream os = e.getResponseBody()) {
                    os.write(responseString.getBytes(StandardCharsets.UTF_8));
                }
            }
        }

        private static Method getRequestMethod(HttpExchange exchange){
            Method method;
            try {
                method = Method.valueOf(exchange.getRequestMethod());
            } catch (IllegalArgumentException e) {
                method = Method.UNKNOWN;
            }
            return method;
        }

        public FileBackedTasksManager getManager(){
        return manager;
        }


    enum Method {GET, POST, DELETE, UNKNOWN}
}
