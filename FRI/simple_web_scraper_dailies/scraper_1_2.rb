require 'open-uri'
require 'nokogiri'
require 'csv'

# Get URL from command line, or exit if missing
url = ARGV[0]
abort "Usage: ruby scraper.rb <url>" unless url

begin
  html = URI.open(url)
rescue => e
  abort "Failed to fetch #{url}: #{e.message}"
end

doc = Nokogiri::HTML(html)

headings = doc.css("h1, h2, h3").map { |h| h.text.strip }
links = doc.css("a[href]").map { |a| a['href'] }

CSV.open("scraped_data.csv", "w") do |csv|
  csv << ["Type", "Content"]
  headings.each { |h| csv << ["Heading", h] }
  links.each { |l| csv << ["Link", l] }
end

puts "Scraping done. Results saved to scraped_data.csv"
