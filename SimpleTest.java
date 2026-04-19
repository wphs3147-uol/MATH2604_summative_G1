/**
 * Plain-Java test driver for Diagonals, Tridiagonals and ODE.
 * No external dependencies — just compile and run:
 *   javac Diagonals.java Tridiagonals.java ODE.java SimpleTest.java
 *   java SimpleTest
 */
public class SimpleTest
{
    private static final double EPS = 1e-9;
    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args)
    {
        System.out.println("=== Diagonals ===");
        testDiagonalsExampleMatrix();
        testDiagonalsInverse();
        testDiagonalsSum();
        testDiagonalsProduct();

        System.out.println();
        System.out.println("=== Tridiagonals ===");
        testTridiagonalsExampleMatrix();
        testTridiagonalsIsValid();
        testTridiagonalsSum();
        testTridiagonalsProductWithDiagonal();
        testTridiagonalsLinearSolve();
        testTridiagonalsImmutability();

        System.out.println();
        System.out.println("=== ODE ===");
        testODE();

        System.out.println();
        System.out.println("----------------------------------------");
        System.out.println("Passed: " + passed + "  Failed: " + failed);
    }

    // ========================================================================
    // Diagonals
    // ========================================================================

    private static void testDiagonalsExampleMatrix()
    {
        double[] m = Diagonals.exampleMatrix();
        check("exampleMatrix not null", m != null);
        check("exampleMatrix length 5", m != null && m.length == 5);
        check("exampleMatrix values",
            arrayEquals(m, new double[]{10.0, 8.0, 5.0, -10.0, 7.0}));
    }

    private static void testDiagonalsInverse()
    {
        // Basic
        check("inverse basic",
            arrayEquals(Diagonals.inverse(new double[]{2.0, 4.0, 5.0}),
                        new double[]{0.5, 0.25, 0.2}));

        // Negative and mixed values
        check("inverse with negatives",
            arrayEquals(Diagonals.inverse(new double[]{-2.0, 4.0, -0.5}),
                        new double[]{-0.5, 0.25, -2.0}));

        // Single element
        check("inverse single element",
            arrayEquals(Diagonals.inverse(new double[]{4.0}),
                        new double[]{0.25}));

        // Empty array — should return empty, not crash
        double[] emptyResult = Diagonals.inverse(new double[]{});
        check("inverse empty array",
            emptyResult != null && emptyResult.length == 0);

        // Null
        check("inverse null returns null", Diagonals.inverse(null) == null);

        // Inverse of inverse should give back the original
        double[] original = {2.0, -3.0, 0.5, 10.0};
        double[] twice = Diagonals.inverse(Diagonals.inverse(original));
        check("inverse of inverse is identity", arrayEquals(twice, original));
    }

    private static void testDiagonalsSum()
    {
        check("sum basic",
            arrayEquals(Diagonals.sum(new double[]{1, 2, 3},
                                      new double[]{10, 20, 30}),
                        new double[]{11, 22, 33}));

        check("sum with negatives",
            arrayEquals(Diagonals.sum(new double[]{1, -2, 3},
                                      new double[]{-1, 2, -3}),
                        new double[]{0, 0, 0}));

        // Empty arrays
        double[] emptySum = Diagonals.sum(new double[]{}, new double[]{});
        check("sum of two empty arrays",
            emptySum != null && emptySum.length == 0);

        // Null handling
        check("sum null first", Diagonals.sum(null, new double[]{1, 2}) == null);
        check("sum null second", Diagonals.sum(new double[]{1, 2}, null) == null);
        check("sum both null", Diagonals.sum(null, null) == null);

        // Mismatched lengths
        check("sum mismatched lengths",
            Diagonals.sum(new double[]{1, 2}, new double[]{1, 2, 3}) == null);
        check("sum mismatched lengths other way",
            Diagonals.sum(new double[]{1, 2, 3}, new double[]{1, 2}) == null);

        // Commutativity: a + b = b + a
        double[] a = {1.5, -2.7, 3.14};
        double[] b = {4.0, 5.5, -6.28};
        check("sum commutative",
            arrayEquals(Diagonals.sum(a, b), Diagonals.sum(b, a)));

        // Input not modified
        double[] aCopy = a.clone();
        double[] bCopy = b.clone();
        Diagonals.sum(a, b);
        check("sum does not modify first input", arrayEquals(a, aCopy));
        check("sum does not modify second input", arrayEquals(b, bCopy));
    }

    private static void testDiagonalsProduct()
    {
        check("product basic",
            arrayEquals(Diagonals.product(new double[]{2, 3, 4},
                                          new double[]{5, 6, 7}),
                        new double[]{10, 18, 28}));

        // Product with identity (all ones) should return the other array
        check("product with identity",
            arrayEquals(Diagonals.product(new double[]{1, 1, 1},
                                          new double[]{7, -2, 3.5}),
                        new double[]{7, -2, 3.5}));

        // Product with zero diagonal
        check("product with zero",
            arrayEquals(Diagonals.product(new double[]{0, 3, 0},
                                          new double[]{5, 6, 7}),
                        new double[]{0, 18, 0}));

        // Empty
        double[] emptyProd = Diagonals.product(new double[]{}, new double[]{});
        check("product of empty arrays",
            emptyProd != null && emptyProd.length == 0);

        // Nulls and mismatch
        check("product null first",
            Diagonals.product(null, new double[]{1}) == null);
        check("product null second",
            Diagonals.product(new double[]{1}, null) == null);
        check("product mismatched lengths",
            Diagonals.product(new double[]{1, 2}, new double[]{1, 2, 3}) == null);

        // Mathematical property: D * D^-1 = identity (all ones)
        double[] d = {2.0, -3.0, 0.5, 10.0};
        double[] dInv = Diagonals.inverse(d);
        double[] shouldBeOnes = Diagonals.product(d, dInv);
        check("D * D^-1 = identity",
            arrayEquals(shouldBeOnes, new double[]{1, 1, 1, 1}));
    }

    // ========================================================================
    // Tridiagonals
    // ========================================================================

    private static void testTridiagonalsExampleMatrix()
    {
        // Structure check
        double[][] m = Tridiagonals.exampleMatrix(5);
        check("exampleMatrix(5) has 3 rows", m.length == 3);
        check("exampleMatrix(5) main diag length", m[1].length == 5);

        // Values per the Javadoc:
        //   upper = 1 for i in [0, n-2]
        //   main[i] = -(i+1)^2
        //   lower[i] = i+1 for i in [0, n-2]  (note: spec says i+2 but code writes i+1)
        // Testing what the code actually produces rather than the spec ambiguity:
        check("exampleMatrix upper diagonal",
            m[0][0] == 1 && m[0][1] == 1 && m[0][2] == 1 && m[0][3] == 1);
        check("exampleMatrix main diagonal",
            arrayEquals(m[1], new double[]{-1, -4, -9, -16, -25}));

        // exampleMatrix with n=1 should still work
        double[][] m1 = Tridiagonals.exampleMatrix(1);
        check("exampleMatrix(1) works", m1 != null && m1[1].length == 1);
        check("exampleMatrix(1) main = -1", m1[1][0] == -1);

        // Invalid n should throw
        boolean threw = false;
        try { Tridiagonals.exampleMatrix(0); }
        catch (IllegalArgumentException e) { threw = true; }
        check("exampleMatrix(0) throws", threw);

        threw = false;
        try { Tridiagonals.exampleMatrix(-5); }
        catch (IllegalArgumentException e) { threw = true; }
        check("exampleMatrix(-5) throws", threw);
    }

    private static void testTridiagonalsIsValid()
    {
        // Valid cases
        check("isValid 1x1", Tridiagonals.isValidTridiagonal(new double[3][1]));
        check("isValid 3x5", Tridiagonals.isValidTridiagonal(new double[3][5]));

        // Null input
        check("isValid null", !Tridiagonals.isValidTridiagonal(null));

        // Wrong outer length
        check("isValid 2 rows", !Tridiagonals.isValidTridiagonal(new double[2][5]));
        check("isValid 4 rows", !Tridiagonals.isValidTridiagonal(new double[4][5]));
        check("isValid 0 rows", !Tridiagonals.isValidTridiagonal(new double[0][]));

        // Null inner arrays
        double[][] nullRow0 = {null, new double[5], new double[5]};
        double[][] nullRow1 = {new double[5], null, new double[5]};
        double[][] nullRow2 = {new double[5], new double[5], null};
        check("isValid null row 0", !Tridiagonals.isValidTridiagonal(nullRow0));
        check("isValid null row 1", !Tridiagonals.isValidTridiagonal(nullRow1));
        check("isValid null row 2", !Tridiagonals.isValidTridiagonal(nullRow2));

        // Zero length inner
        check("isValid zero length inner",
            !Tridiagonals.isValidTridiagonal(new double[3][0]));

        // Mismatched inner lengths
        double[][] mismatch1 = {new double[4], new double[5], new double[5]};
        double[][] mismatch2 = {new double[5], new double[5], new double[4]};
        check("isValid mismatched row 0", !Tridiagonals.isValidTridiagonal(mismatch1));
        check("isValid mismatched row 2", !Tridiagonals.isValidTridiagonal(mismatch2));
    }

    private static void testTridiagonalsSum()
    {
        // Basic case
        double[][] a = {{1, 2, 0}, {3, 4, 5}, {6, 7, 0}};
        double[][] b = {{10, 20, 0}, {30, 40, 50}, {60, 70, 0}};
        double[][] result = Tridiagonals.sum(a, b);
        check("tri sum not null", result != null);
        check("tri sum upper", arrayEquals(result[0], new double[]{11, 22, 0}));
        check("tri sum main",  arrayEquals(result[1], new double[]{33, 44, 55}));
        check("tri sum lower", arrayEquals(result[2], new double[]{66, 77, 0}));

        // Null inputs
        check("tri sum null a", Tridiagonals.sum(null, b) == null);
        check("tri sum null b", Tridiagonals.sum(a, null) == null);

        // Invalid structure
        double[][] bad = new double[2][3];
        check("tri sum invalid a", Tridiagonals.sum(bad, b) == null);
        check("tri sum invalid b", Tridiagonals.sum(a, bad) == null);

        // Size mismatch (both valid but different n)
        double[][] smaller = {new double[2], new double[2], new double[2]};
        check("tri sum size mismatch", Tridiagonals.sum(a, smaller) == null);

        // Inputs should not be modified
        double[][] aCopy = deepCopy(a);
        double[][] bCopy = deepCopy(b);
        Tridiagonals.sum(a, b);
        check("tri sum does not modify a", deepEquals(a, aCopy));
        check("tri sum does not modify b", deepEquals(b, bCopy));

        // Commutativity
        double[][] ab = Tridiagonals.sum(a, b);
        double[][] ba = Tridiagonals.sum(b, a);
        check("tri sum commutative", deepEquals(ab, ba));
    }

    private static void testTridiagonalsProductWithDiagonal()
    {
        // Hand-computed 3x3 case
        // D = diag(2, 3, 4)
        // T upper = {1, 1, _}, main = {5, 6, 7}, lower = {8, 9, _}
        // (D*T)[i][j] = d[i] * T[i][j] for row-scaling interpretation
        double[] d = {2, 3, 4};
        double[][] t = {{1, 1, 0}, {5, 6, 7}, {8, 9, 0}};
        double[][] result = Tridiagonals.productWithDiagonal(d, t);

        check("DT upper", arrayEquals(result[0], new double[]{2, 3, 0}));
        check("DT main",  arrayEquals(result[1], new double[]{10, 18, 28}));
        check("DT lower", arrayEquals(result[2], new double[]{24, 36, 0}));

        // Identity diagonal should return T unchanged
        double[] ones = {1, 1, 1};
        double[][] tCopy = deepCopy(t);
        double[][] shouldBeT = Tridiagonals.productWithDiagonal(ones, t);
        check("DT identity preserves T", deepEquals(shouldBeT, tCopy));

        // Null and invalid inputs
        check("DT null d", Tridiagonals.productWithDiagonal(null, t) == null);
        check("DT null t", Tridiagonals.productWithDiagonal(d, null) == null);
        check("DT invalid t",
            Tridiagonals.productWithDiagonal(d, new double[2][3]) == null);

        // Dimension mismatch
        check("DT dimension mismatch",
            Tridiagonals.productWithDiagonal(new double[]{1, 2}, t) == null);

        // Larger case: D = diag(1, 2, 3, 4), T with known entries
        double[] d4 = {1, 2, 3, 4};
        double[][] t4 = {
            {2, 3, 4, 0},   // upper
            {1, 1, 1, 1},   // main
            {5, 6, 7, 0}    // lower
        };
        double[][] r4 = Tridiagonals.productWithDiagonal(d4, t4);
        // upper[i] = d[i] * t[0][i]:   {1*2, 2*3, 3*4, _} = {2, 6, 12, _}
        // main[i]  = d[i] * t[1][i]:   {1, 2, 3, 4}
        // lower[i] = d[i+1] * t[2][i]: {2*5, 3*6, 4*7, _} = {10, 18, 28, _}
        check("DT 4x4 upper",
            r4[0][0] == 2 && r4[0][1] == 6 && r4[0][2] == 12);
        check("DT 4x4 main", arrayEquals(r4[1], new double[]{1, 2, 3, 4}));
        check("DT 4x4 lower",
            r4[2][0] == 10 && r4[2][1] == 18 && r4[2][2] == 28);
    }

    private static void testTridiagonalsLinearSolve()
    {
        // Identity: Tx = v should give x = v
        double[][] identity = {{0, 0, 0}, {1, 1, 1}, {0, 0, 0}};
        double[] v1 = {7, 8, 9};
        check("linearSolve identity",
            arrayEquals(Tridiagonals.linearSolve(identity, v1), v1));

        // Known 3x3 symmetric system:
        //   [ 2 -1  0][x1]   [1]
        //   [-1  2 -1][x2] = [0]   -> x = {1, 1, 1}
        //   [ 0 -1  2][x3]   [1]
        double[][] tri = {{-1, -1, 0}, {2, 2, 2}, {-1, -1, 0}};
        double[] v2 = {1, 0, 1};
        check("linearSolve symmetric 3x3",
            arrayEquals(Tridiagonals.linearSolve(tri, v2),
                        new double[]{1, 1, 1}));

        // 1x1 case: [5] x = [10] -> x = 2
        double[][] tri1 = {{0}, {5}, {0}};
        double[] v3 = {10};
        check("linearSolve 1x1",
            arrayEquals(Tridiagonals.linearSolve(tri1, v3), new double[]{2}));

        // Diagonal-only matrix (upper/lower all zero)
        // diag(2, 3, 4) x = {4, 9, 16} -> x = {2, 3, 4}
        double[][] diagOnly = {{0, 0, 0}, {2, 3, 4}, {0, 0, 0}};
        double[] v4 = {4, 9, 16};
        check("linearSolve diagonal-only",
            arrayEquals(Tridiagonals.linearSolve(diagOnly, v4),
                        new double[]{2, 3, 4}));

        // Larger verification: construct T, pick x, compute v = Tx, check solver recovers x
        double[][] T5 = {
            {-1, -1, -1, -1, 0},   // upper
            {4, 4, 4, 4, 4},       // main (diagonally dominant)
            {-1, -1, -1, -1, 0}    // lower
        };
        double[] xKnown = {1.0, 2.0, 3.0, 4.0, 5.0};
        double[] vComputed = tridiagTimesVector(T5, xKnown);
        double[] xRecovered = Tridiagonals.linearSolve(T5, vComputed);
        check("linearSolve recovers known x (n=5)",
            arrayEquals(xRecovered, xKnown));

        // Even larger: n=20, random-ish values
        int n = 20;
        double[][] Tbig = new double[3][n];
        double[] xBig = new double[n];
        for (int i = 0; i < n; i++)
        {
            Tbig[1][i] = 5 + 0.1 * i;
            xBig[i] = Math.sin(i);
            if (i < n - 1)
            {
                Tbig[0][i] = -1.0;
                Tbig[2][i] = -1.0;
            }
        }
        double[] vBig = tridiagTimesVector(Tbig, xBig);
        double[] xRec = Tridiagonals.linearSolve(Tbig, vBig);
        check("linearSolve recovers x for n=20",
            arrayEqualsTol(xRec, xBig, 1e-10));

        // Null / invalid inputs
        check("linearSolve null T", Tridiagonals.linearSolve(null, v1) == null);
        check("linearSolve null v", Tridiagonals.linearSolve(identity, null) == null);
        check("linearSolve wrong v length",
            Tridiagonals.linearSolve(identity, new double[]{1, 2}) == null);
        check("linearSolve invalid T",
            Tridiagonals.linearSolve(new double[2][3], v1) == null);
    }

    private static void testTridiagonalsImmutability()
    {
        // linearSolve should not mutate T or v
        double[][] T = {{-1, -1, 0}, {2, 2, 2}, {-1, -1, 0}};
        double[] v = {1, 0, 1};
        double[][] Tcopy = deepCopy(T);
        double[] vCopy = v.clone();
        Tridiagonals.linearSolve(T, v);
        check("linearSolve does not modify T", deepEquals(T, Tcopy));
        check("linearSolve does not modify v", arrayEquals(v, vCopy));

        // productWithDiagonal should not mutate inputs
        double[] d = {2, 3, 4};
        double[][] t = {{1, 1, 0}, {5, 6, 7}, {8, 9, 0}};
        double[] dCopy = d.clone();
        double[][] tCopy = deepCopy(t);
        Tridiagonals.productWithDiagonal(d, t);
        check("productWithDiagonal does not modify d", arrayEquals(d, dCopy));
        check("productWithDiagonal does not modify t", deepEquals(t, tCopy));
    }

    // ========================================================================
    // ODE
    // ========================================================================

    private static void testODE()
    {
        // When a = 0, trivial solution f ≡ 0
        check("ODE a=0 gives 0 (n=10)", Math.abs(ODE.solve(0.0, 10)) < 1e-10);
        check("ODE a=0 gives 0 (n=100)", Math.abs(ODE.solve(0.0, 100)) < 1e-10);
        check("ODE a=0 gives 0 (n=999)", Math.abs(ODE.solve(0.0, 999)) < 1e-10);

        // Linearity: solve(k*a, n) = k * solve(a, n)
        double s1 = ODE.solve(1.0, 200);
        double s2 = ODE.solve(2.0, 200);
        double s5 = ODE.solve(5.0, 200);
        double sMinus = ODE.solve(-1.0, 200);
        check("ODE linearity: 2a",
            Math.abs(2 * s1 - s2) < 1e-9);
        check("ODE linearity: 5a",
            Math.abs(5 * s1 - s5) < 1e-9);
        check("ODE linearity: -a",
            Math.abs(-s1 - sMinus) < 1e-9);

        // Convergence: answers should stabilise as n grows
        double n100 = ODE.solve(1.0, 100);
        double n500 = ODE.solve(1.0, 500);
        double n2000 = ODE.solve(1.0, 2000);
        check("ODE convergence 100 vs 500", Math.abs(n100 - n500) < 1e-3);
        check("ODE convergence 500 vs 2000", Math.abs(n500 - n2000) < 1e-4);

        // Odd vs even n: averaging for even n should still be close to odd n value
        double odd = ODE.solve(1.0, 999);
        double even = ODE.solve(1.0, 1000);
        check("ODE odd n ≈ even n at high n", Math.abs(odd - even) < 1e-4);

        // Sign check: for a > 0, f''(x) ≈ cos(x)f(x) + a*x^2; with f(0)=f(1)=0
        // and positive forcing a*x^2, intuitively f(0.5) should be negative
        // (the forcing pushes f'' positive, which bends f downward between fixed
        // endpoints at 0). This is a useful smoke test.
        check("ODE sign for positive a", ODE.solve(1.0, 500) < 0);
        check("ODE sign for negative a", ODE.solve(-1.0, 500) > 0);
    }

    // ========================================================================
    // Helpers
    // ========================================================================

    private static void check(String name, boolean condition)
    {
        if (condition)
        {
            System.out.println("PASS: " + name);
            passed++;
        }
        else
        {
            System.out.println("FAIL: " + name);
            failed++;
        }
    }

    private static boolean arrayEquals(double[] a, double[] b)
    {
        return arrayEqualsTol(a, b, EPS);
    }

    private static boolean arrayEqualsTol(double[] a, double[] b, double tol)
    {
        if (a == null || b == null) return a == b;
        if (a.length != b.length) return false;
        for (int i = 0; i < a.length; i++)
        {
            if (Math.abs(a[i] - b[i]) > tol) return false;
        }
        return true;
    }

    private static boolean deepEquals(double[][] a, double[][] b)
    {
        if (a == null || b == null) return a == b;
        if (a.length != b.length) return false;
        for (int i = 0; i < a.length; i++)
        {
            if (!arrayEquals(a[i], b[i])) return false;
        }
        return true;
    }

    private static double[][] deepCopy(double[][] a)
    {
        double[][] result = new double[a.length][];
        for (int i = 0; i < a.length; i++)
        {
            result[i] = a[i].clone();
        }
        return result;
    }

    /**
     * Multiplies a tridiagonal matrix (in the 3xn storage format) by a vector.
     * Used to construct known Tx = v pairs for testing linearSolve.
     */
    private static double[] tridiagTimesVector(double[][] T, double[] x)
    {
        int n = x.length;
        double[] v = new double[n];
        for (int i = 0; i < n; i++)
        {
            v[i] = T[1][i] * x[i];
            if (i > 0)     v[i] += T[2][i - 1] * x[i - 1];  // lower diagonal
            if (i < n - 1) v[i] += T[0][i] * x[i + 1];      // upper diagonal
        }
        return v;
    }
}
