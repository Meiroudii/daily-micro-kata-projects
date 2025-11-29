class Binary_Search
  define_method(:recursive_binary_search) do |list, target|
    return false if list.empty?

    mid = list.length / 2
    if list[mid] == target
      true
    elsif list[mid] >= target
      recursive_binary_search(list[0...mid], target)
    else
      recursive_binary_search(list[(mid + 1)..-1], target)
    end
  end
end

searcher = Binary_Search.new
numbers = (1..999).to_a
puts searcher.recursive_binary_search(numbers, 66)
puts searcher.recursive_binary_search(numbers, 9909)
