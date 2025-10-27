def mult_table(n)
  (1..n).map do |i|
    (1..n).map do |j|
      print("| #{i*j}\t")
    end
    puts ""
  end
end

mult_table(20)
