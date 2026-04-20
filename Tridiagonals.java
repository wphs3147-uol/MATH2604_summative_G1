
/**
 * Contains static methods for working with tridiagonal matrices.
 * The matrix is stored as a 3 x n array, where n is its size.
 * The rows represent:
 *   a[0]: super diagonal (above the main diagonal)
 *   a[1]: main diagonal
 *   a[2]: sub diagonal (below the main diagonal)
 *
 * The entries a[0][n-1] and a[2][n-1] are included in the array
 * but are not used, as they do not correspond to actual matrix values.
 *
 * The class includes methods to check validity, add matrices,
 * multiply with a diagonal matrix, solve systems using the Thomas
 * algorithm and create a fixed example matrix.
 */
class Tridiagonals
{
    /**
     * Creates an example tridiagonal matrix of size n.
     *
     * The matrix is stored as a 3 x n array where:
     * m[0] contains the upper diagonal,
     * m[1] contains the main diagonal,
     * m[2] contains the lower diagonal.
     *
     * The upper diagonal entries are 1, the main diagonal entries
     * are -(i+1)^2, and the lower diagonal entries are i+1.
     *
     * @param n the size of the matrix, which must be greater than 0
     * @return a tridiagonal matrix in 3 x n form
     * @throws IllegalArgumentException if n <= 0
     */
    static double[][] exampleMatrix(int n) 
    {
        if (n<=0) 
        {
            throw new IllegalArgumentException ("n must be >0");
        }

        double [][] m = new double [3][n];

        for (int i = 0; i < n - 1; i++)
        {
            m[0][i] =1;
        }

        for (int i = 0; i < n; i++) 
        {
            m[1][i] = -(i+1) * (i+1);
        }

        for (int i = 0; i < n - 1; i++) 
        {
            m[2][i] = i+1;
        }
        return m;
    }
    /**
     * Checks whether the given array is a valid tridiagonal matrix.
     * 
     * A tridiagonal matrix is stored as a 3-by-n array where:
     * a[0] contains the upper diagonal,
     * a[1] contains the main diagonal,
     * a[2] contains the lower diagonal.
     * 
     * @param a the matrix representation
     * @return true if the structure is valid, false otherwise
     * 
     */
    static boolean isValidTridiagonal(double[][] a)
    {
        if (a == null)
        {
            return false;
        }

        if (a.length != 3)
        {
            return false;
        }

        if (a[0] == null || a[1] == null || a[2] == null)
        {
            return false;
        }

        int n = a[1].length;

        if (n < 1)
        {
            return false;
        }

        if (a[0].length != n || a[2].length != n)
        {
            return false;
        }

        return true;
    }

    /**
     * Adds two tridiagonal matrices.
     *
     * The matrices are stored as 3 x n arrays where:
     * a[0] contains the upper diagonal,
     * a[1] contains the main diagonal,
     * a[2] contains the lower diagonal.
     *
     * If either input is invalid or the matrix sizes do not match,
     * the method returns null.
     *
     * @param a the first tridiagonal matrix
     * @param b the second tridiagonal matrix
     * @return the matrix a + b, or null if the inputs are invalid
     */
    static double[][] sum(double[][] a, double[][] b)
    {
        if (!isValidTridiagonal(a) || !isValidTridiagonal(b))
        {
            return null;
        }

        int n = a[1].length;

        if (b[1].length != n)
        {
            return null;
        }
        
        

        double[][] result = new double[3][n];

        for (int i = 0; i < n; i = i + 1)
        {
            result[0][i] = a[0][i] + b[0][i];
        }

        for (int i = 0; i < n; i = i + 1)
        {
            result[1][i] = a[1][i] + b[1][i];
        }

        for (int i = 0; i < n; i = i + 1)
        {
            result[2][i] = a[2][i] + b[2][i];
        }

        return result;
    }

    /**
     * Solves the linear system Tx = v where T is a tridiagonal matrix.
     *
     * The method uses the Thomas algorithm to find the solution.
     * If the inputs are invalid, the method returns null.
     *
     * @param T the tridiagonal matrix stored as a 3 x n array
     * @param v the right hand side vector
     * @return the solution vector x, or null if the inputs are invalid
     */

    static double[] linearSolve(double[][] T, double[] v)
    {
        if (!isValidTridiagonal(T) || v == null)
        {
            return null;
        }

        int n = T[1].length;

        if (v.length != n)
        {
            return null;
        }

        double[] upper = T[0].clone();
        double[] main = T[1].clone();
        double[] lower = T[2].clone();
        double[] rhs = v.clone();

        for (int i = 1; i < n; i = i + 1)
        {
            double factor = lower[i - 1] / main[i - 1];
            main[i] = main[i] - factor * upper[i - 1];
            rhs[i] = rhs[i] - factor * rhs[i - 1];
        }

        double[] x = new double[n];
        x[n - 1] = rhs[n-1] / main[n - 1];
        
        for (int i = n - 2; i >= 0; i = i - 1)
        {
            x[i] = (rhs[i] - upper[i] * x[i + 1]) / main[i];
        }

        return x;
    }


    /**
     * Multiplies a diagonal matrix D by a tridiagonal matrix T.
     *
     * The diagonal matrix is stored as a one dimensional array and the
     * tridiagonal matrix is stored in 3 x n form. If the inputs are
     * invalid or the sizes do not match, the method returns null.
     *
     * @param d the diagonal entries of D
     * @param t the tridiagonal matrix T
     * @return the tridiagonal matrix D * T, or null if the inputs are invalid
     */
    static double[][] productWithDiagonal(double[] d, double[][] t)
    {
        if (d == null || !isValidTridiagonal(t))
        {
            return null;
        }

        int n = t[1].length;

        if (d.length != n)
        {
            return null;
        }

        double[][] result = new double[3][n];

        // upper diagonal
        for (int i = 0; i < n - 1; i++)
        {
            result[0][i] = d[i] * t[0][i];
        }

        // main diagonal
        for (int i = 0; i < n; i++)
        {
            result[1][i] = d[i] * t[1][i];
        }

        // lower diagonal
        for (int i = 0; i < n - 1; i++)
        {
            result[2][i] = d[i + 1] * t[2][i];
        }

        return result;
    }
}

