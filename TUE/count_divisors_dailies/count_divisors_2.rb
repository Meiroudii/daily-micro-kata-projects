define_method(:cd) do |n|
  (1..n).count { |i| n % i == 0 }
end

puts cd(12)
