class User < ApplicationRecord
  has_many :comments
  has_many :posts
  validates :username, presence: true
end

