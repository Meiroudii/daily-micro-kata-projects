define_method(:letter_frequency) {|text| text.chars.group_by(&:itself).transform_values(&:size)}

p letter_frequency("AAAAA")
