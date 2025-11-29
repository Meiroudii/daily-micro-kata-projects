define_method(:rbs) do |numbers, target|
  # return false if numbers.empty?
  return false unless numbers.any?

  m = numbers.length / 2
  if numbers[m] == target
    puts "target: #{target}\nFound in: #{numbers[m]}"
  else
    return rbs(numbers[0...m], target) if numbers[m] >= target

    rbs(numbers[(m + 1..-1)], target)

  end
end
