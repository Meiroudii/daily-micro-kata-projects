function palindrome(word::AbstractString)
  check = string(word)
  return check == reverse(check)
end

print("Palindrome\n\t\tType q to quit\n\n")
while true
  print("|-\t\t>>> ")
  res = readline(stdin)

  if res == "q"
    break
  end

  println("|-\t-\t-\t- $res is $(palindrome(res) ? "a palindrome" : "not a palindrome")")
end
