#!/usr/bin/env python
# coding: utf-8

# # CS5132 PA2: Breast Cancer Prediction using Trees

# ### By Jed, Matthew, Mayukh and Zayan


import numpy as np
import pandas as pd
import matplotlib.pyplot as plt 
import seaborn as sns


# ## Data Acquisition

# The dataset used was the Breast Cancer Wisconsin (Diagnostic) Data Set from the UCI Machine Learning Repository. 

df = pd.read_csv('data.csv')
# The dataset contained 569 instances, with no missing data. 

# ## Data Processing

# As per the reference paper ‘Fuzzy method for pre-diagnosis of breast cancer from the Fine Needle Aspirate analysis’, we added newly generated features of ‘homogeneity’ and ‘uniformity’ that were demonstrated to have diagnostic importance. 
# 
# ‘Homogeneity’ was the difference between the extreme value of symmetry and the mean value of symmetry. ‘Homogeneity’ was an indication of the symmetry of the cell nuclei. 
# 
# ‘Uniformity’ was the difference between the radius extreme value and the radius mean value, ‘Uniformity’ was an indication of the variability in size of the cell nuclei. 

df['homogeneity'] = df['symmetry_worst'] - df['symmetry_mean']

df['uniformity'] = df['radius_worst'] - df['radius_mean']

# As per the reference paper, the features of ‘area’, ‘perimeter’, ‘homogeneity’ and ‘uniformity’ produced the best results. Thus, our method used these 4 features. All other features were dropped.

df = df[['id', 'diagnosis', 'area_worst', 'perimeter_worst', 'homogeneity', 'uniformity']]


df.to_csv('breast_cancer.csv', float_format='%.4f')

