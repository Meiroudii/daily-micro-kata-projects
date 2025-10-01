import java.util.Scanner;
import java.util.Date;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class HTTP3 {
  public static void main(String[] args) throws IOException {
    final ServerSocket server = new ServerSocket(3000);
    System.out.println("The server is live!");
    while(true) {
      try (Socket client = server.accept()) {
        Date today = new Date();
        String http_response = "HTTP/1.1 200 OK\r\n\r\n" + today;
        client.getOutputStream().write(http_response.getBytes("UTF-8"));
      }

    }
  }
}
