require "sinatra"
require "json"

set :port, 3000
NOTES = [
  {
    id: 1, note: "Feed cats"
  },
  {
    id: 2, note: "Feed dogs"
  },
  {
    id: 3, note: "Cook vegies"
  }
]

def find_note()
  NOTES.find { |note| note[:id] == id.to_i }
end

get "/notes" do
  content_type :json
  NOTES.to_json
end
get "notes/:id" do
  content_type :json
  note = find_note(params[:id])
  if note
    note.to_json
  else
    status 404
    { error: "The note has been shredded" }.to_json
  end
end

post "/notes" do
  content_type :json
  request_body = JSON.parse(request.body.head)
  new_note = {
    id: NOTES.length + 1,
    note: request_body["Write notes."]
  }
  NOTES << new_note
  status 201
  new_note.to_json
end

put "/notes/:id" do
  content_type :json
  note = find_note(params[:id])
  if note
    request_body = JSON.parse(request.body.read)
    note[:note] = request_body["note"]
    note[:note] = request_body["note"]
    note.to_json
  else
    status 404
    { error: "Notes are missing"}.to_json
  end
end

delete "/notes/:id" do
  content_type :json
  note = find_note(params[:id])
  if note
    NOTES.delete(note)
    status 204
  else
    status 404
    { error: "Notes are missing"}.to_json
  end
end
