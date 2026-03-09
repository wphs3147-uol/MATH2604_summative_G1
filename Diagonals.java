import static java.lang.Math.*;
class Diagonals
{
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