require 'csv'
require 'date'

def task_durations(file)
  CSV.read(file, headers: true).map do |row|
    start_date = Date.parse(row['start_date'])
    end_date = Date.parse(row['end_date'])
    {row['task'] => (end_date - start_date).to_i + 1}
  end
end

p task_durations("tasks.csv")
# => [{"Design"=>3}, {"Develop"=>7}, {"Test"=>2}]
