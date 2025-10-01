function mult_table(n)
    for i in 1:n
        for j in 1:n
            print("| $(i*j)\t")
        end
        println()
    end
end

mult_table(10)
