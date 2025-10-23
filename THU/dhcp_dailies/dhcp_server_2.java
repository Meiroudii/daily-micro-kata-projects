import java.net.*;
import java.nio.*;
import java.util.*;

public class dhcp_server_2 {
    static final int PORT = 6767;
    static final byte[] COOKIE = {(byte)0x63,(byte)0x82,(byte)0x53,(byte)0x63};
    static final Map<String, String> leases = new HashMap<>();
    static InetAddress srvIp;

    public static void main(String[] args) throws Exception {
        srvIp = InetAddress.getByName("127.0.0.1");
        try (DatagramSocket sock = new DatagramSocket(PORT)) {
            System.out.println("[*] DHCP mini server on UDP/" + PORT);
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
        String macStr = fmtMac(mac);

        int optStart = 236 + 4; // skip BOOTP + magic cookie
        int msgType = -1;
        for (int i = optStart; i < d.length - 2; i++) {
            int code = d[i] & 0xFF;
            if (code == 53) msgType = d[i + 2] & 0xFF;
            if (code == 255) break;
        }

        System.out.printf("[RX] xid=%08x type=%d mac=%s%n", xid, msgType, macStr);

        if (msgType == 1) { // DISCOVER
            String ip = leases.computeIfAbsent(macStr, k -> "10.0.0." + (100 + leases.size()));
            System.out.println("[>] OFFER " + ip);
            byte[] offer = buildReply(xid, mac, ip, (byte) 2);
            DatagramPacket resp = new DatagramPacket(
                    offer, offer.length,
                    InetAddress.getByName("255.255.255.255"), 6868
            );
            sock.send(resp);
        }
    }

    static byte[] buildReply(int xid, byte[] mac, String ip, byte type) throws Exception {
        // allocate extra room for all options (avoid BufferOverflow)
        var b = ByteBuffer.allocate(300);
        b.put((byte) 2).put((byte) 1).put((byte) 6).put((byte) 0)
         .putInt(xid).putShort((short) 0).putShort((short) 0)
         .put(new byte[4]) // ciaddr
         .put(InetAddress.getByName(ip).getAddress()) // yiaddr
         .put(new byte[8]) // siaddr + giaddr
         .put(Arrays.copyOf(mac, 16))
         .put(new byte[64 + 128])
         .put(COOKIE)
         // options
         .put((byte) 53).put((byte) 1).put(type)         // Message Type
         .put((byte) 54).put((byte) 4).put(srvIp.getAddress()) // Server Identifier
         .put((byte) 51).put((byte) 4).putInt(3600)     // Lease time
         .put((byte) 1).put((byte) 4).put(new byte[]{(byte)255,(byte)255,(byte)255,0}) // subnet
         .put((byte) 3).put((byte) 4).put(ipToBytes("10.0.0.1")) // gateway
         .put((byte) 6).put((byte) 4).put(ipToBytes("8.8.8.8"))  // DNS
         .put((byte) 255); // END

        // trim to exact length actually written
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
