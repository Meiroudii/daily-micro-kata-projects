define_method(:factorial) { |n| (1..n).inject(1, :*) }

def catalan(n)
  (0...n).map do |i|
    factorial(2*i) / (factorial(i+1) * factorial(i))
  end
end

p catalan(5)  # => [1, 1, 2, 5, 14]
