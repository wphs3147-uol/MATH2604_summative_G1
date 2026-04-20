
/**
 * Provides static methods for performing operations on diagonal matrices.
 * A diagonal matrix is represented as a one dimensional array of type double,
 * where each element corresponds to an entry on the main diagonal of the matrix.
 * For an n x n diagonal matrix, the array has length n and stores the values
 * A(0,0), A(1,1), ..., A(n-1,n-1).
 *
 * Supported operations include computing the sum, product and inverse
 * of diagonal matrices, as well as returning a fixed example matrix.
 */
class Diagonals
{
    /**
     * Returns the diagonal entries of a 5x5 example diagonal matrix
     * 
     * The example matrix is: diag(10, 8, 5, -10, 7)
     * 
     * @return a double array containing the diagonal entries of the example matrix.
     */
    public static double[] exampleMatrix()
    {
        double[] matrix = {10.0, 8.0, 5.0, -10.0, 7.0};
        return matrix;
    }

    /**
    * Computes the inverse of a diagonal matrix.
    *
    * The matrix is represented by an array whose entries are the values
    * on the main diagonal. The inverse is found by taking the reciprocal
    * of each diagonal entry.
    *
    * If the input array is null, the method returns null.
    * It is assumed that the matrix is invertible.
    *
    * @param a the diagonal entries of the matrix
    * @return an array representing the inverse diagonal matrix, or null if the input is null
    */
    public static double[] inverse(double[] a)
    {
        if (a == null)
        {
            return null;
        }
        int n = a.length;
        double[] result = new double[n];
        for (int i = 0; i < n; i = i + 1)
        {
            double value = a[i];
            double inverseValue = 1.0/value;
            result[i] = inverseValue;
        }
        return result;
    }
/** 
* Part B - sum of two diagonal matrices
* Diagonal matrix is stored as a one dimensional array, each elements represents a diagonal entry 
* In order to compute the sum, I added the corresponding diagonal entries from each array (a and b)
* The result is a new array with the diagonal values summed 

* a= first diagonal matrix (array)
* b = second diagonal matrix (array) 
* returns a new array with summed diagonal values, or null if the inputs are null or not the same length

*/
    public static double[] sum(double[] a, double[] b)
    {
        if (a == null || b == null || a.length != b.length)
        {
            return null;
        }
        
        if (a.length==0)
        {
            return new double [0];
        }

        int n = a.length;
        double[] result = new double[n];

        for (int i = 0; i < n; i = i + 1)
        {
            result[i] = a[i] + b[i];
        }

        return result;
    }
/** 
 * Part C- product of two diagonal matrices
 * Diagonal matrix is stored as a one dimensional array, each element represents a diagonal entry
 * of the matrix
 * 
 * When computing the product of two diagonal matrices, only diagonal elements are used. 
 * The outputted diagonal element is the product of the corresponding elements from the
 * input arrays (a and b)
 * 
 * a= first diagonal matrix (array)
 * b= second diagonal matrix (array)
 * returns a new array with the product of input diagonal matrices or null if inputs are null or 
 * if a and b are different lengths  
 */

    public static double[] product(double[] a, double[] b)
    {
        if (a == null || b == null || a.length != b.length)
        {
            return null;
        }

        if (a.length ==0)
        {
            return new double [0];
        }

        int n = a.length;
        double[] result = new double[n];

        for (int i = 0; i < n; i = i + 1)
        {
            result[i] = a[i] * b[i];
        }

        return result;
    }
}