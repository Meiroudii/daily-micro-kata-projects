require 'open-uri'
require 'nokogiri'

url = "https://google.com"
html = URI.open(url)
doc = Nokogiri::HTML(html)

doc.css("h1, h2, h3, title").each do |node|
  puts node.text.strip
end
