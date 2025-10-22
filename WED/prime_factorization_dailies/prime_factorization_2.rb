define_method(:prime_facts) do |n|
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

p prime_facts(84)
