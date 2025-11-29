require 'gosu'
require 'socket'
require 'time'

class LogWindow < Gosu::Window
  def initialize
    super 800, 600
    self.caption = "HTTP Logs (Gosu)"

    @font = Gosu::Font.new(24)
    @lines = []
    @mutex = Mutex.new

    start_server_thread
  end

  def start_server_thread
    Thread.new do
      server = TCPServer.new(3000)
      log("Server started on port 3000...")

      html = <<~HTML
        <p style="font-size: 2rem;">WORKING?</p>
      HTML

      loop do
        client = server.accept

        # Build response
        now = Time.now
        response = "HTTP/1.1 200 OK\r\n" \
                   "Content-Type: text/html\r\n\r\n" \
                   "#{now}<br>#{html}"

        client.write(response)

        # Log headers
        while (line = client.gets)
          line = line.strip
          break if line.empty?
          log(line)
          puts line
        end

        client.close
      end
    end
  end

  def log(str)
    @mutex.synchronize do
      @lines << str
      @lines.shift if @lines.size > 500
    end
  end

  def draw
    @mutex.synchronize do
      @lines.last(20).each_with_index do |line, i|
        @font.draw_text(line, 20, 20 + i * 26, 1, 1.0, 1.0, Gosu::Color::RED)
      end
    end
  end
end

LogWindow.new.show
