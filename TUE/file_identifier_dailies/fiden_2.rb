#!/usr/bin/env ruby
# fiden_1.rb — binary-safe magic-number file identifier

MAGICS = [
  ["\xCA\xFE\xBA\xBE".b, "Java class file (CAFEBABE)"],
  ["\xFF\xD8\xFF".b,       "JPEG image"],
  ["\x89PNG\r\n\x1A\n".b,  "PNG image"],
  ["GIF87a".b,             "GIF image"],
  ["GIF89a".b,             "GIF image"],
  ["PK\x03\x04".b,         "ZIP container (DOCX/XLSX/PPTX/ZIP)"],
  ["%PDF-".b,              "PDF document"],
  ["\x1F\x8B".b,           "GZIP archive"],
  ["BM".b,                 "BMP image"],
  ["MZ".b,                 "Windows PE executable (MZ header)"],
  ["\x7FELF".b,           "ELF executable"],
  ["\xFE\xED\xFA\xCE".b,  "Mach-O (32-bit, big-endian)"],
  ["\xCE\xFA\xED\xFE".b,  "Mach-O (32-bit, little-endian)"],
  ["\xFE\xED\xFA\xCF".b,  "Mach-O (64-bit, big-endian)"],
  ["\xCF\xFA\xED\xFE".b,  "Mach-O (64-bit, little-endian)"]
].freeze

def read_bytes(path, n = 65_536)
  File.open(path, "rb") { |f| f.read(n) || "".b }.force_encoding("ASCII-8BIT")
end

def printable_text?(bytes)
  return false if bytes.empty?
  # consider text if > 95% bytes are printable or common whitespace, and valid UTF-8
  printable = bytes.bytes.count { |b| (b >= 0x20 && b <= 0x7E) || b == 0x09 || b == 0x0A || b == 0x0D }
  ratio = printable.to_f / bytes.bytesize
  begin
    bytes.force_encoding("UTF-8").valid_encoding? && ratio > 0.9
  rescue
    false
  end
end

def inspect_zip_for_ooxml(path, bytes)
  # fast heuristic: search for typical OOXML names in the first chunk
  chunk = bytes
  return "DOCX/XLSX/PPTX (OOXML inside ZIP)" if chunk.include?("[Content_Types].xml".b) || chunk.include?("word/".b) || chunk.include?("xl/".b) || chunk.include?("ppt/".b)
  "ZIP archive"
end

def identify(path)
  bytes = read_bytes(path)
  MAGICS.each do |magic, desc|
    return desc if bytes.start_with?(magic)
  end

  # additional check for ZIP-type container (DOCX vs ZIP)
  if bytes.start_with?("PK\x03\x04".b) || bytes.start_with?("PK\x05\x06".b) || bytes.start_with?("PK\x07\x08".b)
    return inspect_zip_for_ooxml(path, bytes)
  end

  return "text file" if printable_text?(bytes)
  "unknown"
end

if ARGV.empty?
  STDERR.puts "usage: ruby fiden_1.rb <file>"
  exit 1
end

file = File.expand_path(ARGV[0])
unless File.file?(file)
  STDERR.puts "file not found: #{file}"
  exit 2
end

puts "#{file}: #{identify(file)}"
