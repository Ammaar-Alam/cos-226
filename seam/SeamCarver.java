import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class SeamCarver {
    private Picture picture;    // current picture
    private int width;          // width of current picture
    private int height;         // height of current picture
    private double[][] energyArray; // energy array of current picture

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null)
            throw new IllegalArgumentException();

        this.picture = new Picture(picture);
        this.width = picture.width();
        this.height = picture.height();
        this.energyArray = calculateEnergyArray();
    }

    // calculate the energy array for the current picture
    private double[][] calculateEnergyArray() {
        double[][] energyArray = new double[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                energyArray[y][x] = energy(x, y);
            }
        }

        return energyArray;
    }

    // current picture
    public Picture picture() {
        return new Picture(picture);
    }

    // width of current picture
    public int width() {
        return width;
    }

    // height of current picture
    public int height() {
        return height;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || x >= width)
            throw new IllegalArgumentException();
        if (y < 0 || y >= height)
            throw new IllegalArgumentException();

        if (x == 0 || x == width - 1 || y == 0 || y == height - 1) {
            return computeBorderPixelEnergy(x, y);
        }
        else {
            return computeInnerPixelEnergy(x, y);
        }
    }

    // compute energy for border pixels
    private double computeBorderPixelEnergy(int x, int y) {
        int lx;
        int rx;
        int uy;
        int dy;

        if (x == 0) {
            lx = width - 1;
        }
        else {
            lx = x - 1;
        }

        if (x == width - 1) {
            rx = 0;
        }
        else {
            rx = x + 1;
        }

        if (y == 0) {
            uy = height - 1;
        }
        else {
            uy = y - 1;
        }

        if (y == height - 1) {
            dy = 0;
        }
        else {
            dy = y + 1;
        }

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
        int rgbLeft = picture.getRGB(lx, y);
        int rgbRight = picture.getRGB(rx, y);
        int rgbUp = picture.getRGB(x, uy);
        int rgbDown = picture.getRGB(x, dy);

        int rLeft = (rgbLeft >> 16) & 0xFF;
        int gLeft = (rgbLeft >> 8) & 0xFF;
        int bLeft = rgbLeft & 0xFF;

        int rRight = (rgbRight >> 16) & 0xFF;
        int gRight = (rgbRight >> 8) & 0xFF;
        int bRight = rgbRight & 0xFF;

        int rUp = (rgbUp >> 16) & 0xFF;
        int gUp = (rgbUp >> 8) & 0xFF;
        int bUp = rgbUp & 0xFF;

        int rDown = (rgbDown >> 16) & 0xFF;
        int gDown = (rgbDown >> 8) & 0xFF;
        int bDown = rgbDown & 0xFF;

        int xGradSquare = (rLeft - rRight) * (rLeft - rRight) +
                (gLeft - gRight) * (gLeft - gRight) +
                (bLeft - bRight) * (bLeft - bRight);

        int yGradSquare = (rUp - rDown) * (rUp - rDown) +
                (gUp - gDown) * (gUp - gDown) +
                (bUp - bDown) * (bUp - bDown);

        return Math.sqrt(xGradSquare + yGradSquare);
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        double[][] distTo = new double[width][height];
        int[][] edgeTo = new int[width][height];

        for (int col = 0; col < width; col++) {
            distTo[col][0] = energyArray[0][col];
        }

        for (int row = 1; row < height; row++) {
            for (int col = 0; col < width; col++) {
                double minDist = Double.POSITIVE_INFINITY;
                int minCol = -1;

                if (col > 0 && distTo[col - 1][row - 1] + energyArray[row][col] < minDist) {
                    minDist = distTo[col - 1][row - 1] + energyArray[row][col];
                    minCol = col - 1;
                }

                if (distTo[col][row - 1] + energyArray[row][col] < minDist) {
                    minDist = distTo[col][row - 1] + energyArray[row][col];
                    minCol = col;
                }

                if (col < width - 1 && distTo[col + 1][row - 1] + energyArray[row][col] < minDist) {
                    minDist = distTo[col + 1][row - 1] + energyArray[row][col];
                    minCol = col + 1;
                }

                distTo[col][row] = minDist;
                edgeTo[col][row] = minCol;
            }
        }

        return buildSeam(distTo, edgeTo);
    }

    // build vertical seam from edgeTo array
    private int[] buildSeam(double[][] distTo, int[][] edgeTo) {
        int[] seam = new int[height];
        double minDist = Double.POSITIVE_INFINITY;
        int col = -1;

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
        transpose();
        int[] seam = findVerticalSeam();
        transpose();
        return seam;
    }

    // transpose the picture and energy array
    private void transpose() {
        Picture transposedPic = new Picture(height, width);

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                transposedPic.setRGB(row, col, picture.getRGB(col, row));
            }
        }

        picture = transposedPic;
        energyArray = transposeEnergyArray();
        int temp = width;
        width = height;
        height = temp;
    }

    // transpose the energy array
    private double[][] transposeEnergyArray() {
        double[][] transposedEnergyArray = new double[width][height];

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                transposedEnergyArray[col][row] = energyArray[row][col];
            }
        }

        return transposedEnergyArray;
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

        picture = newPicture;
        height--;
        energyArray = calculateEnergyArray();
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

        picture = newPicture;
        width--;
        energyArray = calculateEnergyArray();
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
