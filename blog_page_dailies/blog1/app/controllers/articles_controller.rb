class ArticlesController < ApplicationController
  define_method(:index) { @articles = Article.all }
end
