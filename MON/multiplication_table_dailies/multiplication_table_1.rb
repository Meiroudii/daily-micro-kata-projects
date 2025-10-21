def multiplication_table(n)
  (1..n).map { |i| (1..n).map { |j| i*j } }
end

p multiplication_table(5)

(1..5).map do |i|
      (1..5).map do |j|
            print "#{i*j}\t"
          end
      puts ""
    end
