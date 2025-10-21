require 'socket'

PORT = 3000
server = TCPServer.new(PORT)
clients = []

puts "Listening on port #{PORT}..."

loop do
  Thread.start(server.accept) do |client|
    clients << client
    client.puts "Welcome to Ruby Chat!"
    name = client.gets&.chomp || "Anon"
    puts "[+] #{name} connected"

    loop do
      msg = client.gets
      break if msg.nil?
      msg = msg.chomp
      puts "#{name}: #{msg}"
      clients.each do |c|
        next if c == client
        c.puts "#{name}: #{msg}"
      end
    end

    puts "[-] #{name} disconnected"
    clients.delete(client)
    client.close
  end
end
