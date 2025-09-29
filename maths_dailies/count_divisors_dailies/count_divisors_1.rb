def count_divisors(n)
  (1..n).count { |i| n % i == 0 }
end

puts count_divisors(12)  # => 6
