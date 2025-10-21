
def lcm(a, b)
  (a * b) / gcd(a, b)
end

puts lcm(21, 6)  # => 42
