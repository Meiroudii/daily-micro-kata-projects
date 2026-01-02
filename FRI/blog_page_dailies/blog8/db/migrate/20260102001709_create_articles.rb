class CreateArticles < ActiveRecord::Migration[8.1]
  def change
    create_table :articles do |t|
      t.text :content
      t.string :author

      t.timestamps
    end
  end
end
