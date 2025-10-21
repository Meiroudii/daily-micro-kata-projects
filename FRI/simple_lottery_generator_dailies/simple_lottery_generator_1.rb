def lottery_numbers(n=6, max=49)
  (1..max).to_a.sample(n).sort
end

p lottery_numbers()  # => [3, 7, 12, 28, 33, 45]
