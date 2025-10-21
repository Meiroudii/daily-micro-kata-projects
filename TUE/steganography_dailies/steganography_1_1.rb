s = "steganograpHy is the practicE of conceaLing a file, message, image, or video within another fiLe, message, image, Or video."
cracked = ""
s.split("").each do |char|
  if char.match?(/[A-Z]/)
    print(char)
  end
end
