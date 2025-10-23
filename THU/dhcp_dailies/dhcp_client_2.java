import java.net.*;
import java.nio.*;
import java.util.*;

public class dhcp_client_2 {
    public static void main(String[] args) throws Exception {
        try (DatagramSocket s = new DatagramSocket()) {
            s.setBroadcast(true);
            s.setSoTimeout(4000);
            byte[] mac = {2,0,0,0,0,1};
            int xid = new Random().nextInt();
            s.send(new DatagramPacket(buildDiscover(xid, mac), 244, InetAddress.getByName("255.255.255.255"), 6767));
            System.out.println("[>] DISCOVER sent, waiting...");
            byte[] buf = new byte[1500];
            try {
                s.receive(new DatagramPacket(buf, buf.length));
                var ip = readIp(buf, 16);
                System.out.printf("[<] OFFERED IP: %s%n", ip);
            } catch (SocketTimeoutException e) {
                System.out.println("[x] Timeout: no OFFER.");
            }
        }
    }

    static byte[] buildDiscover(int xid, byte[] mac) {
        var b = ByteBuffer.allocate(244);
        b.put((byte)1).put((byte)1).put((byte)6).put((byte)0)
         .putInt(xid).putShort((short)0).putShort((short)0)
         .put(new byte[16]) // ciaddr+yiaddr+siaddr+giaddr
         .put(Arrays.copyOf(mac, 16)) // chaddr padded
         .put(new byte[64+128]) // sname+file
         .putInt(0x63825363) // magic cookie
         .put((byte)53).put((byte)1).put((byte)1) // DHCPDISCOVER
         .put((byte)255);
        return b.array();
    }

    static String readIp(byte[] buf, int off) {
        return String.format("%d.%d.%d.%d", buf[off]&255, buf[off+1]&255, buf[off+2]&255, buf[off+3]&255);
    }
}
