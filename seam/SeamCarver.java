import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class SeamCarver {
    private Picture picture;    // current picture
    private int width;          // width of current picture
    private int height;         // width of current picture
    private double[][] energyGrid; // cache for energy calculations

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null)
            throw new IllegalArgumentException();

        this.picture = new Picture(picture);
        this.width = picture.width();
        this.height = picture.height();
        computeEnergyGrid();
    }

    // current picture
    public Picture picture() {
        Picture newPicture = new Picture(width, height);
        for (int col = 0; col < width; col++) {
            for (int row = 0; row < height; row++) {
                newPicture.setRGB(col, row, picture.getRGB(col, row));
            }
        }
        return newPicture;
    }

    // width of current picture
    public int width() {
        return this.width;
    }

    // height of current picture
    public int height() {
        return this.height;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || x >= width)
            throw new IllegalArgumentException();
        if (y < 0 || y >= height)
            throw new IllegalArgumentException();

        return energyGrid[x][y];
    }

    // compute energy grid
    private void computeEnergyGrid() {
        energyGrid = new double[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (x == 0 || x == width - 1 || y == 0 || y == height - 1) {
                    energyGrid[x][y] = computeBorderPixelEnergy(x, y);
                }
                else {
                    energyGrid[x][y] = computeInnerPixelEnergy(x, y);
                }
            }
        }
    }

    // compute energy for border pixels
    private double computeBorderPixelEnergy(int x, int y) {
        int lx = x == 0 ? width - 1 : x - 1;
        int rx = x == width - 1 ? 0 : x + 1;
        int uy = y == 0 ? height - 1 : y - 1;
        int dy = y == height - 1 ? 0 : y + 1;

        return computeEnergy(x, y, lx, rx, uy, dy);
    }

    // compute energy for inner pixels
    private double computeInnerPixelEnergy(int x, int y) {
        int lx = x - 1;
        int rx = x + 1;
        int uy = y - 1;
        int dy = y + 1;

        return computeEnergy(x, y, lx, rx, uy, dy);
    }

    // generic energy computation
    private double computeEnergy(int x, int y, int lx, int rx, int uy, int dy) {
        int rgbXLeft = picture.getRGB(lx, y);
        int rgbXRight = picture.getRGB(rx, y);
        int rgbYUp = picture.getRGB(x, uy);
        int rgbYDown = picture.getRGB(x, dy);

        int rxDiff = ((rgbXLeft >> 16) & 0xFF) - ((rgbXRight >> 16) & 0xFF);
        int gxDiff = ((rgbXLeft >> 8) & 0xFF) - ((rgbXRight >> 8) & 0xFF);
        int bxDiff = (rgbXLeft & 0xFF) - (rgbXRight & 0xFF);
        int xGradSquare = rxDiff * rxDiff + gxDiff * gxDiff + bxDiff * bxDiff;

        int ryDiff = ((rgbYUp >> 16) & 0xFF) - ((rgbYDown >> 16) & 0xFF);
        int gyDiff = ((rgbYUp >> 8) & 0xFF) - ((rgbYDown >> 8) & 0xFF);
        int byDiff = (rgbYUp & 0xFF) - (rgbYDown & 0xFF);
        int yGradSquare = ryDiff * ryDiff + gyDiff * gyDiff + byDiff * byDiff;

        return Math.sqrt(xGradSquare + yGradSquare);
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        double[][] distTo = new double[width][height];
        int[][] edgeTo = new int[width][height];

        for (int col = 0; col < width; col++) {
            for (int row = 0; row < height; row++) {
                if (row == 0) {
                    distTo[col][row] = energy(col, row);
                }
                else {
                    distTo[col][row] = Double.POSITIVE_INFINITY;
                }
            }
        }

        for (int row = 0; row < height - 1; row++) {
            for (int col = 0; col < width; col++) {
                if (col > 0) {
                    relax(col, row, distTo, edgeTo, col - 1);
                }
                relax(col, row, distTo, edgeTo, col);
                if (col < width - 1) {
                    relax(col, row, distTo, edgeTo, col + 1);
                }
            }
        }

        return buildSeam(distTo, edgeTo);
    }

    // relax the edge from pixel (row, col) to pixel (row + 1, nextCol)
    private void relax(int col, int row, double[][] distTo, int[][] edgeTo,
                       int nextCol) {
        if (distTo[nextCol][row + 1] > distTo[col][row] + energy(nextCol, row + 1)) {
            distTo[nextCol][row + 1] = distTo[col][row] + energy(nextCol, row + 1);
            edgeTo[nextCol][row + 1] = col;
        }
    }

    // build vertical seam from edgeTo array
    private int[] buildSeam(double[][] distTo, int[][] edgeTo) {
        int[] seam = new int[height];
        double minDist = Double.POSITIVE_INFINITY;
        int col = 0;
        for (int i = 0; i < width; i++) {
            if (distTo[i][height - 1] < minDist) {
                minDist = distTo[i][height - 1];
                col = i;
            }
        }

        seam[height - 1] = col;
        for (int row = height - 2; row >= 0; row--) {
            col = edgeTo[col][row + 1];
            seam[row] = col;
        }

        return seam;
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        double[][] originalEnergyGrid = energyGrid;
        transposeEnergyGrid();
        int[] seam = findVerticalSeam();
        energyGrid = originalEnergyGrid;
        width = energyGrid.length;
        height = energyGrid[0].length;
        return seam;
    }

    // transpose the energy grid
    private void transposeEnergyGrid() {
        double[][] transposedEnergyGrid = new double[height][width];
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                transposedEnergyGrid[row][col] = energyGrid[col][row];
            }
        }
        energyGrid = transposedEnergyGrid;
        int temp = width;
        width = height;
        height = temp;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        validateSeam(seam, width, height, false);

        Picture newPicture = new Picture(width, height - 1);
        for (int col = 0; col < width; col++) {
            for (int row = 0, newRow = 0; row < height; row++) {
                if (row != seam[col]) {
                    newPicture.setRGB(col, newRow, picture.getRGB(col, row));
                    newRow++;
                }
            }
        }

        this.picture = newPicture;
        this.height--;
        computeEnergyGrid();
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        validateSeam(seam, height, width, true);

        Picture newPicture = new Picture(width - 1, height);
        for (int row = 0; row < height; row++) {
            for (int col = 0, newCol = 0; col < width; col++) {
                if (col != seam[row]) {
                    newPicture.setRGB(newCol, row, picture.getRGB(col, row));
                    newCol++;
                }
            }
        }

        this.picture = newPicture;
        this.width--;
        computeEnergyGrid();
    }

    // validate seam
    private void validateSeam(int[] seam, int seamLength, int maxValue,
                              boolean isVertical) {
        if (seam == null)
            throw new IllegalArgumentException();
        if (seam.length != seamLength)
            throw new IllegalArgumentException();
        if (maxValue <= 1)
            throw new IllegalArgumentException();

        int prevValue = seam[0];
        for (int i = 0; i < seam.length; i++) {
            if (seam[i] < 0 || seam[i] >= maxValue)
                throw new IllegalArgumentException();
            if (i > 0 && Math.abs(seam[i] - prevValue) > 1)
                throw new IllegalArgumentException();
            prevValue = seam[i];
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        Picture picture = new Picture(args[0]);
        SeamCarver seamCarver = new SeamCarver(picture);

        // test width() and height()
        StdOut.println("Width: " + seamCarver.width());
        StdOut.println("Height: " + seamCarver.height());

        // test energy()
        for (int col = 0; col < seamCarver.width(); col++) {
            for (int row = 0; row < seamCarver.height(); row++) {
                StdOut.printf("Energy at (%d, %d): %.2f\n", col, row,
                              seamCarver.energy(col, row));
            }
        }

        // test findHorizontalSeam() and findVerticalSeam()
        int[] horizontalSeam = seamCarver.findHorizontalSeam();
        int[] verticalSeam = seamCarver.findVerticalSeam();
        StdOut.println("Horizontal Seam: " + Arrays.toString(horizontalSeam));
        StdOut.println("Vertical Seam: " + Arrays.toString(verticalSeam));

        // test removeHorizontalSeam() and removeVerticalSeam()
        seamCarver.removeHorizontalSeam(horizontalSeam);
        seamCarver.removeVerticalSeam(verticalSeam);

        // test picture()
        Picture removedSeamPicture = seamCarver.picture();
        StdOut.println("Removed Seam Picture Width: " + removedSeamPicture.width());
        StdOut.println("Removed Seam Picture Height: " + removedSeamPicture.height());
    }
}
