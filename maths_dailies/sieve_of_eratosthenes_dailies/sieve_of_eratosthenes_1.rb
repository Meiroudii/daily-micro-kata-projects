def sieve(n)
  primes = Array.new(n+1, true)
  primes[0] = primes[1] = false
  (2..Math.sqrt(n)).each do |i|
    next unless primes[i]
    (i*i..n).step(i) { |j| primes[j] = false }
  end
  primes.each_index.select { |i| primes[i] }
end

p sieve(30)  # => [2, 3, 5, 7, 11, 13, 17, 19, 23, 29]
