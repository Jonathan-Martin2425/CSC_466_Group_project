# CSC_466_Group_project

data sets to use
https://www.kaggle.com/datasets/emanfatima2025/titanic-passenger-survival-prediction-dataset/data

algorithms
1. SVM
2. linear regression

updates
- parsed data into a matrix/2d list with details on attributes in TitanicProject.java
- cases where age = NULL were set to the mean of all other age values to not affect SVM
- even though some attributes are integers, made 2d_list of type Double because some attributes required them and is required algorithms we are implementing
