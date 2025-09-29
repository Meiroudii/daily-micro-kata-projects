def letter_frequency(text)
  text.chars.group_by(&:itself).transform_values(&:size)
end

p letter_frequency("hello world")  
# => {"h"=>1, "e"=>1, "l"=>3, "o"=>2, " "=>1, "w"=>1, "r"=>1, "d"=>1}
