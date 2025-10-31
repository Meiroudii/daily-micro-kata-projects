def lottery_number(n=6, max=49)
  (1..max).to_a.sample(n).sort
end

p lottery_number()
