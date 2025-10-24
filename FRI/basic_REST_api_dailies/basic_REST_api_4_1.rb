require 'sinatra'
require 'json'

set :port, 3000

NOTES = [{id: 1, n: 'Feed cats'}, {id: 2, n: 'Feed dogs'}, {id: 3, n: 'Cook veggies'}]

def search(index); NOTES.find { |x| x[:id] == index.to_i }; end

before { content_type :json }

get('/notes') { NOTES.to_json }

get('/notes/:id') { (x = f(params[:id])) ? x.to_json : halt(404, { e: '404' }.to_json) }

post('/notes') { x = { id: NOTES.size + 1, n: JSON.parse(request.body.read)['n'] }; NOTES << x; status 201; x.to_json }

put('/notes/:id') { (x = f(params[:id])) ? (x[:n] = JSON.parse(request.body.read)['n']; x.to_json) : halt(404, { e: '404' }.to_json) }

delete('/notes/:id') { (x = f(params[:id])) ? (NOTES.delete(x); status 204) : halt(404, { e: '404' }.to_json) }
