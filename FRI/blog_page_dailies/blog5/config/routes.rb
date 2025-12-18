Rails.application.routes.draw do
  # NOTE Add routes for blog pages

  b="blogs"
  root "#{b}#index"

  get "/#{b}", to: "#{b}#index", as: "#{b}"
  get "/#{b}/new", to: "#{b}#new", as: "new_#{b[0..-2]}"
  post "/#{b}", to: "#{b}#create"
  get "/#{b}/:id", to: "#{b}#show", as: "#{b[0..-2]}"
  get "/#{b}/:id/edit", to: "#{b}#edit", as: "edit_#{b[0..-2]}"
  patch "/#{b}/:id", to: "#{b}#update"
  delete "/#{b}/:id", to: "#{b}#destroy"


  # Reveal health status on /up that returns 200 if the app boots with no exceptions, otherwise 500.
  # Can be used by load balancers and uptime monitors to verify that the app is live.
  get "up" => "rails/health#show", as: :rails_health_check

  # Render dynamic PWA files from app/views/pwa/* (remember to link manifest in application.html.erb)
  # get "manifest" => "rails/pwa#manifest", as: :pwa_manifest
  # get "service-worker" => "rails/pwa#service_worker", as: :pwa_service_worker

  # Defines the root path route ("/")
  # root "posts#index"
end
