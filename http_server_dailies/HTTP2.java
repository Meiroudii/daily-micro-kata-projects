import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.Scanner;

public class HTTP2 {

  public static void kick_the_server(int port) throws IOException {
    final ServerSocket server = new ServerSocket(port);
    System.out.println("Server is now live!");
    while(true) {
      try (Socket client = server.accept()) {
        Date today = new Date();
        String http_response = "HTTP/1.1 200 OK \r\n\r\n" + today;
        client.getOutputStream().write(http_response.getBytes("UTF-8"));

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

  public static void main(String[] args) throws IOException {
    int port;
    Scanner get_input = new Scanner(System.in);
    System.out.print("Enter the port: ");
    while (!get_input.hasNextInt()) get_input.next();
    port = get_input.nextInt();
    kick_the_server(port);
  }
}
