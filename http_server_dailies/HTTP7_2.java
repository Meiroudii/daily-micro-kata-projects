import java.util.Date;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

class HTTP7_2 {
  public static void main(String[] args) throws IOException {
    final ServerSocket SERVER = new ServerSocket(3000);
    while(true) {
      try (Socket client = SERVER.accept()) {
        Date today = new Date();
        String r = "HTTP/1.1 200 OK\r\n\r\n"+today;
        client.getOutputStream().write(r.getBytes("UTF-8"));

        InputStreamReader isr = new InputStreamReader(client.getInputStream());
        BufferedReader rb = new BufferedReader(isr);

        String line = rb.readLine();
        while(!line.isEmpty()) {
          System.out.println(line);
          line = rb.readLine();
        }
      }
    }
  }
}
