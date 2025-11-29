def recursive_binary_search(l, t)
  return false unless l.any?

  mid = l.length / 2

  if l[mid] == t
    true
  elsif l[mid] >= t
    recursive_binary_search(l[0...mid], t)
  else
    recursive_binary_search(l[(mid + 1..-1)], t)
  end
end
