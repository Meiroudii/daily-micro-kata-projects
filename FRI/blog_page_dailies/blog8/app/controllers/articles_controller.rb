class ArticlesController < ApplicationController
  before_action :set_article, only: %i[ show edit update destroy ]
  def index
    @articles = Article.all
  end
  def show
  end
  def new
    @article = Article.new
  end
  def create
    @article = Article.new(article_params)
    if @article.save
      return redirect_to @article
    else
      return render :new, status: :unprocessable_entity
    end
  end
  def update
    respond_to do |format|
      if @article.update(article_params)
        format.html { redirect_to @article, status: :see_other}
      else
        format.html { render :edit, status: :unprocessable_entity }
      end
    end
  end
  def destroy
    @article.destroy!
    return redirect_to articles_path, status: :see_other
  end

  private
  def set_article
    @article = Article.find(params.expect(:id))
  end
  def article_params
    params.expect(article: [ :author, :content ])
  end
end
