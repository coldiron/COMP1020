public class E8 {
    public static void main(String[] args) {
        System.out.println("Ackermann's Function");
        System.out.println("--------------------");
        System.out.println("A(m, n):");
        System.out.println("If m == 0 ............ -> return n + 1");
        System.out.println("If m  > 0, and n == 0, -> return A(m - 1, 1)");
        System.out.println("If m  > 0, and n >  0, -> return A(m - 1, A(m, n - 1))");

        System.out.println("A(3, 2): " + A(3, 2));
        System.out.println("A(2, 3): " + A(2, 3));
        System.out.println("A(1, 3): " + A(1, 3));
        System.out.println("A(3, 1): " + A(3, 1));

        System.out.println("\nEnd of processing.");
    }
    private static int A(int m, int n) {
        int functionReturn = -1; 

        if(m == 0) {
            functionReturn = n+1;
        }
        else if(m > 0 && n == 0) {
            functionReturn = A(m - 1, 1);
        }
        else if(m > 0 && n > 0) {
            functionReturn = A(m - 1, A(m, n - 1));
        }

        return functionReturn;
    }
}