class BlogsController < ApplicationController
  before_action :set_blog, only: %i[ show update edit destroy]
  before_action :blogs, onlly: %i[ index show ]
  def index
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
  def edit
  end
  def update
    if @blog.update(blog_params)
      redirect_to @blog
    else
      render :edit, status: :unprocessable_entity
    end
  end
  def destroy
    @blog.destroy
    redirect_to root_path, status: :see_other
  end

# app/controllers/blogs_controller.rb
def bulk_destroy
  # Filter out empty values if necessary and destroy
  Blog.where(id: params[:blog_ids]).destroy_all

  respond_to do |format|
    format.html { redirect_to blogs_path, notice: "Selected blogs were deleted." }
    format.turbo_stream { render turbo_stream: turbo_stream.action(:refresh, "") } # Optional: cleaner Turbo refresh
  end
end

  private
  def blog_params
    params.expect(blog: [ :title, :content ])
  end
  def set_blog
    @blog = Blog.find(params[:id])
  end
  def blogs
    @blogs = Blog.all
  end

end
