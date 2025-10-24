require 'sinatra'
require 'json'

set :port, 3000
NOTES = [
  { id: 1, note: 'Feed cats' },
  { id: 2, note: 'Feed dogs' },
  { id: 3, note: 'Cook veggies' }
]

# Helper function to find a note by ID
def find_note(id)
  NOTES.find { |note| note[:id] == id.to_i }
end

# Routes

get '/notes' do
  content_type :json
  NOTES.to_json
end

get '/notes/:id' do
  content_type :json
  note = find_note(params[:id])
  note ? note.to_json : halt(404, { error: 'The note has been shredded' }.to_json)
end

post '/notes' do
  content_type :json
  new_note = { id: NOTES.size + 1, note: JSON.parse(request.body.read)['note'] }
  NOTES << new_note
  status 201
  new_note.to_json
end

put '/notes/:id' do
  content_type :json
  note = find_note(params[:id])
  if note
    note[:note] = JSON.parse(request.body.read)['note']
    note.to_json
  else
    halt 404, { error: 'Notes are missing' }.to_json
  end
end

delete '/notes/:id' do
  content_type :json
  note = find_note(params[:id])
  if note
    NOTES.delete(note)
    status 204
  else
    halt 404, { error: 'Notes are missing' }.to_json
  end
end
