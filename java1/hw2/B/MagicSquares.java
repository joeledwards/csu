class MagicSquares 
{
    public static void main(String args[]) {
        if (args.length != 1) {
            usage();
        }

        int n = 0;
        try {
            n = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            usage();
        }

        if ((n % 2) != 1) {
            usage();
        }

        int[][] result = solve(n);

        display(result, n);
    }

    public static void display(int[][] square, int n) {
        int cmax;
        int max = cmax = n * n;
        int width = 1;
        while (cmax >= 10) {
            width++;
            cmax /= 10;
        }

        for (int y = 0; y < n; y++) {
            for (int x = 0; x < n; x++) {
                System.out.print(String.format(String.format("  %%%dd", width), square[x][y]));
            }
            System.out.println("");
        }
    }

    public static int[][] solve(int n) {
        int max = n * n;
        int[][] square = new int[n][n];
        int x = n / 2;
        int y = 0;

        for (int k = 1; k <= max; k++) {
            if (square[x][y] != 0) {
                if (y == (n - 2)) {
                    y = 0;
                } else if (y == (n - 1)) {
                    y = 1;
                } else {
                    y += 2;
                }
                if (x == (n - 1)) {
                    x = 0;
                } else {
                    x++;
                }
            }
            square[x][y] = k;
            if (x == 0) {
                x = n - 1;
            } else {
                x--;
            }
            if (y == 0) {
                y = n - 1;
            } else {
                y--;
            }
        }

        return square;
    }

    private static void usage() {
        System.out.println("usage: MagicSquares <size>");
        System.out.println("       size - must be an odd integer");
        System.exit(1);
    }
}
