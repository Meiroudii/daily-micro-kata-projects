import java.util.Date;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

class HTTP7 {
  public static void main(String[] args) throws IOException {
    final int PORT = 3000;
    final ServerSocket SERVER = new ServerSocket(PORT);
    System.out.println("The server is open at port "+PORT);

    while (true) {
      try (Socket client = SERVER.accept()) {
        Date today = new Date();
        String res = "HTTP/1.1 200 OK\r\n\r\n"+today;
        client.getOutputStream().write(res.getBytes("UTF-8"));

        InputStreamReader inpt_strm_readr = new InputStreamReader(client.getInputStream());
        BufferedReader buffrd_readr = new BufferedReader(inpt_strm_readr);
        String line = buffrd_readr.readLine();
        while (!line.isEmpty()) {
          System.out.println(line);
          line = buffrd_readr.readLine();
        }
      }
    }
  }
}



