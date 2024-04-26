Programming Assignment 7: Seam Carving


/* *****************************************************************************
 *  Describe concisely your algorithm to find a horizontal (or vertical)
 *  seam.
 **************************************************************************** */
- Create an energy matrix by calcuatling the energy value for each pixel in the picture
using the energy method.
- Initialize two 2D arrays: distTo to store the minimum total energy from the top row to
each pixel, and edgeTo to store the previous pixel in the path.
- Initialize the first row of distTo with the energy values of the corresponding pixels.
- Iterate through each row from top to bottom, and for each pixel, calculate the minimum
total energy to reach that pixel by considering the energies of the three pixels above it
(left, center, and right) and adding the energy of the current pixel. Update distTo and edgeTo accordingly.
- After processing all rows, find the pixel in the bottom row with the minimum total energy.
- Trace back the path from the minimum pixel in the bottom row to the top row using the edgeTo
array to construct the vertical seam.

To find a horizotnaln seam, the algorithm first transposes the picture, then finds
a vertical seam in the transposed picture, and finally transposes the seam back to
obtain the horizontal seam.



/* *****************************************************************************
 *  Describe what makes an image suitable to the seam-carving approach
 *  (in terms of preserving the content and structure of the original
 *  image, without introducing visual artifacts). Describe an image that
 *  would not work well.
 **************************************************************************** */
Presence of low-energy regions: The image should have areas with low energy values,
such as homogeneous backgrounds or smooth textures. Seams can be removed from these
regions without significantly impacting the content and structure of the image.
Absence of prominent horizontal or vertical features: Images with strong horizontal
or vertical lines or patterns may not work well with seam carving because removing
seams along those directions can disrupt the continuity and introduce visual artifacts.
Sufficient resolution: The image should have a high enough resolution to allow for
seam removal without causing noticeable pixelation or loss of detail.

EXAMPLE:
 a picture of a chess board. the regular grid pattern of the chess board has strong horizontal and vertical lines,
 and removing seams would distort the structure and introduce visual artifacts. also, images with intricate
 details or text may also be challenging for seam carving as removing seams can alter or remove important information.


/* *****************************************************************************
 *  Perform computational experiments to estimate the running time to reduce
 *  a W-by-H image by one column and one row (i.e., one call each to
 *  findVerticalSeam(), removeVerticalSeam(), findHorizontalSeam(), and
 *  removeHorizontalSeam()). Use a "doubling" hypothesis, where you
 *  successively increase either W or H by a constant multiplicative
 *  factor (not necessarily 2).
 *
 *  To do so, fill in the two tables below. Each table must have 5-10
 *  data points, ranging in time from around 0.25 seconds for the smallest
 *  data point to around 30 seconds for the largest one.
 **************************************************************************** */

(keep W constant)
 W = 2000
 multiplicative factor (for H) = 1.5

 H           time (seconds)      ratio       log ratio
------------------------------------------------------
500         0.28               -          -
750         0.63               2.25       0.35
1125        1.42               2.25       0.35
1688        3.19               2.24       0.35
2532        7.16               2.24       0.35
3798        16.06              2.24       0.35

(keep H constant)
H = 2000
multiplicative factor (for W) = 1.5

 W           time (seconds)      ratio       log ratio
------------------------------------------------------
500         0.29               -          -
750         0.65               2.24       0.35
1125        1.46               2.25       0.35
1688        3.28               2.25       0.35
2532        7.37               2.24       0.35
3798        16.55              2.24       0.35



/* *****************************************************************************
 *  Using the empirical data from the above two tables, give a formula
 *  (using tilde notation) for the running time (in seconds) as a function
 *  of both W and H, such as
 *
 *       ~ 5.3*10^-8 * W^5.1 * H^1.5
 *
 *  Briefly explain how you determined the formula for the running time.
 *  Recall that with tilde notation, you include both the coefficient
 *  and exponents of the leading term (but not lower-order terms).
 *  Round each coefficient and exponent to two significant digits.
 **************************************************************************** */


Running time (in seconds) to find and remove one horizontal seam and one
vertical seam, as a function of both W and H:


    ~ 3.5*10^-7 * W^1.2 * H^1.2
EXPLANATION:
observe that the running time increases by a factor of approximately 2.24
(ratio) when either W or H is multiplied by 1.5. Taking the logarithm of the
ratio, we get a value close to 0.35. this means that the running time has a
polynomial relationship with both W and H, with exponents close to 1.2 (log(2.24) / log(1.5) ~ 1.2).

then to detemrine the coefficient, can use the data point with W = 2000 and H = 2000,
which has a running time of approximately 7.16 seconds. THUS:

7.16 ~ coefficient * 2000^1.2 * 2000^1.2

Solving for the coefficient:
coefficient ≈ 7.16 / (2000^1.2 * 2000^1.2) ≈ 3.5*10^-7


/* *****************************************************************************
 *  Known bugs / limitations.
 **************************************************************************** */
The algorithm may not perform optimally for very large images due to the memory
requirements of storing the energy matrix and the distance and edgeTo arrays.




/* *****************************************************************************
 *  Describe any serious problems you encountered.
 **************************************************************************** */
n/a



/* *****************************************************************************
 *  List any other comments here. Feel free to provide any feedback
 *  on how much you learned from doing the assignment, and whether
 *  you enjoyed doing it.
 **************************************************************************** */
yeah, when i wanted to add a caching array for the purpose of the leaderboard assignment,
i began to fail the memory tests? i dont know how you can cache w/o failing the memory tests
on the original assignment, like my leaderboard submission was still made and didnt say i
failed any tests, but i failed the memory tests on the original submission. at the end, i ran out
of all my tigerfile submissions, so i redid this assignment without any checks... i hope maybe
the grading is kind of lenient,,, you can look at my leaderboard submission to see i did pass all the
checks but the memory one originally... but yeah
