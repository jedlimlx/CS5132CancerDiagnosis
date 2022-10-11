# Breast Cancer Diagnosis
> Done by Jed Lim, Matthew Kan, Karimi Zayan and Mayukh Das
## Intro
Breast Cancer is the number one most common cancer amongst women in Singapore. 
Early and accurate diagnosis of breast cancer is important for breast-saving and life saving treatment. 

Hence, we as part of our CS5132 project, have decided to implement a Decsion Tree to classify breast tumor cells to see if they are malignant or benign. 

## Dataset 
The dataset used is the [Breast Cancer Wisconsin (Diagnistic) Data set](https://www.kaggle.com/datasets/uciml/breast-cancer-wisconsin-data)

# Usage

## Build Instructions
In order to build and run the application, running (in the comandline)

`./gradlew run`

should suffice. Gradle should automatically pull all dependencies.

## GUI
The GUI is relatively straightforward, 

![](https://github.com/jedlimlx/CS5132CancerDiagnosis/raw/master/TeX/screenshot006.png)

Just enter the measured values of the patient's cells in the left text input, and then press diagnose.

Data can be imported, and the predictions can then be exported after pressing diagnose. 
