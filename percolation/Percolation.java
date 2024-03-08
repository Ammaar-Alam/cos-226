import edu.princeton.cs.algs4.QuickFindUF;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean[][] grid; // grid of all sites
    private final int n; // grid size
    private final QuickFindUF uf; // UF object for percolation check
    private final WeightedQuickUnionUF backwashThingy; // prevent backwash (hopefully)
    private final int topVirtual; // virtual top site index
    private final int bottomVirtual; // virtual bottom site index
    private int openSitesCount; // #of open sites


    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n must be greater than 0");
        }
        this.n = n;
        grid = new boolean[n][n];
        // ammaar istg if u forget the +2 for virtual sites again
        uf = new QuickFindUF(n * n + 2); // i didnt forget yay
        backwashThingy = new WeightedQuickUnionUF(n * n + 1);
        topVirtual = n * n;
        bottomVirtual = n * n + 1;
        openSitesCount = 0;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (!isOpen(row, col)) {
            grid[row][col] = true;
            openSitesCount++;
            int siteIndex = encode(row, col);
            // connect to top if in row
            if (row == 0) {
                uf.union(siteIndex, topVirtual);
                backwashThingy.union(siteIndex, topVirtual);
            }
            // connect to bot if in row
            if (row == n - 1) {
                uf.union(siteIndex, bottomVirtual);
            }
            // connec to adj open sites
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
        return backwashThingy.find(topVirtual) ==
                backwashThingy.find(encode(row, col));
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openSitesCount;
    }

    // does the system percolate?
    public boolean percolates() {
        return uf.find(topVirtual) == uf.find(bottomVirtual);
    }

    // ... validates... idk wat else to say
    private void validate(int row, int col) {
        if (row < 0 || row >= n || col < 0 || col >= n) {
            throw new IllegalArgumentException("Index out of bounds");
        }
    }

    // encode 2D to 1D index
    private int encode(int row, int col) {
        return row * n + col;
    }

    // connect open adjacent sites
    private void connectAdjacent(int row, int col) {
        int siteIndex = encode(row, col);
        if (row > 0 && isOpen(row - 1, col)) {
            uf.union(siteIndex, encode(row - 1, col));
            backwashThingy.union(siteIndex, encode(row - 1, col));
        }
        if (row < n - 1 && isOpen(row + 1, col)) {
            uf.union(siteIndex, encode(row + 1, col));
            backwashThingy.union(siteIndex, encode(row + 1, col));
        }
        if (col > 0 && isOpen(row, col - 1)) {
            uf.union(siteIndex, encode(row, col - 1));
            backwashThingy.union(siteIndex, encode(row, col - 1));
        }
        if (col < n - 1 && isOpen(row, col + 1)) {
            uf.union(siteIndex, encode(row, col + 1));
            backwashThingy.union(siteIndex, encode(row, col + 1));
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
        percolation.isFull(4, 1); // add proper test later
        percolation.isOpen(4, 1); // add proper test later

        // check if system percolates after opening vert line of sites
        StdOut.println("Percolates: " + percolation.percolates());
        StdOut.println("Number of open sites: " + percolation.numberOfOpenSites());
        StdOut.println("Is Open: " + percolation.isOpen(4, 1));


    }
}
