# chat_client.rb
require 'socket'
require 'thread'

print "Server IP [default: localhost]: "
host = gets.chomp
host = 'localhost' if host.empty?

socket = TCPSocket.new(host, 3000)
puts "Connected to #{host}:3000"

print "Enter your name: "
name = gets.chomp
socket.puts(name)

receiver = Thread.new do
  while (msg = socket.gets)
    puts "\r#{msg}\n> "
  end
end

print "> "
while (line = STDIN.gets)
  socket.puts(line.chomp)
  print "> "
end
