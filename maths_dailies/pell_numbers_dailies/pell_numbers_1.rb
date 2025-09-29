
def pell(n)
  return n if n < 2
  2 * pell(n-1) + pell(n-2)
end

puts pell(10)  # => 2378
