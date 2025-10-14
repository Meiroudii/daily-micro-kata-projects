import java.net.*;
import java.io.*;
import java.util.*;

public class dhcp_client_1 {
    public static void main(String[] args) throws Exception {
        DatagramSocket sock = new DatagramSocket();
        sock.setSoTimeout(5000);
        sock.setBroadcast(true);

        byte[] mac = new byte[]{(byte)0x02,0x00,0x00,0x00,0x00,0x01}; // fake MAC
        int xid = new Random().nextInt();

        byte[] discover = buildDiscover(xid, mac);
        DatagramPacket p = new DatagramPacket(discover, discover.length, InetAddress.getByName("255.255.255.255"), 6767);
        sock.send(p);
        System.out.println("DISCOVER sent, waiting for OFFER...");

        byte[] buf = new byte[1500];
        try {
            DatagramPacket resp = new DatagramPacket(buf, buf.length);
            sock.receive(resp);
            System.out.println("Got reply from " + resp.getAddress() + ":" + resp.getPort() + " len=" + resp.getLength());
            // naive parse: print yiaddr bytes at offset 16..19
            byte[] data = Arrays.copyOf(resp.getData(), resp.getLength());
            int yi_off = 16;
            int a = data[yi_off] & 0xFF;
            int b = data[yi_off+1] & 0xFF;
            int c = data[yi_off+2] & 0xFF;
            int d = data[yi_off+3] & 0xFF;
            System.out.println("Offered IP: " + a+"."+b+"."+c+"."+d);
        } catch (SocketTimeoutException ste) {
            System.out.println("No OFFER received (timeout).");
        }
        sock.close();
    }

    static byte[] buildDiscover(int xid, byte[] mac) throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(bout);
        out.writeByte(1); // op = request
        out.writeByte(1); // htype = ethernet
        out.writeByte(mac.length); // hlen
        out.writeByte(0); // hops
        out.writeInt(xid);
        out.writeShort(0); out.writeShort(0); // secs, flags
        out.write(new byte[4]); // ciaddr
        out.write(new byte[4]); // yiaddr
        out.write(new byte[4]); // siaddr
        out.write(new byte[4]); // giaddr
        out.write(mac); // chaddr (first hlen bytes)
        out.write(new byte[16 - mac.length]); // rest of chaddr
        out.write(new byte[64]); // sname
        out.write(new byte[128]); // file
        // magic cookie
        out.write(new byte[]{(byte)0x63,(byte)0x82,(byte)0x53,(byte)0x63});
        // options
        // DHCP Message Type 53 = 1 (DISCOVER)
        out.writeByte(53); out.writeByte(1); out.writeByte(1);
        // End
        out.writeByte(255);
        return bout.toByteArray();
    }
}
