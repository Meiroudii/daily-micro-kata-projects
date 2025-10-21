
def cubes(n)
  (1..n).map { |i| i**3 }
end

p cubes(5)  # => [1, 8, 27, 64, 125]
