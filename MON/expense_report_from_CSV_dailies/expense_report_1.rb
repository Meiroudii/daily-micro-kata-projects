require 'csv'

def category_summary(file)
  data = CSV.read(file, headers: true)
  summary = Hash.new(0.0)

  data.each do |row|
    summary[row['category']] += row['amount'].to_f
  end

  summary
end

p category_summary("expenses.csv")
# => {"coffee"=>3.5, "lunch"=>12.0, "books"=>25.0}
