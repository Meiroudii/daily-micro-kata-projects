import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class HTTP1 {
  public static void main(String[] args) throws IOException {
    final int PORT = 3000;
    final ServerSocket server = new ServerSocket(PORT);
    System.out.println("Server is now live!");
    while (true) {
      final Socket client = server.accept();
      InputStreamReader isr = new InputStreamReader(client.getInputStream());
      BufferedReader reader = new BufferedReader(isr);
      String line = reader.readLine();
      while(!line.isEmpty()) {
        System.out.println(line);
        line = reader.readLine();
      }
    }
  }
}
