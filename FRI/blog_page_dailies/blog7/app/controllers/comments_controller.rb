class CommentsController < ApplicationController
  before_action :set_post, only: %i[ new create]

  def new
    @comment = @post.comments.new
  end

  def create
    @comment = @room.comments.create!(comment_params)
    respond_to do |format|
      format.html { redirect_to @post }
    end
  end

  private
  def set_post
    @post = Post.find(params.expect(:post_id))
  end
  def comment_params
    params.expect(comment: [ :content ])
  end
end
