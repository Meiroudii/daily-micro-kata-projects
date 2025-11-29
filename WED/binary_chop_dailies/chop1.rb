def chop(target, array, left = 0, right = array.length - 1)
  return -1 if left > right

  mid = (left + right) / 2

  if array[mid] == target
    mid
  elsif array[mid] < target
    chop(target, array, mid + 1, right)
  else
    chop(target, array, left, mid - 1)
  end
end

def verify(i32)
  i32 > 0 ? "Found 'em" : 'Mission Failed'
end

puts verify(chop(2, [1, 2, 3, 4, 5]))
puts verify(chop(100, [1, 2, 3, 4, 5]))
