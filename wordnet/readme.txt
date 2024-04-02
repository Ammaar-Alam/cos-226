Programming Assignment 6: WordNet


/* *****************************************************************************
 *  Describe concisely the data structure(s) you used to store the
 *  information in synsets.txt. Why did you make this choice?
 **************************************************************************** */
- A HashMap<String, HashSet<Integer>> called nounToSynsets to map each noun to the
set of synset IDs it appears in.
This allows for efficient lookup of all synset IDs associated with a given noun.
HashSet provides O(1) insertion and check operations.
- A HashMap<Integer, String> called synsetToNouns to map each synset ID to the
corresponding synset (set of nouns).


/* *****************************************************************************
 *  Describe concisely the data structure(s) you used to store the
 *  information in hypernyms.txt. Why did you make this choice?
 **************************************************************************** */
To store the information from hypernyms.txt, I just used digraphs from the given library.
Each line in hypernyms.txt represents a hypernym relationship, with the first value being
the synset ID and the subsequent values being its hypernym synset IDs. I created a diagraph
with the number of vertices equal to the number of synsets.
Each line is processed, and edges are added to the digraph using the addEdge() method from
the synset ID to each of its hypernym synset IDs.

The digraph class was a good choice because it efficiently represents the directed acyclic
graph (DAG) structure (and totally not just bc the assignment/lectures this week were all
about digraphs) of the hypernym relationships. It provides methods to check for cycles and
find the number of edges, which are kinda necessary for validating the WordNet digraph. The
digraph also allows for efficient graph traversals and shortest path computations in the
ShortectCommonAncestor class.

/* *****************************************************************************
 *  Describe concisely the algorithm you use in the constructor of
 *  ShortestCommonAncestor to check if the digraph is a rooted DAG.
 *  What is the order of growth of the worst-case running times of
 *  your algorithm? Express your answer as a function of the
 *  number of vertices V and the number of edges E in the digraph.
 *  (Do not use other parameters.) Use Big Theta notation to simplify
 *  your answer.
 **************************************************************************** */

Description:
1. Create a new digraph object and pass the input digraph to its constructor to make a
defensive copy.
2. Use the DirectedCycle class to check if the digraph has a cycle.
    2a. If a cycle is found, throw an IllegalArgumentException.
3. Iterate through all vertices in the digraph and count the number of vertices with
outdegree 0 (roots).
    3a. If the number of roots is not exactly 1, throw an exception.


Order of growth of running time:
-   Creating a new Digraph object takes O(V + E) time
-   Checking for a cycle using DirectedCycle takes O(V + E) time in the worst case
-   Iterating through all vertices to count the number of roots takes O(V) time

/* *****************************************************************************
 *  Describe concisely your algorithm to compute the shortest common ancestor
 *  in ShortestCommonAncestor. For each method, give the order of growth of
 *  the best- and worst-case running times. Express your answers as functions
 *  of the number of vertices V and the number of edges E in the digraph.
 *  (Do not use other parameters.) Use Big Theta notation to simplify your
 *  answers.
 *
 *  If you use hashing, assume the uniform hashing assumption so that put()
 *  and get() take constant time per operation.
 *
 *  Be careful! If you use a BreadthFirstDirectedPaths object, don't forget
 *  to count the time needed to initialize the marked[], edgeTo[], and
 *  distTo[] arrays.
 **************************************************************************** */

Description:
The algorithm to compute the shortest common ancestor in ShortestCommonAncestor uses a
variation of bidirectional breadth-first search (BFS):

1. Perform BFS from the source vertices (either a single vertex or a subset of vertices)
in both directions, keeping track of the distance from the source to each visited vertex.
    1a. Use two separate data structures to store the distances for each direction (distToA and distToB).
3. During the BFS, if a vertex is visited in both directions, calculate the total distance
as the sum of the distances from the source to that vertex in both directions.
4. Keep track of the minimum total distance and the corresponding vertex (ancestor) that achieves it.
4. Return the minimum total distance and the ancestor vertex.


                                 running time
method                  best case            worst case
--------------------------------------------------------
length()                O(1)                O(V + E)

ancestor()              O(1)                O(V + E)

lengthSubset()          O(1)                O(V + E)

ancestorSubset()        O(1)                O(V + E)
(give me that extra credit now pls pls)


/* *****************************************************************************
 *  Known bugs / limitations.
 **************************************************************************** */
n/a my code is totally 100000% flawless, dont quote me on that


/* *****************************************************************************
 *  Describe any serious problems you encountered.
 **************************************************************************** */
biggest challenge was optimizing SAC class for the best runtime complexity. initially i used
hashmaps to store the distances during BFS traversal but that was too slow, then i used arrays
to store the distnaces since the vertex IDs are ints and can be directly used as indices. or i
think? i got mmy leaderboard and default assignment files mixed up tbh i dont know which one ive
submitted


/* *****************************************************************************
 *  List any other comments here. Feel free to provide any feedback
 *  on how much you learned from doing the assignment, and whether
 *  you enjoyed doing it.
 **************************************************************************** */
more leaderboard assignments pls pls
also more leaderboard submissions pls pls; the leaderboard is just an ego thing atp anyway,
i dont really understand why there is a cap on submissions
