Programming Assignment 3: Autocomplete


/* *****************************************************************************
 *  Describe how your firstIndexOf() method in BinarySearchDeluxe.java
 *  finds the first index of a key that is equal to the search key.
 **************************************************************************** */
The firstIndexOf() method implements binary search to locate the first occurrence of a key.
It narrows down the search by halving the search space, adjusting either the low or high
bounds based on comparinsons. When a match is found, it continues searching the lower half
to ensure the first occurance is identiifed, marking potential matches until the lowest
possible index is determined.



/* *****************************************************************************
 *  Identify which sorting algorithm (if any) that your program uses in the
 *  Autocomplete constructor and instance methods. Choose from the following
 *  options:
 *
 *    none, selection sort, insertion sort, mergesort, quicksort, heapsort
 *
 *  If you are using an optimized implementation, such as Arrays.sort(),
 *  select the principal algorithm.
 **************************************************************************** */

Autocomplete() : Arrays.sort(), which uses TimSort, a hybrid of mergesort and insertion sort
(i think)

allMatches() : Arrays.sort(), for sorting by reverse weight order

numberOfMatches() : None, as it relies on binary search rather than sorting

/* *****************************************************************************
 *  How many compares (in the worst case) does each of the operations in the
 *  Autocomplete data type make, as a function of both the number of terms n
 *  and the number of matching terms m? Use Big Theta notation to simplify
 *  your answers.
 *
 *  Recall that with Big Theta notation, you should discard both the
 *  leading coefficients and lower-order terms, e.g., Theta(m^2 + m log n).
 **************************************************************************** */

Autocomplete():     Theta(n log n) due to sorting the terms

allMatches():       Theta(log n) for binary search to find the range +
Theta(m log m) for sorting the matches

numberOfMatches():  Theta(log n) for binary search to find the range of matches




/* *****************************************************************************
 *  Known bugs / limitations.
 **************************************************************************** */
n/a


/* *****************************************************************************
 *  Describe any serious problems you encountered.
 **************************************************************************** */
none that i really remember? binary search was kinda challenging to get correctly
identifying the first/last indices of matching terms ig?


/* *****************************************************************************
 *  List any other comments here. Feel free to provide any feedback
 *  on how much you learned from doing the assignment, and whether
 *  you enjoyed doing it.
 **************************************************************************** */
n/a
