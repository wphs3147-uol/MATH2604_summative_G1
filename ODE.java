import static java.lang.Math.*;

/**
 * Solves the boundary value problem f''(x) = cos(x) * f(x) + a * x^2
 * on the interval [0, 1], with boundary conditions f(0) = 0 and f(1) = 0.
 *
 * The interval is split into n interior points and a tridiagonal system
 * is formed using finite difference approximations. This system is then
 * solved using the Thomas algorithm from the Tridiagonals class.
 *
 * The final output gives an approximation of f(0.5).
 */
class ODE
{
    /**
     * Approximates the value of f(0.5) for the differential equation
     * f''(x) = cos(x) * f(x) + a * x^2
     * with boundary conditions f(0) = 0 and f(1) = 0.
     * 
     * The interval [0,1] is split into n interior grid points. A tridiagonal
     * matrix M and vector are made. The linear system
     * M w = -h^2 v
     * is then solved using Tridiagonals.linearSolve.
     *
     * If x = 0.5 is one of the grid points, that value is returned.
     * Otherwise the average of the two closest grid values is returned.
     *
     * @param a the constant a from the differential equation
     * @param n the number of interior grid points (n > 0)
     * @return an approximation of f(0.5)
     */
    static double solve(double a, int n)
    {
        double h = 1.0 / (n + 1);

        double[][] m = new double[3][n];
        double[] rightHandSide = new double[n];

        //upper diagonal
        for (int i = 0; i < n - 1; i = i + 1)
        {
            m[0][i] = -1.0;
        }

        //main diagonal
        for (int i = 0; i < n; i = i + 1)
        {
            double x = (i + 1) * h;
            m[1][i] = 2.0 + h * h * cos(x);
        }

        // lower diagonal
        for (int i = 0; i < n - 1; i = i + 1)
        {
            m[2][i] = -1.0;
        }

        //Making right hand side vector
        for (int i = 0; i < n; i = i + 1)
        {
            double x = (i + 1) * h;
            double vValue = a * x * x;
            rightHandSide[i] = -h * h * vValue;
        }

        double[] w = Tridiagonals.linearSolve(m, rightHandSide);

        //Return approximation when x = 0.5
        if (n % 2 == 1)
        {
            int middleIndex = (n - 1) / 2;
            return w[middleIndex];
        }
        else
        {
            int leftIndex = (n / 2) - 1;
            int rightIndex = n / 2;
            return (w[leftIndex] + w[rightIndex]) / 2.0;
        }
    }
}