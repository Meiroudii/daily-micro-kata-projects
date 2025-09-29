def harmonic_sum(n)
  (1..n).map { |i| 1.0 / i }.sum
end

puts harmonic_sum(5)  # => 2.283333333333333
