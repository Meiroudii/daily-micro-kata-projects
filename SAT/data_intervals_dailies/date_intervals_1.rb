
require 'date'

def days_until(date_str)
  (Date.parse(date_str) - Date.today).to_i
end

puts days_until("2025-12-31")  # => number of days until new year
