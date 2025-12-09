#!/usr/bin/env ruby

MAGIC_TABLE = {
  "\xFF\xD8\xFF".b       => "JPEG image",
  "\x89PNG\r\n\x1A\n".b  => "PNG image",
  "GIF87a".b             => "GIF image",
  "GIF89a".b             => "GIF image",
  "PK\x03\x04".b         => "ZIP container (DOCX/XLSX/PPTX/etc.)",
  "%PDF-".b              => "PDF document",
  "\x1F\x8B".b           => "GZIP archive",
  "BM".b                 => "BMP image"
}

def identify_by_magic(path, max_len = 16)
  bytes = File.binread(path, max_len).b
  MAGIC_TABLE.each { |magic, type| return type if bytes.start_with?(magic) }
  "unknown"
end

if ARGV.empty?
  puts "usage: ruby fiden_1.rb <file>"
  exit 1
end

file = ARGV[0]

begin
  puts "#{file}: #{identify_by_magic(file)}"
rescue Errno::ENOENT
  puts "file not found: #{file}"
end
