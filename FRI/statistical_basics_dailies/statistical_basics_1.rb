def mean(arr)
  arr.sum.to_f / arr.size
end

def median(arr)
  s = arr.sort
  len = s.size
  len.odd? ? s[len/2] : (s[len/2 - 1] + s[len/2]) / 2.0
end

def mode(arr)
  arr.group_by{ |x| x }.max_by{ |k,v| v.size }[0]
end

numbers = [1,2,2,3,4,4,4,5]
puts mean(numbers)    # => 3.125
puts median(numbers)  # => 3.5
puts mode(numbers)    # => 4
