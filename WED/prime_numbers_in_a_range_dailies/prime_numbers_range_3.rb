define_method(:prime_n) do |x,y|
  (x..y).select { |n| n > 1 && (2..Math.sqrt(n).to_i).none? {|i| n % 1 == 0 } }
end
p prime_n(3,42342)
