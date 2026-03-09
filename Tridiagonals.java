import static java.lang.Math.*;

class Tridiagonals
{
static double[][] exampleMatrix(int n) 
    {
        if (n<=0) throw new IllegalArgumentException ("n must be >0");

        double [][] m = new double [3][n];

        for (int i =0; i< n - 1; i++) {
            m[0][i] =1;
        }
        for (int i=0; i<n; i++) {
            m[1][i] = -pow (i+1, 2);
        }
        for (int i=0; i<n-1; i++) {
            m[2][i] = i+2;
        }
        return m; 
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
}
