import java.net.*;
import java.io.*;
import java.util.*;

/*
 * dhcp_server_1 - dev/demo server on UDP port 6767.
 * - Handles DISCOVER -> send OFFER
 * - Handles REQUEST  -> send ACK (simple acceptance)
 *
 * Not production: single-threaded, in-memory leases, no conflict checks.
 */
public class dhcp_server_1 {
    static final int SERVER_PORT = 6767; // dev port
    static final byte[] MAGIC_COOKIE = {(byte)0x63, (byte)0x82, (byte)0x53, (byte)0x63};
    static Map<String, Lease> leases = new HashMap<>();
    static InetAddress serverIp;

    static class Lease { String ip; long expiry; Lease(String ip, long expiry){this.ip=ip;this.expiry=expiry;} }

    public static void main(String[] args) throws Exception {
        DatagramSocket sock = new DatagramSocket(SERVER_PORT);
        sock.setSoTimeout(0);
        serverIp = InetAddress.getByName("127.0.0.1"); // for server identifier option in dev
        System.out.println("RAW DHCP SERVER listening on UDP/" + SERVER_PORT);

        byte[] buf = new byte[1500];
        while (true) {
            DatagramPacket p = new DatagramPacket(buf, buf.length);
            sock.receive(p);
            byte[] data = Arrays.copyOf(p.getData(), p.getLength());
            try {
                handlePacket(sock, p.getAddress(), p.getPort(), data);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static void handlePacket(DatagramSocket sock, InetAddress clientAddr, int clientPort, byte[] data) throws IOException {
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(data));
        int op = in.readUnsignedByte();
        int htype = in.readUnsignedByte();
        int hlen = in.readUnsignedByte();
        in.readUnsignedByte(); // hops
        int xid = in.readInt();
        in.readUnsignedShort(); // secs
        in.readUnsignedShort(); // flags
        byte[] ciaddr = new byte[4]; in.readFully(ciaddr);
        byte[] yiaddr = new byte[4]; in.readFully(yiaddr);
        byte[] siaddr = new byte[4]; in.readFully(siaddr);
        byte[] giaddr = new byte[4]; in.readFully(giaddr);

        byte[] chaddrFull = new byte[16]; in.readFully(chaddrFull);
        byte[] mac = Arrays.copyOf(chaddrFull, hlen);
        String macStr = macToStr(mac);

        // skip sname(64) + file(128)
        in.skipBytes(64 + 128);

        // read and validate magic cookie
        byte[] cookie = new byte[4];
        in.readFully(cookie);
        // options parse
        Map<Integer, byte[]> options = new HashMap<>();
        while (true) {
            int code = in.readUnsignedByte();
            if (code == 0) continue; // pad
            if (code == 255) break;  // end
            int len = in.readUnsignedByte();
            byte[] val = new byte[len];
            in.readFully(val);
            options.put(code, val);
        }

        byte[] optMsgType = options.get(53); // DHCP Message Type
        int msgType = (optMsgType != null && optMsgType.length>0) ? (optMsgType[0] & 0xFF) : -1;
        System.out.printf("RX: op=%d xid=0x%08x mac=%s msgType=%d from %s:%d%n", op, xid, macStr, msgType, clientAddr.getHostAddress(), clientPort);

        if (msgType == 1) { // DISCOVER
            // choose an IP for this MAC
            String offered = allocateIpFor(macStr);
            System.out.println("Offering " + offered + " to " + macStr);
            byte[] offer = buildReply(xid, mac, offered, (byte)2 /*OFFER*/, options);
            DatagramPacket resp = new DatagramPacket(offer, offer.length, InetAddress.getByName("255.255.255.255"), 6868); // broadcast dev
            // NOTE: we send to 255.255.255.255 port 6868 to simulate broadcast in dev.
            sock.send(resp);
        } else if (msgType == 3) { // REQUEST
            // In real server, parse requested IP option (50) and server id (54)
            String ip = leases.get(macStr) != null ? leases.get(macStr).ip : allocateIpFor(macStr);
            System.out.println("ACKing " + ip + " to " + macStr);
            byte[] ack = buildReply(xid, mac, ip, (byte)5 /*ACK*/, options);
            DatagramPacket resp = new DatagramPacket(ack, ack.length, InetAddress.getByName("255.255.255.255"), 6868);
            sock.send(resp);
            // set lease expiry
            leases.put(macStr, new Lease(ip, System.currentTimeMillis() + 3600_000));
        } else {
            System.out.println("Unhandled message type: " + msgType);
        }
    }

    static String allocateIpFor(String mac) {
        // very simple allocator: if exists, return; else pick next from 10.0.0.100+
        if (leases.containsKey(mac)) return leases.get(mac).ip;
        int base = 100;
        boolean found = false; String candidate = null;
        for (int i=base;i<250;i++){
            candidate = "10.0.0." + i;
            boolean used = false;
            for (Lease L : leases.values()) if (L.ip.equals(candidate)) { used=true; break; }
            if (!used) { found=true; break;}
        }
        if (!found) throw new RuntimeException("pool exhausted");
        leases.put(mac, new Lease(candidate, System.currentTimeMillis() + 3600_000));
        return candidate;
    }

    static byte[] buildReply(int xid, byte[] chaddr, String yourIp, byte dhcpMessageType, Map<Integer, byte[]> recvOptions) throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(bout);
        // op(2=reply), htype(1), hlen(6), hops
        out.writeByte(2); out.writeByte(1); out.writeByte(chaddr.length); out.writeByte(0);
        out.writeInt(xid);
        out.writeShort(0); out.writeShort(0); // secs, flags
        // ciaddr
        out.write(new byte[4]);
        // yiaddr (your IP)
        out.write(ipToBytes(yourIp));
        // siaddr, giaddr
        out.write(new byte[4]); out.write(new byte[4]);
        // chaddr (16 bytes)
        out.write(chaddr);
        if (chaddr.length < 16) out.write(new byte[16 - chaddr.length]);
        // sname (64) and file (128)
        out.write(new byte[64]); out.write(new byte[128]);
        // magic cookie
        out.write(MAGIC_COOKIE);

        // options:
        // 53 - DHCP Message Type
        out.writeByte(53); out.writeByte(1); out.writeByte(dhcpMessageType);
        // 54 - Server Identifier (our IP)
        byte[] sid = serverIp.getAddress();
        out.writeByte(54); out.writeByte(sid.length); out.write(sid);
        // 51 - Lease Time (4 bytes)
        out.writeByte(51); out.writeByte(4); out.writeInt(3600); // 1 hour
        // 1 - Subnet Mask 255.255.255.0
        out.writeByte(1); out.writeByte(4); out.write(new byte[]{(byte)255,(byte)255,(byte)255,0});
        // 3 - Router (gateway) -> use 10.0.0.1
        out.writeByte(3); out.writeByte(4); out.write(ipToBytes("10.0.0.1"));
        // 6 - DNS server
        out.writeByte(6); out.writeByte(4); out.write(ipToBytes("8.8.8.8"));
        // 255 - end
        out.writeByte(255);

        return bout.toByteArray();
    }

    static byte[] ipToBytes(String ip) throws UnknownHostException { return InetAddress.getByName(ip).getAddress(); }

    static String macToStr(byte[] mac) {
        StringBuilder sb = new StringBuilder();
        for (int i=0;i<mac.length;i++){
            if (i>0) sb.append(':');
            sb.append(String.format("%02x", mac[i]));
        }
        return sb.toString();
    }
}
