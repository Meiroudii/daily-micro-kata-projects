
def sum_divisors(n)
  (1..n).select { |i| n % i == 0 }.sum
end

puts sum_divisors(12)  # => 28
