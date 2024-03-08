Programming Assignment 2: Deques and Randomized Queues


/* *****************************************************************************
 *  Explain briefly how you implemented the randomized queue and deque.
 *  Which data structure did you choose (array, linked list, etc.)
 *  and why?
 **************************************************************************** */
I chose a doubly linked list. This was because I needed efficient operations
@ both ends of the deque. Each node in the list contains two pointers: one to the next
node and another to the previous node, enabling constant-time additions and removals from
either end without needing to shift elements as in an array.

For the randomized queue, I stole -  I mean took influence from the resize array
code that was shown in the lecture to store the items. This was because I needed random
access to any item, which arrays naturally support ,,,, and the lecture gave us free code
yk. To ensure that enqueue and dequeue methods remained effeicient as the structure grows/shrinks,
the array resizes itself by doubling its capacity when full and halving when more than
three-quarters empty. And to get uniform randomness in dequeue operations, I chose to swap
rhe removed item with the last item in the array and then return the last item, which
avoids leaving holes in the array and ensures constant amortized time complexity
(i think? i think that's the correct explanation idrk lol).

/* *****************************************************************************
 *  How much memory (in bytes) do your data types use to store n items
 *  in the worst case? Use the 64-bit memory cost model from Section
 *  1.4 of the textbook and use tilde notation to simplify your answer.
 *  Briefly justify your answers and show your work.
 *
 *  Do not include the memory for the items themselves (as this
 *  memory is allocated by the client and depends on the item type)
 *  or for any iterators, but do include the memory for the references
 *  to the items (in the underlying array or linked list).
 **************************************************************************** */

Randomized Queue:   ~  16n  bytes

Deque:              ~  40n  bytes

JUSTIFICATION:

+ Randomized:
- Object overhead: 16 bytes
- Integer size and padding: 8 bytes
- Reference to array: 8 bytes
- Array overhead: 16 bytes
- Array contents (worst case when just doubled): 2n * 8 bytes
- Total (using tilde notation): ~ 16n bytes



+ Deque:
- Object overhead: 16 bytes
- Two references (first and last): 16 bytes
- Integer size and padding: 8 bytes
- Per node: 40 bytes
- Total for n nodes: n * 40 bytes
- Total (using tilde notation): ~ 40n bytes


/* *****************************************************************************
 *  Known bugs / limitations.
 **************************************************************************** */
idk tbh? i dont think this iterator really supports concurrent modications?
like you can't have both add/remove calls while iterating

/* *****************************************************************************
 *  Describe any serious problems you encountered.
 **************************************************************************** */
n/a


/* *****************************************************************************
 *  List any other comments here. Feel free to provide any feedback
 *  on how much you learned from doing the assignment, and whether
 *  you enjoyed doing it.
 **************************************************************************** */
n/a
