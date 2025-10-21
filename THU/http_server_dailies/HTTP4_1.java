import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

class HTTP4_1 {
  public static void main(String[] args) throws IOException {
    try {
      final ServerSocket server = new ServerSocket(3000, 50);
      System.out.println("Server is open");
      while (true) {
        try (Socket client = server.accept()) {
          Date today = new Date();
          String res = "HTTP/1.1 200 OK\r\n\r\n"+today;
          client.getOutputStream().write(res.getBytes("UTF-8"));
        } catch (IOException e) {
          System.err.println("Oof, Something bad happened.");
        }
      }
    } catch (IOException e) {
      System.err.println("Error starting server:" + e.getMessage());
    }
  }
}
