define_method(:bool_rbs) do |u64_vector_list, i32_target|
  return false unless u64_vector_list.any?

  u64_midpoint = u64_vector_list.length / 2
  return true if u64_vector_list[u64_midpoint] == i32_target

  if u64_vector_list[u64_midpoint] >= i32_target
    bool_rbs(u64_vector_list[0...u64_midpoint], i32_target)
  else
    bool_rbs(u64_vector_list[(u64_midpoint + 1..-1)], i32_target)
  end
end
