Programming Assignment 7: Fraud Detection

/* *****************************************************************************
 *  Describe how you implemented the Clustering constructor
 **************************************************************************** */
1. Created a complete EdgeWeightedGraph with the Points2D as vertices and edges weighted by Euclidean distance.
2. Computed the minimum spanning tree using KruskalMST.
3. Grabbed the k-1 lowest weight edges from the MST to form the cluster graph.
4. Used the CC class to find the connected components, which represent the clusters.


/* *****************************************************************************
 *  Describe how you implemented the WeakLearner constructor
 **************************************************************************** */
started by sorting the input points along each dimension to efficiently find the best decision
stump for that dimension. then, for each dimension and sign predictor value (0 or 1), i calculated
the weighted sum of correctly classifed points for each potential threshold value. kept track of
the best decision stump parameters that maximized the weighted sum of correctly classified points.
to handle cases where multiple points have the same coordinate, i only updated the best parameters
when the coordinate value changed. and lastly, i stored the best parameters as instance variables for
later use in prediction.

/* *****************************************************************************
 *  Consider the large_training.txt and large_test.txt datasets.
 *  Run the boosting algorithm with different values of k and T (iterations),
 *  and calculate the test data set accuracy and plot them below.
 *
 *  (Note: if you implemented the constructor of WeakLearner in O(kn^2) time
 *  you should use the small_training.txt and small_test.txt datasets instead,
 *  otherwise this will take too long)
 **************************************************************************** */

      k          T         test accuracy       time (seconds)
   --------------------------------------------------------------------------
    10          50          0.95125             0.42
    20          50          0.955               0.501
    100         100         0.9475              2.042
    50          500         0.98                5.509
    52          1000        0.9725              11.194
    40          1000        0.97875             9.158
    50          910         0.98                9.992


/* *****************************************************************************
 *  Find the values of k and T that maximize the test data set accuracy,
 *  while running under 10 second. Write them down (as well as the accuracy)
 *  and explain:
 *   1. Your strategy to find the optimal k, T.
 *   2. Why a small value of T leads to low test accuracy.
 *   3. Why a k that is too small or too big leads to low test accuracy.
 **************************************************************************** */
 Optimal values: k = 50 ; t = 910; accuracy = .98
1. basically just tried out a bunch of different combinations and recorded the results. started with small
values and gradually increased them until the running time got close to 10 seconds. then i just picked the
combination that gave the best accuracy while staying under the time limit.


2. from the results, can see that increasing the number of iterations generally leads to
higher test accuracy, but with diminishing returns. for ex, going from 50 to 500 iterations
for k=50 improves the accuracy from around 95% to 98%, but further increasing T to 1000 only giesv
a marginal gain.

3. and for the number of clusters, moderate values of k (around 40 to 50) seem to provide a good
balance between capturing sufficient detail and avoiding overfitting. very low values like k=10 may
not capture enough information, while high values like k=100 can lead to overfitting and reduced accuracy.

the optimal combination appears to be around k=50 and T=500 to 1000, which achieves a test accuracy
of 98% within a reasonable running time of 5 to 10 seconds.


/* *****************************************************************************
 *  Known bugs / limitations.
 **************************************************************************** */
n/a

/* *****************************************************************************
 *  Describe any serious problems you encountered.
 **************************************************************************** */
weak learner showed me that i was a weak learner.


/* *****************************************************************************
 *  List any other comments here. Feel free to provide any feedback
 *  on how much you learned from doing the assignment, and whether
 *  you enjoyed doing it.
 **************************************************************************** */
idk im so tired w everything lol
