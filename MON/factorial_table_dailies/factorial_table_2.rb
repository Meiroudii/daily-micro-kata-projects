def factorial(n)
  (1..n).reduce(1, :*) || 1
end

def factorial_table(n)
  puts "Number | Factorial"
  puts "-------+----------"
  (0..n).each do |i|
    puts "%6d | %9d" % [i, factorial(i)]
  end
end

factorial_table(5)
