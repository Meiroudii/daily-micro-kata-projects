define_method(:recursive_bin) do |target, list|
  return false if list.empty?
  # 真中をつくっています
  mannaka = list.length / 2
  if target == list[mannaka]
    return true
  else
    if list[mannaka] <= target
      return rcursive_bin(target, list[(mannaka+1..-1)])
    else
      return recursive_bin(target, list[0...mannaka])
    end
  end
end
