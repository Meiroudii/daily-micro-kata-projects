def f(from)
  5.times do |i|
    puts "#{from}: #{i}"
  end
end

f("direct")

t0 = Thread.new { f("What you ") }
t1 = Thread.new { f("afar") }
t2 = Thread.new { f("doing") }
t3 = Thread.new { f("below") }
t4 = Thread.new { f("right") }
t5 = Thread.new { f("skil") }
t6 = Thread.new { f("right") }
t7 = Thread.new { f("issues") }
t8 = Thread.new { f("right") }
t9 = Thread.new { f("right?") }

[t1,t2,t3, t4, t5, t6, t7 ,t8 ,t9,t0].each(&:join)
puts "done"
