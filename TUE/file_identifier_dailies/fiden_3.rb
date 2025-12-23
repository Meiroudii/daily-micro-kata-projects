#!/usr/bin/env ruby

MAGICS = [
  ["\xFF\xD8\xFF".b, "This is a JPEG"],
  ["\x7FELF".b, "This is an ELF executable"]
].freeze

def read_bytes(path, n = 65_536)
  File.open(path, "rb") { |f| f.read(n) || "".b }.force_encoding("ASCII-8BIT")
end

def printable_text?(bytes)
  return false if bytes.empty?
  printable = bytes.bytes.count { |b| (b >= 0x20 && b <= 0x7E) || b == 0x09 || b == 0x0A || b == 0x0D }
  ratio = printable.to_f / bytes.bytesize
  begin
    bytes.force_encoding("UTF-8").valid_encoding? && ratio > 0.9
  rescue
    false
  end
end

def identify(path)
  bytes = read_bytes(path)
  MAGICS.each do |magic, desc|
    return desc if bytes.start_with?(magic)
  end
  return "text file" if printable_text?(bytes)
  "unknown"
end

if ARGV.empty?
  STDERR.puts "usage: ruby fiden_3.rb <file>"
  exit 1
end

file = File.expand_path(ARGV[0])
unless File.file?(file)
  STDERR.puts "file not found: #{file}"
  exit 2
end
puts "#{file}: #{identify(file)}"
