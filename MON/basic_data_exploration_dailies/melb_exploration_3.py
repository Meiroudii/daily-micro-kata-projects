import pandas as pd
from sklearn.tree import DecisionTreeRegressor as d

df = pd.read_csv("./melb_data.csv")

df.describe()
df.info()
df.columns
df = df.dropna(axis=0)

y = df.Price
mf = ["Rooms", "Bathroom", "Landsize", "Lattitude", "Longtitude"]
X = df[mf]
X.describe()
X.info()
X.head()

mm = regre(random_state=1)
mm.fit(X, y)

print("Predictions of 5 houses:")
print(X.head())
print("The predictdions are")
print(mm.predict(X.head()))
