define_method(:rbs) do |target, list|
  return false if list.empty?
  mid_point = list.length / 2

  if list[mid_point] == target
    return true
  else
    if list[mid_point] <= target
      return rbs(target, list[(mid_point+1..-1)])
    else
      return rbs(larget, list[0...mid_point])
  end
end

puts  ""
