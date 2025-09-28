import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Date;
import java.net.Socket;
import java.net.ServerSocket;

class HTTP6 {
  public static void main(String[] args) throws IOException {
    final ServerSocket server = new ServerSocket(8080);
    System.out.println("The server is now live.");
    while(true) {
      try (Socket client = server.accept()) {
        Date today = new Date();
        String res = "HTTP/1.1 200 OK\r\n\r\n"+today;
        client.getOutputStream().write(res.getBytes("UTF-8"));

        InputStreamReader read_stream = new InputStreamReader(client.getInputStream());
        BufferedReader read_buffer = new BufferedReader(read_stream);
        String line = read_buffer.readLine();
        while(!line.isEmpty()) {
          System.out.println(line);
          line = read_buffer.readLine();
        }
      }
    }
  }
}
