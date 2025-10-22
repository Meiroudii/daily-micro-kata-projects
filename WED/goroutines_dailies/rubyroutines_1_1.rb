def f(from)
  3.times do |i|
    puts "#{from} : #{i}"
  end
end

f("direct")

thread_1 = Thread.new { f("rubyroutine") }
thread_2 = Thread.new { puts "going" }
[thread_1, thread_2].each(&:join)

puts "done"
