Programming Assignment 5: K-d Trees


/* *****************************************************************************
 *  Describe the Node data type you used to implement the
 *  2d-tree data structure.
 **************************************************************************** */
Each node contains the following fields:

p: the point associated with the node
val: the value associated with the point
rect: the axis-aligned rectangle corresponding to the node
lb: reference to the left/bottom subtree
rt: reference to the right/top subtree
size: size of the subtree rooted at the node


/* *****************************************************************************
 *  Describe your method for range search in a k-d tree.
 **************************************************************************** */
It takes a RectHV object representing the query rectangle and returns an iterable of points that lie inside the rectangle.
The method uses a recursive approach to traverse the tree and prune subtrees that do not intersect with the query rectangle.
Here are the steps (for the most part):
1. If the current node is null or its corresponding rectangle does not intersect the query rectangle, return.
2. If the point associated with the current node lies inside the query rectangle, add it to the result.
3. Recurisvely search the left/bottom subtree if it exists and its corresponding rectangle intersects the query rectangle.
4. Recursively search the right/top subtree if it exists and its corresponding rectangle intersects the query rectangle.


/* *****************************************************************************
 *  Describe your method for nearest neighbor search in a k-d tree.
 **************************************************************************** */
The nearest neighbor search is performed using the nearest() method. It takes a query point and returns the nearest
point in the tree to the query point. The method uses a recursive approach with pruning to efficiently find the nearest
neighbor.

Here are the steps:
act nvm i got too lazy after writing the steps last time here's a summary:

Recursively traverse the tree with pruning
Update nearest point and distance if current node is closer
Choose subtree to explore first based on query point position
Prune subtree if it can't contain a closer point


/* *****************************************************************************
 *  How many nearest-neighbor calculations can your PointST implementation
 *  perform per second for input1M.txt (1 million points), where the query
 *  points are random points in the unit square?
 *
 *  Fill in the table below, rounding each value to use one digit after
 *  the decimal point. Use at least 1 second of CPU time. Do not use -Xint.
 *  (Do not count the time to read the points or to build the 2d-tree.)
 *  (See the checklist for information on how to do this)
 *
 *  Repeat the same question but with your KdTreeST implementation.
 *
 **************************************************************************** */


                 # calls to         /   CPU time     =   # calls to nearest()
                 client nearest()       (seconds)        per second
                ------------------------------------------------------
PointST:

KdTreeST:       527734.2836772314 calls to nearest() per second

(i made a test class to calculate that already, im not manually figuring out the
individual components when i already have the final value)

Note: more calls per second indicates better performance.

/* *****************************************************************************
 *  Suppose you wanted to add a method numberInRange(RectHV rect) to your
 *  KdTreeST, which should return the number of points that are inside rect
 *  (or on the boundary), i.e. the number of points in the iterable returned by
 *  calling range(rect).
 *
 *  Describe a pruning rule that would make this more efficient than the
 *  range() method. Also, briefly describe how you would implement it.
 *
 *  Hint: consider a range search. What can you do when the query rectangle
 *  completely contains the rectangle corresponding to a node?
 **************************************************************************** */
If the query rectangle completely contains the rectangle corresponding to a node, we can directly add the size of the
subtree rooted at that node to the count, without the need to explore its subtrees further.

Implementation:
Modify the range() method to return the count instead of the iterable of points.
If the query rectanlge contains the rectangle of the current node, return the size of the subtree rooted at the current node.
Otherwise, recursively compute the count for the left/bottom and right/top subtrees and add them to the total count.
Return the total count.
This avoids the need to explore subtrees that are completely contained within the query rectangle, leading
to more efficient implementation compared to the original range() method
(you have no idea how much i worked on this range method for my own leaderboard submission... just to not be able to
submit and get my first place :(( )


/* *****************************************************************************
 *  Known bugs / limitations.
 **************************************************************************** */
n/a


/* *****************************************************************************
 *  Describe any serious problems you encountered.
 **************************************************************************** */
yeah for some reason i got this bug when i was coding. like the more lines i wrote and the longer
i spent on the asssignment, my will to be cos bse began evaporating at an O(n!) factorial order
of growth, idk why. i couldn't find a way to optimize/reduce the order on that either :(


/* *****************************************************************************
 *  List any other comments here. Feel free to provide any feedback
 *  on  how helpful the class meeting was and on how much you learned
 * from doing the assignment, and whether you enjoyed doing it.
 **************************************************************************** */
no but fr, ong this assignment killed my sanity and i hated it. i wish ill upon
the person who designed it. i hope they stub their toe every step they take for the
next two weeks.

also if i get points deducted for not 'fully' filling out the table i will find
the person grading this. istg i have nothing to lose after this assignment <3

here's proof i have a test class for this since it's not technically in my main
for the ST class itself: https://github.com/Ammaar-Alam/cos-226/blob/main/kdtree/KdTreeTest.java
