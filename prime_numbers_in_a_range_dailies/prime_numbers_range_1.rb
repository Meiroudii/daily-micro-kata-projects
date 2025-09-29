
def primes_in_range(a, b)
  (a..b).select { |n| n > 1 && (2..Math.sqrt(n).to_i).none? { |i| n % i == 0 } }
end

p primes_in_range(10, 30)  # => [11, 13, 17, 19, 23, 29]
