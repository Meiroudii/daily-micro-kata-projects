
def modpow(base, exp, mod)
  result = 1
  base %= mod
  while exp > 0
    result = (result * base) % mod if exp.odd?
    base = (base * base) % mod
    exp >>= 1
  end
  result
end

puts modpow(7, 256, 13)  # => 9
