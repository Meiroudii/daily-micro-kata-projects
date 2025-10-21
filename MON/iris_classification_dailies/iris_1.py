import pandas as pd
import numpy as np
import seaborn as sns
from matplotlib import pyplot as plt
from sklearn.datasets import load_iris

iris = load_iris()
df = pd.DataFrame(iris.data, columns=iris.feature_names)
pd.DataFrame(iris.data, columns=iris.feature_names)

df['species'] = pd.Categorical.from_codes(iris.target, iris.target_names)
df.columns = [col.replace(' (cm)', '').replace(' ', '_') for col in df.columns] # big brain regex string manipulation remove cm
df.describe(include="all")
df.isnull().sum()
print(f"Duplicates: {df.duplicated().sum()}")
for column in df.columns[:-1]:
  sns.histplot(data=df, x=column, hue="species", kde=True)
  plt.title(f"Distribution of {column} by Species")
  plt.show()

for column in df.columns[:-1]:
  sns.histplot(data=df, x=column, hue="species")
  plt.title(f"Distribution of {column} by Species")
  plt.show()

sns.pairplot(df, hue="species")

numeric_df = df.drop("species", axis=1)

plt.colormaps()
sns.heatmap(numeric_df.corr(), annot=True, cmap="coolwarm")
plt.title("Feature Correlation Matrix")
plt.show()

sns.heatmap(numeric_df.corr(), annot=True, cmap="Greens")
plt.title("Feature Correlation Matrix")
plt.show()


sns.heatmap(numeric_df.corr(), annot=True, cmap="viridis")
plt.title("Feature Correlation Matrix")
plt.show()

for column in df.columns[:-1]:
  sns.boxplot(data=df, x="species", y=column)
  plt.title(f"{column} by species")
  plt.show()

from sklearn.model_selection import train_test_split
X = df.drop("species", axis=1)
y = df["species"]

X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)
train_test_split(X, y, test_size=0.2, random_state=42)

from sklearn.tree import DecisionTreeClassifier
from sklearn.metrics import classification_report, confusion_matrix

model = DecisionTreeClassifier(random_state=43)
model.fit(X_train, y_train)

y_pred = model.predict(X_test)

print(confusion_matrix(y_test, y_pred))

print(classification_report(y_test, y_pred))

from sklearn.tree import plot_tree

plt.figure()
plot_tree(model, feature_names=X.columns, class_names=model.classes_, filled=True)
plt.show()
