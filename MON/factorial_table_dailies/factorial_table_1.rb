def factorial_table(n)
  (0..n).map { |i| [i, factorial(i)] }.to_h
end

p factorial_table(5)  
# => {0=>1, 1=>1, 2=>2, 3=>6, 4=>24, 5=>120}
