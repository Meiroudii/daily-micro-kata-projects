
require 'securerandom'

def random_password(length=12)
  SecureRandom.alphanumeric(length)
end

puts random_password(16)  # => "G7hJ9k2LmP4qW1xZ"
