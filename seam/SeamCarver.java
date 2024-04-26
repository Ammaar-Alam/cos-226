import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;

public class SeamCarver {
    private Picture picture;        // current picture
    private int width;              // width of current picture
    private int height;             // height of current picture

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null)
            throw new IllegalArgumentException();

        this.picture = new Picture(picture);
        this.width = picture.width();
        this.height = picture.height();
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
        validateColumn(x);
        validateRow(y);

        // handle border pixels
        if (x == 0 || x == width - 1 || y == 0 || y == height - 1) {
            return borderEnergy(x, y);
        }

        // calculate energy for non-border pixels
        int lx = x - 1;
        int rx = x + 1;
        int uy = y - 1;
        int dy = y + 1;

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

        int dxRed = rRight - rLeft;
        int dxGreen = gRight - gLeft;
        int dxBlue = bRight - bLeft;

        int dyRed = rDown - rUp;
        int dyGreen = gDown - gUp;
        int dyBlue = bDown - bUp;

        int dxSquared = dxRed * dxRed + dxGreen * dxGreen + dxBlue * dxBlue;
        int dySquared = dyRed * dyRed + dyGreen * dyGreen + dyBlue * dyBlue;

        return Math.sqrt(dxSquared + dySquared);
    }

    // calculate energy for border pixels
    private double borderEnergy(int x, int y) {
        if (x == 0 || x == width - 1) {
            int uy, dy;
            if (y == 0) {
                uy = height - 1;
                dy = y + 1;
            }
            else if (y == height - 1) {
                uy = y - 1;
                dy = 0;
            }
            else {
                uy = y - 1;
                dy = y + 1;
            }

            int rgbUp = picture.getRGB(x, uy);
            int rgbDown = picture.getRGB(x, dy);

            int rUp = (rgbUp >> 16) & 0xFF;
            int gUp = (rgbUp >> 8) & 0xFF;
            int bUp = rgbUp & 0xFF;

            int rDown = (rgbDown >> 16) & 0xFF;
            int gDown = (rgbDown >> 8) & 0xFF;
            int bDown = rgbDown & 0xFF;

            int dyRed = rDown - rUp;
            int dyGreen = gDown - gUp;
            int dyBlue = bDown - bUp;

            return dyRed * dyRed + dyGreen * dyGreen + dyBlue * dyBlue;
        }
        else {
            int lx, rx;
            if (x == 0) {
                lx = width - 1;
                rx = x + 1;
            }
            else {
                lx = x - 1;
                rx = 0;
            }

            int rgbLeft = picture.getRGB(lx, y);
            int rgbRight = picture.getRGB(rx, y);

            int rLeft = (rgbLeft >> 16) & 0xFF;
            int gLeft = (rgbLeft >> 8) & 0xFF;
            int bLeft = rgbLeft & 0xFF;

            int rRight = (rgbRight >> 16) & 0xFF;
            int gRight = (rgbRight >> 8) & 0xFF;
            int bRight = rgbRight & 0xFF;

            int dxRed = rRight - rLeft;
            int dxGreen = gRight - gLeft;
            int dxBlue = bRight - bLeft;

            return dxRed * dxRed + dxGreen * dxGreen + dxBlue * dxBlue;
        }
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        transposeMatrix();
        int[] horizontalSeam = findVerticalSeam();
        transposeMatrix();
        return horizontalSeam;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        // create energy matrix
        double[][] energyMatrix = new double[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                energyMatrix[y][x] = energy(x, y);
            }
        }

        // initialize distance and edgeTo arrays
        double[][] distTo = new double[height][width];
        int[][] edgeTo = new int[height][width];

        // initialize first row of distTo with energy values
        for (int x = 0; x < width; x++) {
            distTo[0][x] = energyMatrix[0][x];
        }

        // fill in distTo and edgeTo using dynamic programming
        for (int y = 1; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double minDist = Double.POSITIVE_INFINITY;
                int minX = -1;

                // check left, center, and right pixels in previous row
                for (int dx = -1; dx <= 1; dx++) {
                    int nx = x + dx;
                    if (nx >= 0 && nx < width) {
                        double dist = distTo[y - 1][nx] + energyMatrix[y][x];
                        if (dist < minDist) {
                            minDist = dist;
                            minX = nx;
                        }
                    }
                }

                distTo[y][x] = minDist;
                edgeTo[y][x] = minX;
            }
        }

        // find the minimum distance in the last row
        double minDist = Double.POSITIVE_INFINITY;
        int minX = -1;
        for (int x = 0; x < width; x++) {
            if (distTo[height - 1][x] < minDist) {
                minDist = distTo[height - 1][x];
                minX = x;
            }
        }

        // reconstruct the seam path
        int[] seam = new int[height];
        seam[height - 1] = minX;
        for (int y = height - 2; y >= 0; y--) {
            seam[y] = edgeTo[y + 1][seam[y + 1]];
        }

        return seam;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (height <= 1)
            throw new IllegalArgumentException();
        validateSeam(seam, width);

        transposeMatrix();
        removeVerticalSeam(seam);
        transposeMatrix();
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (width <= 1)
            throw new IllegalArgumentException();
        validateSeam(seam, height);

        Picture newPicture = new Picture(width - 1, height);
        for (int y = 0; y < height; y++) {
            int seamX = seam[y];
            for (int x = 0; x < seamX; x++) {
                newPicture.setRGB(x, y, picture.getRGB(x, y));
            }
            for (int x = seamX; x < width - 1; x++) {
                newPicture.setRGB(x, y, picture.getRGB(x + 1, y));
            }
        }
        picture = newPicture;
        width--;
    }

    // transpose the picture
    private void transposeMatrix() {
        Picture transposedPicture = new Picture(height, width);
        for (int y = 0; y < width; y++) {
            for (int x = 0; x < height; x++) {
                transposedPicture.setRGB(x, y, picture.getRGB(y, x));
            }
        }
        picture = transposedPicture;
        int temp = width;
        width = height;
        height = temp;
    }

    // validate column index
    private void validateColumn(int col) {
        if (col < 0 || col >= width)
            throw new IllegalArgumentException();
    }

    // validate row index
    private void validateRow(int row) {
        if (row < 0 || row >= height)
            throw new IllegalArgumentException();
    }

    // validate seam
    private void validateSeam(int[] seam, int length) {
        if (seam == null || seam.length != length)
            throw new IllegalArgumentException();
        for (int i = 0; i < seam.length; i++) {
            if (seam[i] < 0 || seam[i] >= length)
                throw new IllegalArgumentException();
            if (i > 0 && Math.abs(seam[i] - seam[i - 1]) > 1)
                throw new IllegalArgumentException();
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        Picture picture = new Picture(args[0]);
        SeamCarver seamCarver = new SeamCarver(picture);

        // test width() and height()
        StdOut.println("width: " + seamCarver.width());
        StdOut.println("height: " + seamCarver.height());

        // test energy()
        for (int x = 0; x < seamCarver.width(); x++) {
            for (int y = 0; y < seamCarver.height(); y++) {
                StdOut.printf("energy at (%d, %d): %.2f\n", x, y,
                              seamCarver.energy(x, y));
            }
        }

        // test findVerticalSeam() and removeVerticalSeam()
        int[] verticalSeam = seamCarver.findVerticalSeam();
        StdOut.println("Vertical seam:");
        for (int y : verticalSeam)
            StdOut.print(y + " ");
        StdOut.println();
        seamCarver.removeVerticalSeam(verticalSeam);
        StdOut.println("wdith after vertical seam removal: " + seamCarver.width());

        // test findHorizontalSeam() and removeHorizontalSeam()
        int[] horizontalSeam = seamCarver.findHorizontalSeam();
        StdOut.println("horizonal seam:");
        for (int x : horizontalSeam)
            StdOut.print(x + " ");
        StdOut.println();
        seamCarver.removeHorizontalSeam(horizontalSeam);
        StdOut.println("height after horizontal seam removal: " + seamCarver.height());

        // test picture()
        Picture resultPicture = seamCarver.picture();
        StdOut.println("resulting width: " + resultPicture.width());
        StdOut.println("resulting height: " + resultPicture.height());
    }
}
