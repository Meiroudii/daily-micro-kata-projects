define_method(:fibonacci) do |n|
  return n if n < 2
  fibonacci(n - 1) + fibonacci(n - 2)
end

puts fibonacci(10)  # => 55
