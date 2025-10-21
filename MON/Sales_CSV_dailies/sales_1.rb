require 'csv'

def top_n_sellers(file, n=20)
  tally = Hash.new(0)
  CSV.read(file, headers: true).each do |row|
    tally[row['product']] += row['units_sold'].to_i
  end

  # Sort descending and take top n
  tally.sort_by { |_, v| -v }.first(n)
end

def print_table(rows)
  # find column widths
  name_width = [rows.map { |r| r[0].length }.max, "Product".length].max
  units_width = [rows.map { |r| r[1].to_s.length }.max, "Units Sold".length].max

  # print header
  puts "+-#{'-'*name_width}-+-#{'-'*units_width}-+"
  puts "| #{'Product'.ljust(name_width)} | #{'Units Sold'.rjust(units_width)} |"
  puts "+-#{'-'*name_width}-+-#{'-'*units_width}-+"

  # print rows
  rows.each do |name, units|
    puts "| #{name.ljust(name_width)} | #{units.to_s.rjust(units_width)} |"
  end

  # footer
  puts "+-#{'-'*name_width}-+-#{'-'*units_width}-+"
end

rows = top_n_sellers("sales.csv", 20)
print_table(rows)
