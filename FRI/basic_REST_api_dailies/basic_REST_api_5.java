import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;
import java.nio.charset.StandardCharsets;

public class basic_REST_api_5 {
    static List<Map<String, Object>> notes = new CopyOnWriteArrayList<>(List.of(
        Map.of("id", 1, "n", "Feed cats"),
        Map.of("id", 2, "n", "Feed dogs"),
        Map.of("id", 3, "n", "Cook veggies")
    ));

    public static void main(String[] args) throws IOException {
        var server = HttpServer.create(new InetSocketAddress(3000), 0);
        server.createContext("/notes", basic_REST_api_5::handle);
        server.setExecutor(Executors.newVirtualThreadPerTaskExecutor());
        server.start();
        System.out.println("Server running on http://localhost:3000");
    }

    static void handle(HttpExchange ex) throws IOException {
        var path = ex.getRequestURI().getPath();
        var method = ex.getRequestMethod();
        var idPart = path.replaceFirst("/notes/?", "");
        ex.getResponseHeaders().add("Content-Type", "application/json");

        if (method.equals("GET") && idPart.isEmpty()) {
            send(ex, 200, toJson(notes));
        } else if (method.equals("GET")) {
            var note = find(idPart);
            if (note != null) send(ex, 200, toJson(note));
            else send(ex, 404, "{\"e\":\"404\"}");
        } else if (method.equals("POST")) {
            var body = new String(ex.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            var text = extractJsonValue(body, "n");
            var newNote = new LinkedHashMap<String,Object>();
            newNote.put("id", notes.size() + 1);
            newNote.put("n", text);
            notes.add(newNote);
            send(ex, 201, toJson(newNote));
        } else if (method.equals("PUT")) {
            var note = find(idPart);
            if (note == null) send(ex, 404, "{\"e\":\"404\"}");
            else {
                var body = new String(ex.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                note.put("n", extractJsonValue(body, "n"));
                send(ex, 200, toJson(note));
            }
        } else if (method.equals("DELETE")) {
            var note = find(idPart);
            if (note == null) send(ex, 404, "{\"e\":\"404\"}");
            else {
                notes.remove(note);
                ex.sendResponseHeaders(204, -1);
            }
        } else send(ex, 405, "{\"e\":\"method not allowed\"}");
    }

    static Map<String,Object> find(String idPart) {
        try {
            int id = Integer.parseInt(idPart);
            return notes.stream().filter(n -> n.get("id").equals(id)).findFirst().orElse(null);
        } catch (Exception e) { return null; }
    }

    static void send(HttpExchange ex, int code, String json) throws IOException {
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        ex.sendResponseHeaders(code, bytes.length);
        ex.getResponseBody().write(bytes);
        ex.close();
    }

    static String toJson(Object o) {
        if (o instanceof List<?> list)
            return list.stream().map(basic_REST_api_5::toJson).collect(Collectors.joining(",", "[", "]"));
        if (o instanceof Map<?,?> map)
            return map.entrySet().stream()
                .map(e -> "\"" + e.getKey() + "\":\"" + e.getValue() + "\"")
                .collect(Collectors.joining(",", "{", "}"));
        return "\"" + o + "\"";
    }

    static String extractJsonValue(String json, String key) {
        var pattern = "\""+key+"\"\\s*:\\s*\"([^\"]+)\"";
        var m = java.util.regex.Pattern.compile(pattern).matcher(json);
        return m.find() ? m.group(1) : "";
    }
}
