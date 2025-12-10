#!/usr/bin/env ruby
require 'socket'
require 'timeout'

# Define the ports to scan
PORTS = [22, 80, 443, 8080, 3306]

# Scan a single port and try to grab a banner
def scan_port(target_ip, port)
  begin
    # Set a timeout for the connection attempt (3 seconds)
    Timeout.timeout(3) do
      socket = TCPSocket.new(target_ip, port)
      socket.setsockopt(Socket::SOL_SOCKET, Socket::SO_RCVBUF, 0)  # Disable buffer to get immediate response
      socket.set_read_timeout(1)  # Timeout for read operation (1 second)

      # Attempt to read up to 256 bytes of data from the port (banner grabbing)
      banner = socket.recv(256).strip
      socket.close

      return banner.empty? ? nil : banner
    end
  rescue Timeout::Error
    return nil  # Connection timed out
  rescue StandardError => e
    return nil  # Any other error (e.g., connection refused)
  end
end

# Main function that runs the port scan
def run(target_ip)
  puts "Scanning #{target_ip}..."

  PORTS.each do |port|
    banner = scan_port(target_ip, port)
    
    if banner
      puts "Port #{port} is open."
      puts "  Banner: #{banner}"
    else
      puts "Port #{port} is closed or filtered."
    end
  end
end

# Get the target IP from command-line arguments
if ARGV.length < 1
  puts "Usage: ruby #{__FILE__} <target_ip>"
  exit 1
end

target_ip = ARGV[0]
run(target_ip)
