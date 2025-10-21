def prime?(n)
  return false if n < 2
  (2..Math.sqrt(n).to_i).none? { |i| n % i == 0 }
end

puts prime?(17)  # => true
puts prime?(18)  # => false
