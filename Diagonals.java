import static java.lang.Math.*;
class Diagonals
{
    /**
     * Part A) Implement example diagonal matrix.
     * The example matrix is: [10, 8, 5, -10, 7]
     * @return the diagonal entries of the example matrix
     */
    public static double[] exampleMatrix()
    {
        double[] matrix = {10, 8, 5, -10, 7};
        return matrix;
    }

    /**
     * Computes the inverse of a diagonal matrix
     *
     * The matrix is represented by an array where each element
     * corresponds to a value on the diagonal. The inverse of a
     * diagonal matrix is calculated by taking the reciprocal of
     * each diagonal entry.
     *
     * If the input array is null then the method returns null.
     *
     * @param a the array representing the diagonal entries
     * @return an array representing the diagonal entries of the inverse matrix
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
}



