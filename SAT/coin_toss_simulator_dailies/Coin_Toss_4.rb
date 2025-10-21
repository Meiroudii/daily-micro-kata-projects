def tui()
  current_money = 5000
  print "\t\t>>> "
  cmd = gets.chomp.split
  money = cmd[1].to_i
  coinflip = rand < 0.5 ? "heads" : "tails"
  if ["coinflip", "cf", "cointoss", "toss", "ct"].include?(cmd[0].downcase)
    if cmd[1].downcase == (rand < 0.5 ? "heads" : "tails") || cmd[1].downcase == (rand < 0.5 ? "h" : "t")
      puts "You won! you got #{money * 2}"
    else
      puts "You lose #{current_money - money}"
    end
  else
    puts "Command: #{cmd[0]} is not a command\ncoinflip help"
  end
end

def main()
  while true
    tui()
    puts "Continue? y/n\n\t>>> "
    if ["n", "no"].include?(gets.chomp.downcase)
      break
    end
  end
end

main()
