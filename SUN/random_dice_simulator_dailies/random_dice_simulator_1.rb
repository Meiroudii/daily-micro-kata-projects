def roll_dice(sides=6, times=1)
  Array.new(times) { rand(1..sides) }
end

p roll_dice(6, 5)  # => [3, 6, 1, 4, 2] example
