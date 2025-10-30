def pf(n)
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

p pf(9439453)
