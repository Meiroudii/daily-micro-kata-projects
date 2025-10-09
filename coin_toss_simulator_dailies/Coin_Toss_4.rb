define_method(:toss) do |times=1|
  Array.new(times) {
    rand < 0.5 ? "Heads" : "Tails"
  }
end

def tui()
  cmd = gets.chomp
end

p toss(3)
