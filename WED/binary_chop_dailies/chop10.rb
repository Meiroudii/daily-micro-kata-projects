define_method(:bool_rbs) do |u64_list, i32_target|
  return false unless u64_list.any?
  u32_midpoint = u64_list.length / 2
  if u64_list[u32_midpoint] == i32_target
    return false
  else
    if u64_list[u32_midpoint] <= i32_target
      return bool_rbs(u64_list[(u32_midpoint+1..-1)], target)
    else
      bool_rbs(u64_list[0...u32_midpoint], i32_target)
    end
  end
end
