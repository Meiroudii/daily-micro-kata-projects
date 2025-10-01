define_method(:palindrome?) do |word|
  check = word.to_s
  check == check.reverse
end

print "Palindrome: "
w = gets.chomp
puts palindrome?(w)
