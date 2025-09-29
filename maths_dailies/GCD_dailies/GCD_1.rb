define_method(:gcd) {|a,b| b == 0 ? a : gcd(b, a % b) }


puts gcd(56, 98)  # => 14
