Rails.application.routes.draw do
  root "blogs#index"
  get "/blogs", to: "blogs#index", as: "blogs"
  get "/blogs/new", to: "blogs#new", as: "new_blog"
  post "/blogs", to: "blogs#create"
  get "/blogs/:id", to: "blogs#show", as: "blog"
  get "/blogs/:id/edit", to: "blogs#edit", as: "edit_blog"
  patch "/blogs/:id", to: "blogs#update"
  delete "/blogs/:id", to: "blogs#destroy"
  # config/routes.rb
  resources :blogs do
    collection do
      delete :bulk_destroy
    end
  end
  # Define your application routes per the DSL in https://guides.rubyonrails.org/routing.html

  # Reveal health status on /up that returns 200 if the app boots with no exceptions, otherwise 500.
  # Can be used by load balancers and uptime monitors to verify that the app is live.
  get "up" => "rails/health#show", as: :rails_health_check

  # Render dynamic PWA files from app/views/pwa/* (remember to link manifest in application.html.erb)
  # get "manifest" => "rails/pwa#manifest", as: :pwa_manifest
  # get "service-worker" => "rails/pwa#service_worker", as: :pwa_service_worker

  # Defines the root path route ("/")
  # root "posts#index"
end
