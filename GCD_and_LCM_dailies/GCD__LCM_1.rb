
def gcd(a, b)
  b == 0 ? a : gcd(b, a % b)
end

def lcm(a, b)
  (a * b) / gcd(a, b)
end

puts gcd(48, 180)   # => 12
puts lcm(48, 180)   # => 720
