import java.net.*;
import java.nio.*;
import java.util.*;

public class dhcp_server_2_1 {
  static final int PORT = 6767;
  static final byte[] COOKIE = {
    (byte)0x63,
    (byte)0x82,
    (byte)0x53,
    (byte)0x63
  };
  static final Map<String, String> leases = new HashMap<>();
  static InetAddress serve_ip;

  public static void main(String[] args) throws Exception {
    serve_ip = InetAddress.getByName("127.0.0.1");
    try (DatagramSocket sock = new DatagramSocket(PORT)) {
      System.out.println("[*] DHCP server on UDP/"+PORT);
      byte[] buf = new byte[1500];
      while (true) {
        DatagramPacket p = new DatagramPacket(buf, buf.length);
        sock.receive(p);
        byte[] data = Arrays.copyOf(p.getData(), p.getLength());
        handle(sock, data);
      }
    }
  }
  static void handle(DatagramSocket sock, byte[] d) throws Exception {
    var bb = ByteBuffer.wrap(d).order(ByteOrder.BIG_ENDIAN);
    bb.position(4);
    int xid = bb.getInt();
    bb.position(28);
    byte[] mac = new byte[6];
    bb.get(mac);
    String mac_str = fmtMac(mac);

    int opt_start = 236 + 4;
    int msg_type = -1;
    for (int i = opt_start; i < d.length - 2; i++) {
      int code = d[i] & 0xFF;
      if (code == 53 ) msg_type = d[i + 2] & 0xFF;
      if (code == 255) break;
    }
    System.out.printf("[RX] xid=%08x type=%d mac=%s%n", xid, msg_type, mac_str);
    if (msg_type == 1) {
      String ip = leases.computeIfAbsent(mac_str, k -> "10.0.0."+ (100 + leases.size()));
      System.out.println("[>] OFFER "+ip);
      byte[] offer = build_reply(xid, mac, ip, (byte)2); 
      DatagramPacket resp = new DatagramPacket(
        offer, offer.length,
        InetAddress.getByName("255.255.255.255"), 6868
      );
      sock.send(resp);
    }
  }

  static byte[] build_reply(int xid, byte[] mac, String ip, byte type) throws Exception {
    var b = ByteBuffer.allocate(300);
    b.put((byte) 2).put((byte) 1).put((byte) 6).put((byte)0)
      .putInt(xid).putShort((short) 0).putShort((short) 0)
      .put(new byte[4])
      .put(InetAddress.getByName(ip).getAddress())
      .put(new byte[8])
      .put(Arrays.copyOf(mac, 16))
      .put(new byte[64 + 128])
      .put(COOKIE)
      .put((byte) 53).put((byte) 1).put(type)
      .put((byte) 54).put((byte) 4).put(serve_ip.getAddress())
      .put((byte) 51).put((byte) 4).putInt(3600)
      .put((byte) 1).put((byte) 4).put(ipToBytes("10.0.0.1"))
      .put((byte) 6).put((byte) 4).put(ipToBytes("8.8.8.8"))
      .put((byte) 255);
    return Arrays.copyOf(b.array(), b.position());
  }

  static byte[] ipToBytes(String ip) throws UnknownHostException {
    return InetAddress.getByName(ip).getAddress();
  }

  static String fmtMac(byte[] mac) {
    var sb = new StringBuilder();
    for (byte m : mac) sb.append(String.format("%02x:", m));
    return sb.substring(0, sb.length() - 1);
  }
}
