require 'securerandom'

def random_password(length=22)
  SecureRandom.alphanumeric(length)
end

define_method(:generate_passwd) {|length=22| SecureRandom.alphanumeric(length)}

puts random_password(22)
puts "password generated: #{generate_passwd}"
