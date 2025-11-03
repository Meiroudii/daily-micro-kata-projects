def gcd(a,b)
  b == 0 ? a : gcd(b, a % b)
end
def lcm(a, b)
  (a * b) / gcd(a, b)
end

puts gcd(34,34)
puts lcm(34,34)
