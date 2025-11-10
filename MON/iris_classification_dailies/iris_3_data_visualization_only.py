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
