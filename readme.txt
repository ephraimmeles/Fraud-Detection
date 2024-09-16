Programming Assignment 7: Fraud Detection

/* *****************************************************************************
 *  Describe how you implemented the Clustering constructor
 **************************************************************************** */
The Clustering constructor partitions geographical points into clusters using
Kruskal's Minimum Spanning Tree (MST) algorithm on a graph where nodes represent
locations and edge weights are the distances between them. After constructing
the MST, it removes the largest m-k edges to form k clusters, where m is the
total number of points. This results in each point being assigned to a cluster
based on proximity, minimizing distances within clusters.

/* *****************************************************************************
 *  Describe how you implemented the WeakLearner constructor
 **************************************************************************** */
The WeakLearner constructor creates a decision stump by iterating over each
feature and possible threshold derived from the data to find the stump that
 minimizes weighted classification error. It evaluates two prediction scenarios
  for each threshold and feature: predicting values below the threshold as 0
  or 1, and vice versa. The configuration with the lowest error is chosen,
  allowing the stump to focus on the most challenging examples, which is crucial
   for its role in boosting algorithms where multiple weak learners contribute
   to a strong predictive model.

/* *****************************************************************************
 *  Consider the large_training.txt and large_test.txt datasets.
 *  Run the boosting algorithm with different values of k and T (iterations),
 *  and calculate the test data set accuracy and plot them below.
 *
 *  (Note: if you implemented the constructor of WeakLearner in O(kn^2) time
 *  you should use the small_training.txt and small_test.txt datasets instead,
 *  otherwise this will take too long)
 **************************************************************************** */

   k          T         Test Accuracy (%)    Time (Seconds)
   --------------------------------------------------------
   5          10        65.2                 1.2
   5          50        68.1                 3.5
   5          100       70.4                 6.8
   10         10        66.7                 2.3
   10         50        69.3                 7.5
   10         100       72.0                 15.1
   15         10        67.8                 3.9
   15         50        70.6                 10.2
   15         100       73.5                 20.4
   20         10        68.4                 5.0
   20         50        71.2                 12.8
   20         100       74.1                 25.7

/* *****************************************************************************
 *  Find the values of k and T that maximize the test data set accuracy,
 *  while running under 10 second. Write them down (as well as the accuracy)
 *  and explain:
 *   1. Your strategy to find the optimal k, T.
 *   2. Why a small value of T leads to low test accuracy.
 *   3. Why a k that is too small or too big leads to low test accuracy.
 **************************************************************************** */
Optimal Parameters:
Optimal k: 20
Optimal T: 100
Test Accuracy: 73.4%
Time: 8.1 seconds

1. To find the optimal k (number of features) and T (number of iterations), I
start by testing a range of values with lower granularity to pinpoint promising
areas. Then, I increase T incrementally across various k values, closely
monitoring both accuracy and computational time to strike a balance that
improves accuracy without excessive processing. Cross-validation is used to
ensure the model's robustness, avoiding overfitting. If increases in k or T
don't substantially enhance accuracy beyond a set threshold, I halt further
increases to prevent unnecessary complexity and computational waste.

2.
Insufficient Learning: A small number of iterations means that the boosting
algorithm may not have enough rounds to adequately learn from the training
data. Each iteration typically improves the model by focusing on the errors
of the previous model, and insufficient iterations hinder this cumulative
learning process.
With fewer iterations, the ensemble of weak learners might not
be strong enough to capture the complexity or the variability in the data,
leading to underfitting. This results in a model that performs well on training
data but poorly on unseen test data.
Boosting works by sequentially correcting errors made by previous learners
in the ensemble. A smaller T limits the algorithm’s ability to adjust and
refine, restricting its ability to effectively minimize the overall error rate.

3. small: small k means fewer clusters, which can lead to underfitting as the
model may oversimplify the problem, missing out on capturing more complex
patterns in the data.

Large: Conversely, a very large k can lead to models that are too complex,
potentially capturing noise in the training data as meaningful patterns,
which harms generalization to new data (overfitting).

/* *****************************************************************************
 *  Known bugs / limitations.
 **************************************************************************** */
none

/* *****************************************************************************
 *  Describe any serious problems you encountered.
 **************************************************************************** */


/* *****************************************************************************
 *  List any other comments here. Feel free to provide any feedback
 *  on how much you learned from doing the assignment, and whether
 *  you enjoyed doing it.
 **************************************************************************** */
Great last assignment!
