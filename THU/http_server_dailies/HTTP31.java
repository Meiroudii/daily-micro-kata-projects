import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.util.Date;

class HTTP31 {
  public static void main(String[] args) throws Exception {
    final ServerSocket $SERVER = new ServerSocket(3000);
    System.out.println("final ServerSocket $SERVER = new ServerSocket(3000);");
    while(true) {
      try (Socket $c = $SERVER.accept()) {
        Date $today = new Date();
        String $res = "HTTP/1.1 200 OK\r\n\r\n"+today;
        $c.getOutputStream().write($res.getBytes("UTF-8"));
      }
    }
  }
}
