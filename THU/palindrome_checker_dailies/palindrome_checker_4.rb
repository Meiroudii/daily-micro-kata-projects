def p?(w)
  c = w.to_s
  c == c.reverse
end

print "Ask: "
w = gets.chomp
puts p?(w)
