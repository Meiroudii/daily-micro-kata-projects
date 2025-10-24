require "gosu"
require "socket"
require "thread"

class ChatWindow < Gosu::Window
  def initialize
    super 600, 500
    self.caption = "Ruby Forum
    @font = Gosu::Font.new(20)"
    @messages = []
    @input = ""
    @socket = TCPSocket.new("localhost", 3000)
    @receiver = Thread.new do
      while (msg = @socket.gets)
        @messages << msg.strip
      end
    end
  end

  def update
  end

  def draw
    Gosu.draw_rect(0,0,600,500, Gosu::Color.rgb(0,0,0))
    y = 20
    @messages.last(20).each do |m|
      @font.draw_text(m, 20, y, 1,1,1, Gosu::Color::GREEN)
      y += 25
    end
    Gosu.draw_rect(0, 460, 600, 40, Gosu::Color.rgb(50,50,55))
    @font.draw_text(">> #{@input}", 10, 465, 1,1,1, Gosu::Color::GREEN)
  end

  def button_down(id)
    case id
    when Gosu::KB_RETURN
      @socket.puts @input
      @messages << "\tYou: #{@input}"
      @input.clear
    when Gosu::KB_BACKSPACE
      @input.chop!
    else
      char = Gosu.button_id_to_char(id)
      @input << char if char
    end
  end
end

ChatWindow.new.show
