def toss_coin(times=1)
  Array.new(times) { rand < 0.5 ? "Heads" : "Tails" }
end

p toss_coin(10)
