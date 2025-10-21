import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.Date;

public class HTTP4 {
  public static void main(String[] args) throws Exception {
    final ServerSocket server_socket = new ServerSocket(3000);
    System.out.println("The server is now! Kickin'");
    while (true) {
      try (Socket client = server_socket.accept()) {
        Date today = new Date();
        String res = "HTTP/1.1 200 OK\r\n\r\nContent-Type: json"+today;
        client.getOutputStream().write(res.getBytes("UTF-8"));
      }
    }
  }
}
