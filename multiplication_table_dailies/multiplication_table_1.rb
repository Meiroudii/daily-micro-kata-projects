
def multiplication_table(n)
  (1..n).map { |i| (1..n).map { |j| i*j } }
end

p multiplication_table(5)  
# => [[1,2,3,4,5], [2,4,6,8,10], ...]
