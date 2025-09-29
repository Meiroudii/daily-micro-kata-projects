
def geometric_sum(a1, r, n)
  return a1 if r == 1
  a1 * (1 - r**n) / (1 - r)
end

puts geometric_sum(1, 2, 5)  # => 31 (1+2+4+8+16)
