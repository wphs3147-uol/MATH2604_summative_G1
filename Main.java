public class Main
{
    public static void main(String[] args)
    {
        // Test Diagonals
        double[] a = {1, 2, 3};
        double[] b = {4, 5, 6};

        double[] sum = Diagonals.sum(a, b);
        double[] product = Diagonals.product(a, b);

        System.out.println("Sum:");
        for (double x : sum) System.out.print(x + " ");
        System.out.println();

        System.out.println("Product:");
        for (double x : product) System.out.print(x + " ");
        System.out.println();

        // Test Tridiagonal
        double[][] T = Tridiagonals.exampleMatrix(3);

        System.out.println("Tridiagonal main diagonal:");
        for (double x : T[1]) System.out.print(x + " ");
        System.out.println();

        // Test ODE
        double result = ODE.solve(1.0, 5);
        System.out.println("ODE result: " + result);
    }
}