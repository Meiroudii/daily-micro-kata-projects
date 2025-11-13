def factorial(n)
  (1..n).reduce(1, :*) || 1
end

def factorial_table(n)
  puts "\t\t______________________"
  puts "\t\t| Number | Factorial |"
  puts "\t\t|--------+-----------|"
  (0..n).each do |i|
  puts "\t\t|%6d  |%9d  |" % [i, factorial(i)]
  end
  puts "\t\t|________|___________|"
end
factorial_table(10)
