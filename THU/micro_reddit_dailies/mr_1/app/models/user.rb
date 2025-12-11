class User < ApplicationRecord
  has_many :comments
  has_many :posts
  validates :username, presence: true, uniqueness: { case_sensitive: false}, length: { in: 1..20 }
end
