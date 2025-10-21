def convert(amount, rate)
  (amount * rate).round(2)
end

puts convert(100, 1.2)  # => 120.0 USD → EUR example
