import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Date;
import java.net.ServerSocket;
import java.net.Socket;

class HTTP7_3 {
  public static void main(String[] args) throws IOException{
    final ServerSocket SERVER = new ServerSocket(3000);
    System.out.println("localhost:3000");
    while(true) {
      try (Socket client = SERVER.accept()) {
        InputStreamReader isr = new InputStreamReader(client.getInputStream());
        System.out.println("ISR: "+isr);
        BufferedReader b = new BufferedReader(isr);
        System.out.println("BufferedReader: "+b);
        String l = b.readLine();
        System.out.println("LINE: "+l);
        while(!l.isEmpty()) {
          ///////// HERE I WANT SWING TO RENDER HERE! LIKE system.out.println(); thingy
          System.out.println(l);
          l = b.readLine();
        }
      }
    }
  }
}
