
def lucas(n)
  return n if n < 2
  lucas(n-1) + lucas(n-2)
end

puts lucas(10)  # => 123
