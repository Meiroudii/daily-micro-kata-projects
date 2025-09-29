## Can I use postman? 
NO, You're going to use cURL.

## CHEATSHEETS, memorize these

```ruby
curl http://localhost:4567/posts
Get Single Post by ID (GET /posts/:id)
curl http://localhost:4567/posts/1
Create a New Post (POST /posts)
curl -X POST http://localhost:4567/posts -d '{"title": "New Post", "body": "This is a new post"}' -H "Content-Type: application/json"
Update an Existing Post (PUT /posts/:id)
curl -X PUT http://localhost:4567/posts/1 -d '{"title": "Updated Post", "body": "Updated content"}' -H "Content-Type: application/json"
Delete a Post (DELETE /posts/:id)
curl -X DELETE http://localhost:4567/posts/1
```
