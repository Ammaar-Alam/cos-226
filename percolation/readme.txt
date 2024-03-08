/* *****************************************************************************
 *  Put each response on the same line as the question, after the colon.
 *
 *  Operating system: macOS Sonoma 14.3
 *  [ examples are "OS X Ventura 13.5", "Windows 11", and "Ubuntu 22.04" ]
 *
 *  Compiler: Javac
 *  [ an example is "Temurin 11.0.20" ]
 *
 *  Text editor / IDE: IntelliJ IDEA 2023.2.1
 *  [ an example is "IntelliJ 2023.2" ]
 *
 *  Have you taken (part of) this course before: no
 *  ["yes" or "no"]
 *
 *  Have you taken (part of) the Coursera course Algorithms, Part I or II: no
 *  ["yes" or "no"]
 *
 *  Hours to complete assignment (optional): too many for first assignment :(
 *
 **************************************************************************** */

Programming Assignment 1: Percolation


/* *****************************************************************************
 *  Describe the data structures (i.e., instance variables) you used to
 *  implement the Percolation API.
 **************************************************************************** */
 used a two-dimensional boolean array to keep track of open and blocked sites.
 also, i used two instances of the WeightedQuickUnionUF class from the given library:
 one for managing connectivity between open sites and another get that extra credit
 for the backwash (idk how to do it and get all the extra credit points,,,, and im lazy
 and dont care enough hehe).

specifics:
 boolean[][] grid: 2D array representing the grid, sites are blocked (false) or not

 WeightedQuickUnionUF uf: object to efficiently manage the connectivity between open
 sites/ quickly determine if two sites are in the same component or not.

 int openSitesCount: how many sites are open idk

 int topVirtualIndex: represents a virtual top site used to check for percolation
 all top row sites are connected to this virtual site (or they should be atl)

 int bottomVirtualIndex: see above ^





/* *****************************************************************************
 *  Briefly describe the algorithms you used to implement each method in
 *  the Percolation API.
 **************************************************************************** */
open(): checks if a site is open -> if not: open site & connect to neighbors and
yk update # of open site etc etc, and connecting to virtual top rows if there
isOpen(): just returns boolean value of the site
isFull(): check if opensite is connected to top by checking virtual top site
numberOfOpenSites(): counter go brrrrrr when a new site is opened
percolates(): checks if any sites in the bottom row are full/if percolate is percolating

/* *****************************************************************************
 *  First, implement Percolation using QuickFindUF.
 *  What is the largest value of n that PercolationStats can handle in
 *  less than one minute on your computer when performing T = 100 trials?
 *
 *  Fill in the table below to show the values of n that you used and the
 *  corresponding running times. Use at least 5 different values of n.
 **************************************************************************** */

 T = 100

 n          time (seconds)
--------------------------
64          0.234
128         3.042
256         46.328
272         59.233
274         61.77

312         102.568



/* *****************************************************************************
 *  Describe the strategy you used for selecting the values of n.
 **************************************************************************** */
idk man i just kinda doubled it every now and then lol


/* *****************************************************************************
 *  Next, implement Percolation using WeightedQuickUnionUF.
 *  What is the largest value of n that PercolationStats can handle in
 *  less than one minute on your computer when performing T = 100 trials?
 *
 *  Fill in the table below to show the values of n that you used and the
 *  corresponding running times. Use at least 5 different values of n.
 **************************************************************************** */

 T = 100

 n          time (seconds)
--------------------------
256         0.261
512         1.011
1024        4.572
2048        33.918
2510        59.894
2516        60.805

4096        208.638


/* *****************************************************************************
 *  Known bugs / limitations.
 **************************************************************************** */
not sure if this really counts, but when considering backwash, there is a significant
memory limitation with having to have two weighted quick unions



/* *****************************************************************************
 *  Describe any serious problems you encountered.
 **************************************************************************** */
i ran into the fact i mysteriously lost the will to complete this god forsaken class;
i fixed this bug by remembering my parents would disown me if i did not graduate.



/* *****************************************************************************
 *  List any other comments here. Feel free to provide any feedback
 *  on how much you learned from doing the assignment, and whether
 *  you enjoyed doing it.
 **************************************************************************** */
