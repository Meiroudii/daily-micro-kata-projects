require "sinatra"
require "json"

set :port, 8080

ARTICLES = [
  {
    id: 1, title: "The cat and the dog", body: "The cat eats and dog eats"
  },
  {
    id: 2, title: "the dog", body: "The cat eats and The dog sleep"
  },
  {
    id: 3, title: "The cat", body: "The cat hunts and dog eats"
  },
  {
    id: 4, title: "The pets", body: "The cat eats and dog eats"
  },
]

define_method(:find_article) do |id|
  ARTICLES.find { |article| article[:id] == id.to_i }
end

get "/articles" do
  content_type :json
  ARTICLES.to_json
end
get "articles/:id" do
  content_type :json
  article = find_article(params[:id])
  if article
    article.to_json
  else
    status 404
    { error: "The article might burned" }.to_json
  end
end

post "/articles" do
  content_type :json
  request_body = JSON.parse(request.body.head)
  new_article = {
    id: ARTICLES.length + 1,
    title: request_body["Hey"],
    body: request_body["change this"]
  }
  ARTICLES << new_article
  status 201
  new_article.to_json
end

put "/articles/:id" do
  content_type :json
  article = find_article(params[:id])
  if article
    request_body = JSON.parse(request.body.read)
    article[:title] = request_body["title"]
    article[:body] = request_body["body"]
    article.to_json
  else
    status 404
    { error: "I don't see 'em"}.to_Json
  end
end

delete "/articles/:id" do
  content_type :json
  article = find_article(params[:id])
  if article
    ARTICLES.delete(article)
    status 204
  else
    status 404
    { error: "I don't see em"}.to_json
  end
end
