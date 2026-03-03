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
}
