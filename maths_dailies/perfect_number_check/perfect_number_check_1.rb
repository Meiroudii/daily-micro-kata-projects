def perfect?(n)
  sum_divisors(n) - n == n
end

puts perfect?(28)  # => true
puts perfect?(12)  # => false
