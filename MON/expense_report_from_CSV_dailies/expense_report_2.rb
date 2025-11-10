require "csv"

def category_summary(file)
  data = CSV.read(file, headers: true)
  summary = Hash.new(0.0)

  data.each do |row|
    summary[row["category"]] += row["amount"].to_f
  end
  summary
end

p category_summary("expense.csv")
