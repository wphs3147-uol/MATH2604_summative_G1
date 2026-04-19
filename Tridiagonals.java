import static java.lang.Math.*;

class Tridiagonals
{
    /**
     * A tridiagonal matrix is stored as a 2D array of size 3 x n
     * m[0] stores the elements that are above the main diagonal of the matrix 
     * m[1] stores the elements that are on the diagonal of the matrix
     * m[2] stores the elements below the main diagonal of the matrix 
     * 
     * this function constructs the tridiagonal matrix where: 
     * all elements above the main diagonal are =1
     * elements on the main  diagonal are = -(i+1)^2
     * elements below the main diagonal are = i+2
     * 
     * n is the size of the matrix which has to be >0
     * this function returns a 3xn array representing a tridiagonal matrix
     * if the input n is zero or negative the code will stop and throw an error.
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
    * Checks whether the given array represents a valid tridiagonal matrix representation
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
     * Computes the sum of two tridiagonal matrices.
     *
     * The matrices are stored in the following format as per the assignment brief:
     * a[0] contains the upper diagonal,
     * a[1] contains the main diagonal,
     * a[2] contains the lower diagonal.
     *
     * The method adds the corresponding entries of the two matrices.
     *
     * If either matrix is null, has the wrong structure, or the sizes
     * are incompatible, the method should return null.
     *
     * The input matrices are not modified.
     *
     * @param a the first tridiagonal matrix
     * @param b the second tridiagonal matrix
     * @return the resulting tridiagonal matrix representing a + b, or null if the inputs are invalid
     */
    static double[][] sum(double[][] a, double[][] b)
    {
        if (a == null || b == null)
        {
            return null;
        }

        if (a.length != 3 || b.length != 3)
        {
            return null;
        }

        if (a[0] == null || a[1] == null || a[2] == null)
        {
            return null;
        }

        if (b[0] == null || b[1] == null || b[2] == null)
        {
            return null;
        }

        int n = a[1].length;

        if (n < 1)
        {
            return null;
        }

        if (a[0].length != n || a[2].length != n)
        {
            return null;
        }

        if (b[0].length != n || b[1].length != n || b[2].length != n)
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
     * Solves the linear system Tx = v where T is a tridiagonal matrix represented in the specified format.
     * Uses the Thomas algorithm (a specialized form of Gaussian elimination for tridiagonal systems) to compute the solution.
     * 
     * @param T the tridiagonal matrix represented as a 3-by-n array
     * @param v the right-hand side vector
     * @return the solution vector x, or null if the inputs are invalid or if the system cannot be solved
     * @assumes T is invertible 
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
 * Computes the product of a diagonal matrix D with a tridiagonal matrix T.
 * D is represented as a double[] containing the diagonal entries.
 * T is represented using the tridiagonal storage format:
 * T[0] = upper diagonal
 * T[1] = main diagonal
 * T[2] = lower diagonal
 *
 * If the inputs are invalid or dimensions do not match, return null.
 * @param d the diagonal matrix
 * @param t the tridiagonal matrix
 * @return the tridiagonal matrix representing D*T
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

