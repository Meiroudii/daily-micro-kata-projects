def binary_search(lists, target)
  return false unless lists.any?

  mid_point = lists.length / 2
  if lists[mid_point] == target
    return true
  else
    if lists[mid_point] >= target
      return binary_search(lists[0...mid_point], target)
    else
      return binary_search(lists[(mid_point+1..-1)], target)
    end
  end
end

