require "socket"

PORT = 3000
server = TCPServer.new(PORT)
clients = []

puts "Listening on port #{PORT}..."

loop do
  Thread.start(server.accept) do |client|
    clients << client
    client.puts "Ruby Forum"
    name = client.gets&.chomp || "Anonymous"
    puts "\t[+] #{name} joined the server."

    loop do
      msg = client.gets
      break if msg.nil?
      msg = msg.chomp
      puts "\t\t\t#{name}: #{msg}"
      clients.each do |c|
        next if c == client
        c.puts "\t\t\t#{name}: #{msg}"
      end
    end

    puts "[-] #{name} left the server"
    clients.delete(client)
    client.close
  end
end
