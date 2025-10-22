def pir(a,b)
  (a..b).select { |n| n > 1 && (2..Math.sqrt(n).to_i).none? { |i| n % i == 0 } }
end

p pir(10, 30)
