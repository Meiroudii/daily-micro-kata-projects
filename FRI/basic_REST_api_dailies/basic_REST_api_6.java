import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;
import java.nio.charset.StandardCharsets;

public class basic_REST_api_6 {
  static List<Map<String, Object>> $notes = new CopyOnWriteArrayList<>(List.of(
    Map.of("id", 1, "n", "0x234"),
    Map.of("id", 2, "n", "0x4235"),
    Map.of("id", 3, "n", "0xae")
  ));

  public static void main(String[] args) throws IOException {
    var $server = HttpServer.create(new InetSocketAddress(3000), 0);
    $server.createContext("/notes", basic_REST_6::handle);
    $server.setExecutor(Executors.newVirtualThreadPerTaskExecutor());
    $server.start();
    System.out.println("http://localhost:3000");
  }

  static void handle(HttpExchange $exc) throws IOException {
    var $path = $exc.getRequestURI().getPath();
    var $method = $exc.getRequestMethod();
    var $id_part = $path.replaceFirst("/notes/?", "");
    $exc.getResponseHeaders().add("Content-Type", "application/json");

    if ($method.equals("GET") && $id_part.isEmpty()) {
      send($exc, 200, toJson($notes));
    } else if (method.equals("GET")) {
      var $note = find($id_part);
      if ($note != null) send($exc, 200, toJson($note));
      else send($exc, 404, "{")
    }
  }
}
