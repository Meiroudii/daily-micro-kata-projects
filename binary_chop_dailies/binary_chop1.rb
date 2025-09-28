def chop(target, array)
  left = 0
  right = array.length - 1
  while left <= right
    mid = (left + right) / 2
    guess = array[mid]
    if guess == target
      return mid
    elsif guess < target
      left = mid + 1
    else
      right = mid - 1
    end
  end
  -1
end

def verify(i32)
  i32 > 0 ? "Found!" : "Can't find em"
end

puts "First Experiment: target: 2"
puts chop(2, [1,3,4,5,6])
puts verify(chop(2, [1,3,4,5,6]))
puts "Second Experiment: target: 2"
puts verify(chop(2, [1,2,4,5,6]))
puts chop(2, [1,2,4,5,6])
