def pa?(word)
  check = word.to_s
  check == check.reverse
end

print "Test: "
w = gets.chomp
puts pa?(w)
