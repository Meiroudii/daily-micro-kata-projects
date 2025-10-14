import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;

class HTTP25 {
  public static void main(String[] args) throws IOException{
    final int PORT = 3000;
    final ServerSocket SERVER = new ServerSocket(PORT);
    System.out.println("Listening on Port: "+PORT);
    while(true) {
      try (Socket client = SERVER.accept()) {
        InputStreamReader isr = new InputStreamReader(client.getInputStream());
        BufferedReader buf = new BufferedReader(isr);
        String res = buf.readLine();
        while (res != null && !res.isEmpty()) {
          System.out.println(res);
          res = buf.readLine();
        }
      } catch (NullPointerException err) {
        System.out.println(err.getMessage());
      }
    }
  }
}
