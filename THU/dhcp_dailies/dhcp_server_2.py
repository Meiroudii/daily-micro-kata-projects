#!/usr/bin/env python3
# dhcp_discover_py.py
# Minimal DHCPDISCOVER using UDP sockets.
# Sends DISCOVER to server_ip:server_port with UDP source port 6868 and listens on 6868 for OFFER.

import socket, struct, random, time

SERVER_IP = "127.0.0.1"
SERVER_PORT = 6767
LOCAL_SRC_PORT = 6868
TIMEOUT = 5

def build_discover(xid, mac_bytes):
    pkt = bytearray()
    # BOOTP header (op,htype,hlen,hops)
    pkt += struct.pack('!BBBB', 1, 1, len(mac_bytes), 0)
    pkt += struct.pack('!I', xid)         # xid
    pkt += struct.pack('!HH', 0, 0)       # secs, flags
    pkt += b'\x00'*4                      # ciaddr
    pkt += b'\x00'*4                      # yiaddr
    pkt += b'\x00'*4                      # siaddr
    pkt += b'\x00'*4                      # giaddr
    # chaddr (16 bytes, pad)
    ch = mac_bytes + b'\x00'*(16 - len(mac_bytes))
    pkt += ch
    # sname (64) + file (128)
    pkt += b'\x00'*(64+128)
    # magic cookie
    pkt += struct.pack('!I', 0x63825363)
    # DHCP options: message type = 1 (discover)
    pkt += struct.pack('!BBB', 53, 1, 1)
    # end
    pkt += struct.pack('!B', 255)
    return pkt

def read_yiaddr(data):
    if len(data) >= 20:
        a,b,c,d = data[16], data[17], data[18], data[19]
        return f"{a & 0xFF}.{b & 0xFF}.{c & 0xFF}.{d & 0xFF}"
    return None

def main():
    xid = random.randint(0, 0xffffffff)
    mac = bytes([0x02,0x00,0x00,0x00,0x00,0x01])  # fake MAC
    pkt = build_discover(xid, mac)

    # UDP socket bound to source port LOCAL_SRC_PORT to receive reply there
    s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    s.settimeout(TIMEOUT)
    s.bind(("0.0.0.0", LOCAL_SRC_PORT))

    print(f"[>] DISCOVER sending xid=0x{xid:08x} from src-port={LOCAL_SRC_PORT} -> {SERVER_IP}:{SERVER_PORT}")
    s.sendto(pkt, (SERVER_IP, SERVER_PORT))

    try:
        data, addr = s.recvfrom(2048)
        print(f"[<] Packet from {addr[0]}:{addr[1]} len={len(data)}")
        yi = read_yiaddr(data)
        if yi:
            print(f"[<] OFFERED IP: {yi}")
        else:
            print("[x] Received packet, but couldn't parse yiaddr.")
    except socket.timeout:
        print("[x] Timeout: no OFFER received.")
    finally:
        s.close()

if __name__ == "__main__":
    main()
