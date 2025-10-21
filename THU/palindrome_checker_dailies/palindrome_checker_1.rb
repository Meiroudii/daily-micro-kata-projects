
def palindrome?(x)
  s = x.to_s
  s == s.reverse
end

puts palindrome?("racecar")  # => true
puts palindrome?(12321)      # => true
puts palindrome?(12345)      # => false
