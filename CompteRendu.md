# Introduction
During this year of Master 2 MIAGE, in the context of Model Driven Engineering, our final project was to create a **Machine Learning DSL**. In this report, we will present the different steps of this project, from the creation of the grammar, to the Compiler creation and finally the benchmark of the different algorithms and framework.

# Programming 
In this first part, we will see the different stages of the programming part. 
## The grammar 
The first thing to do was to create a grammar. The main goal of this grammar was to simplify the creation of Machine Learning algorithm. We first identified the information needed : the location of a dataset, the choice of framework, the choice of algorithm with different parameters and the choice of the metrics. Once our grammar done, we just had to run it on a new Eclipse and create a new file with the extension **".mml"**. We can use `Ctrl+space` to get the different options through auto-completion. 
We used this to create a lot of **".mml"** files for a testing later. 

## Compiler
Once our grammar done, we chose a **compiler transformation** approach. The main idea was to transform our mml code into one of the 3 following programming languages : **Python** (using **scikit-learn** framework), **R**, and **Java** (using **Weka** framework). 
Our modus operandi was : 
	- Code in the target language (R, Java, Pyhton). This step helped us understanding how the different languages worked. 
	- Then we created the compiler for the language. We split the different stages for each algorithms : 
&nbsp;&nbsp;&nbsp;1) Initialization : import of the libraries, initialization of variables...
&nbsp;&nbsp;&nbsp;2) Treatment of the algorithm itself (Random Forest, SVM, Decision Tree, Logistic Regression)
&nbsp;&nbsp;&nbsp;3) Treatment of the metrics (accuracy, recall, precision, F1)



## Creation of a CSV File 
The final step of the programming stage was to store the result. In fact our goal was to compare the the algorithm and the framework. In order to get a more accurate result, we decided to calculate the average accuracy and the average execution time. We chose to make 20 iteration for each "mml" file. 
We store all the data in a CSV file with the following structure :
 
dataset ; framework ; algo ; accuracy ; execution time
 

# Benchmark
Now that the compiler and our performance saving system are working, we can compare the results of all the algorithm. We chose the accuracy as metrics and the execution time. The idea to chose the best one is to keep a good accuracy while reducing the execution time. 
## Framework
![algo Decision Tree](https://github.com/DjangoBK/MML-classification/blob/master/capture_Histogramme/algo_DT.PNG)
![algo Logistic Regression](https://github.com/DjangoBK/MML-classification/blob/master/capture_Histogramme/algo_LR.PNG)

We chose these two graphs because the others algorithms weren't coded in all the frameworks. We can see from this result that **scikit-learn** is the quickest, it's not always the most accurate one but the difference isn't enough significant to disqualify it. 
Our conclusion on the framework is that **scikit-learn** is the better one.
## Algorithm
![framework scitkit](https://github.com/DjangoBK/MML-classification/blob/master/capture_Histogramme/framework_Scikit-learn.PNG)
![framework weka](https://github.com/DjangoBK/MML-classification/blob/master/capture_Histogramme/framework_Weka.PNG)
![framework R](https://github.com/DjangoBK/MML-classification/blob/master/capture_Histogramme/framework_R.PNG)

For the 3 frameworks, the Logistic Regression is the most accurate algorithm. It's not the quickest one, but the difference is in ms.

![framework scitkit](https://github.com/DjangoBK/MML-classification/blob/master/capture_Histogramme/framework_Iris.PNG)
![framework scitkit](https://github.com/DjangoBK/MML-classification/blob/master/capture_Histogramme/framework_wine.PNG)

When compared to a dataset that contains more variables, there is a higher execution time and a lower accuracy.
The decrease in accuracy is due to the higher number of variables. This makes forecasting more complex.

# Conclusion
To conclude, we can see that *Scikit-learn* is always the quickest framework with an execution time 0.5 to 1s quicker than *R* and *Weka*. The most accurate one seems to be *R* but the difference isn't very relevant in every case. 
## Issues 
During this project, we faced multiples issues, we solved some of them but we didn't have the time to solve them all. 
There is an issue when we use a different dataset with *R* and *Weka*, it seems to be an issue with the number of rows. 
There are also algorithms unfinished : 
	- SVM is not include in *Weka* 
	- The calculation of accuracy with *R* isn't working (return 0)

## Our feelings about this project
We found this project very interesting. We didn't have a huge Machine Learning background, it was really interesting to learn about this topic. It also helped us understanding DSL, compilation transformation...