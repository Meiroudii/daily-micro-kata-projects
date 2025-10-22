def f(from)
  3.times do |i|
    puts "#{from}: #{i}"
  end
end

# Like: f("direct")
f("direct")

# Like: go f("goroutine")
t1 = Thread.new { f("rubyroutine") }

# Like: go func(msg string) { fmt.Println(msg) }("going")
t2 = Thread.new { puts "going" }

# Wait for threads to finish (instead of time.Sleep)
[t1, t2].each(&:join)

puts "done"
