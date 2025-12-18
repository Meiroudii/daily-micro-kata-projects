class BlogsController < ApplicationController
  before_action :set_blog, only: %i[ show edit update destroy]
  def index
    @blogs = Blog.all
  end
  def show
  end
  def new
    @blog = Blog.new
  end
  def create
    @blog = Blog.new(blog_params)
    if @blog.save
      redirect_to @blog
    else
      render :new, status: :unprocessable_entity
    end
  end
  def update
    if @blog.update(blog_params)
      redirect_to @blog
    else
      render :edit, status: :unprocessable_entity
    end
  end
  def edit
  end
  def destroy
    @blog.destroy
    redirect_to root_path, status: :see_other
  end
  private
  def set_blog
    @blog = Blog.find(params[:id])
  end
  def blog_params
    params.expect(blog: [ :title, :content, :stock, :version ])
  end
end
