def fibonacci_array(n)
  fib = [0, 1]
  (2...n).each { |i| fib << fib[i-1] + fib[i-2] }
  fib[0...n]
end

p fibonacci_array(10)  # => [0, 1, 1, 2, 3, 5, 8, 13, 21, 34]
