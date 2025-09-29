
def prime_factors(n)
  factors = []
  d = 2
  while n > 1
    while n % d == 0
      factors << d
      n /= d
    end
    d += 1
  end
  factors
end

p prime_factors(84)  # => [2, 2, 3, 7]
