public class SimpleTest
{
    private static final double EPS = 1e-9;
    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args)
    {
        // --- Diagonals ---
        check("inverse basic",
            arrayEquals(Diagonals.inverse(new double[]{2.0, 4.0, 5.0}),
                        new double[]{0.5, 0.25, 0.2}));

        check("inverse null", Diagonals.inverse(null) == null);

        check("sum basic",
            arrayEquals(Diagonals.sum(new double[]{1, 2, 3}, new double[]{10, 20, 30}),
                        new double[]{11, 22, 33}));

        check("sum mismatched",
            Diagonals.sum(new double[]{1, 2}, new double[]{1, 2, 3}) == null);

        check("product basic",
            arrayEquals(Diagonals.product(new double[]{2, 3, 4}, new double[]{5, 6, 7}),
                        new double[]{10, 18, 28}));

        // --- Tridiagonals ---
        check("isValid rejects wrong outer length",
            !Tridiagonals.isValidTridiagonal(new double[2][5]));

        double[][] identity = {{0,0,0}, {1,1,1}, {0,0,0}};
        double[] v1 = {7, 8, 9};
        check("linearSolve identity",
            arrayEquals(Tridiagonals.linearSolve(identity, v1), v1));

        double[][] tri = {{-1,-1,0}, {2,2,2}, {-1,-1,0}};
        double[] v2 = {1, 0, 1};
        check("linearSolve known system",
            arrayEquals(Tridiagonals.linearSolve(tri, v2), new double[]{1, 1, 1}));

        double[] d = {2, 3, 4};
        double[][] t = {{1,1,0}, {5,6,7}, {8,9,0}};
        double[][] prod = Tridiagonals.productWithDiagonal(d, t);
        check("productWithDiagonal upper", arrayEquals(prod[0], new double[]{2, 3, 0}));
        check("productWithDiagonal main",  arrayEquals(prod[1], new double[]{10, 18, 28}));
        check("productWithDiagonal lower", arrayEquals(prod[2], new double[]{24, 36, 0}));

        // --- ODE ---
        check("ODE zero a gives zero", Math.abs(ODE.solve(0.0, 50)) < 1e-10);

        double s1 = ODE.solve(1.0, 200);
        double s2 = ODE.solve(2.0, 200);
        check("ODE linearity", Math.abs(2 * s1 - s2) < 1e-9);

        double coarse = ODE.solve(1.0, 100);
        double fine   = ODE.solve(1.0, 1000);
        check("ODE converges", Math.abs(coarse - fine) < 1e-3);

        System.out.println();
        System.out.println("Passed: " + passed + "  Failed: " + failed);
    }

    private static void check(String name, boolean condition)
    {
        if (condition) {
            System.out.println("PASS: " + name);
            passed++;
        } else {
            System.out.println("FAIL: " + name);
            failed++;
        }
    }

    private static boolean arrayEquals(double[] a, double[] b)
    {
        if (a == null || b == null) return a == b;
        if (a.length != b.length) return false;
        for (int i = 0; i < a.length; i++) {
            if (Math.abs(a[i] - b[i]) > EPS) return false;
        }
        return true;
    }
}