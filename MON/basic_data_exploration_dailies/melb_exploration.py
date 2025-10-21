import pandas as pd
import numpy as np
from sklearn.tree import DecisionTreeRegressor

df = pd.read_csv("./melb_data.csv")

df.describe()
df.info()
df.columns
df = df.dropna(axis=0)

y = df.Price
melbourne_features = ["Rooms", "Bathroom", "Landsize", "Lattitude", "Longtitude"]
X = df[melbourne_features]
X.describe()
X.info()
X.head()

melbourne_model = DecisionTreeRegressor(random_state=1)

melbourne_model.fit(X, y)

print("Making predictions for the following 5 houses:")
print(X.head())
print("The predictions are")
print(melbourne_model.predict(X.head()))
