import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation2 {
    private boolean[][] grid;
    private final int n;
    private final WeightedQuickUnionUF uf, backwashUF;
    private final int topVirtual;
    private final int bottomVirtual;
    private int openSitesCount;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n must be greater than 0");
        }
        this.n = n;
        grid = new boolean[n][n];
        uf = new WeightedQuickUnionUF(n * n + 1);
        backwashUF = new WeightedQuickUnionUF(n * n + 1);
        topVirtual = 0;
        bottomVirtual = 0;
        openSitesCount = 0;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        validate(row, col);
        if (!isOpen(row, col)) {
            grid[row][col] = true;
            openSitesCount++;
            if (row == 0) {
                uf.union(encode(row, col), topVirtual);
                backwashUF.union(encode(row, col), topVirtual);
            }
            connectAdjacent(row, col);
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        validate(row, col);
        return grid[row][col];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        validate(row, col);
        return backwashUF.find(topVirtual) == backwashUF.find(encode(row, col));
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openSitesCount;
    }

    // does the system percolate
    public boolean percolates() {
        for (int col = 0; col < n; col++) {
            if (backwashUF.find(topVirtual) == backwashUF.find(encode(n - 1, col))) {
                return true;
            }
        }
        return false;
    }


    // is the cell a valid cell
    private void validate(int row, int col) {
        if (row < 0 || row >= n || col < 0 || col >= n) {
            throw new IllegalArgumentException("Index out of bounds");
        }
    }

    private int encode(int row, int col) {
        return row * n + col + 1;
    }

    // sites connected to open sites
    private void connectAdjacent(int row, int col) {
        if (row > 0 && isOpen(row - 1, col)) {
            uf.union(encode(row, col), encode(row - 1, col));
            backwashUF.union(encode(row, col), encode(row - 1, col));
        }
        if (row < n - 1 && isOpen(row + 1, col)) {
            uf.union(encode(row, col), encode(row + 1, col));
            backwashUF.union(encode(row, col), encode(row + 1, col));
        }
        if (col > 0 && isOpen(row, col - 1)) {
            uf.union(encode(row, col), encode(row, col - 1));
            backwashUF.union(encode(row, col), encode(row, col - 1));
        }
        if (col < n - 1 && isOpen(row, col + 1)) {
            uf.union(encode(row, col), encode(row, col + 1));
            backwashUF.union(encode(row, col), encode(row, col + 1));
        }
    }

    // unit testing (required)
    // the audacity to not provide test client :( cmon
    public static void main(String[] args) {
        int n = 5; // random
        Percolation percolation = new Percolation(n);

        // Open some sites
        percolation.open(0, 1);
        percolation.open(1, 1);
        percolation.open(2, 1);
        percolation.open(3, 1);
        percolation.open(4, 1);

        // check if system percolates after opening vert line of sites
        System.out.println("Percolates: " + percolation.percolates());
        System.out.println("Number of open sites: " + percolation.numberOfOpenSites());
    }
}
