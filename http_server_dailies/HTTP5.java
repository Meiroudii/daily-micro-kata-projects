import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.util.Date;

class HTTP5 {
  public static void main(String[] args) throws IOException {
    final ServerSocket server = new ServerSocket(3000);
    System.out.println("The server is now kickin'");
    while (true) {
      try (Socket client = server.accept()) {
        Date today = new Date();
        String res = "HTTP/1.1 200 OK\r\n\r\n"+today;
        client.getOutputStream().write(res.getBytes("UTF-8"));
      }
    }
  }
}
