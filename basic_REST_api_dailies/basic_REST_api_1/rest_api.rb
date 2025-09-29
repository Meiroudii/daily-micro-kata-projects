require 'sinatra'
require 'json'

set :port, 3000

# In-memory database (just for demonstration)
POSTS = [
  { id: 1, title: 'First Post', body: 'This is the body of the first post' },
  { id: 2, title: 'Second Post', body: 'This is the body of the second post' }
]

# Helper to find a post by ID
def find_post(id)
  POSTS.find { |post| post[:id] == id.to_i }
end

# GET /posts - Fetch all posts
get '/posts' do
  content_type :json
  POSTS.to_json
end

# GET /posts/:id - Fetch a single post by ID
get '/posts/:id' do
  content_type :json
  post = find_post(params[:id])
  if post
    post.to_json
  else
    status 404
    { error: 'Post not found' }.to_json
  end
end

# POST /posts - Create a new post
post '/posts' do
  content_type :json
  request_body = JSON.parse(request.body.read)
  new_post = {
    id: POSTS.length + 1,
    title: request_body['title'],
    body: request_body['body']
  }
  POSTS << new_post
  status 201
  new_post.to_json
end

# PUT /posts/:id - Update an existing post
put '/posts/:id' do
  content_type :json
  post = find_post(params[:id])
  if post
    request_body = JSON.parse(request.body.read)
    post[:title] = request_body['title']
    post[:body] = request_body['body']
    post.to_json
  else
    status 404
    { error: 'Post not found' }.to_json
  end
end

# DELETE /posts/:id - Delete a post by ID
delete '/posts/:id' do
  content_type :json
  post = find_post(params[:id])
  if post
    POSTS.delete(post)
    status 204
  else
    status 404
    { error: 'Post not found' }.to_json
  end
end
