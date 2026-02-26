public class Diagonals {

    public static double[] sum(double[] A, double[] B) {
        if (A == null || B == null) {
            return null;
        }

        if (A.length != B.length) {
            return null;
        }

        double[] result = new double[A.length];
        for (int i = 0; i < A.length; i++) {
            result[i] = A[i] + B[i];
        }

        return result;
    }

}




