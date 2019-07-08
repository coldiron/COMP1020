public class Exercise8 {
    public static void main(String[] args) {
        System.out.println("Ackermann's Function");
        System.out.println("--------------------");
        System.out.println("A(m, n):");
        System.out.println("If m == 0 ............ -> return n + 1 ............... -> " + new Ackermann(0, 9));
        System.out.println("If m  > 0, and n == 0, -> return A(m - 1, 1) ......... -> " + new Ackermann(2, 0));
        System.out.println("If m  > 0, and n >  0, -> return A(m - 1, A(m, n - 1)) -> " + new Ackermann(2, 1));

        System.out.print("\n" + new AckermannTable(3, 4));

        System.out.println("\nEnd of processing.");
    }
}

class AckermannTable {
    int mMax, nMax;

    public AckermannTable(int mMax, int nMax) {
        this.mMax = mMax;
        this.nMax = nMax;
    }

    public String toString() {u
        return recursiveTable("%5s|", mMax, nMax, 0, 0);
    }

    private static String recursiveTable(String format, int mMax, int nMax, int m, int n) {
        String output = new String();
        
        if(m == 0) {
            output += "Ackermann function table, implemented using recursion\n";
            output += tableDivider(format, nMax);
            output += header(format, nMax);
        }

        output+= tableRow(format, nMax, m, n);
        output+= tableDivider(format, nMax);

        if(m < mMax) {
            output += recursiveTable(format, mMax, nMax, m + 1, n);
        }

        return output;
    }

    private static String tableRow(String format, int nMax, int m, int n) {
        String output = new String();

        if(n == 0) {
            output += String.format(format, "m=" + m);
        }

        output += String.format(format, new Ackermann(m, n).getValue());

        if(n < nMax) {
            output+= tableRow(format, nMax, m, n + 1);
        }

        return output;
    }

    private static String header(String format, int nMax) {
        return recursiveHeader(format, nMax, 0);
    }

    private static String recursiveHeader(String format, int nMax, int n) {
        String output = new String();

        if(n == 0) {
            output += String.format(format, "m\\n");
        }

        output += String.format(format, "n=" + n);

        if(n < nMax) {
            output += recursiveHeader(format, nMax, n + 1);
        }
        else {
            output += tableDivider(format, nMax);
        }

        return output;
    } 

    private static String tableDivider(String format, int nMax) {
        int length = String.format(format, "1").length();
        length = (nMax + 2) * length;

        return "\n" + recursiveRepeat("-", length, 0) + "\n";
    }

    private static String recursiveRepeat(String string, int length, int pos) {
        if(pos < length) {
            string += recursiveRepeat(string, length, pos + 1);
        }

        return string;
    }

}
class Ackermann {
    private int m, n, value;

    public Ackermann(int m, int n) {
        this.m = m;
        this.n = n;
        validate();
        this.value = A(m, n);
    }

    private void validate() {
        if(m < 0 || n < 0) {
            throw new IllegalArgumentException(
                "Ackermann's function requires nonnegative input."
            );
        }
    }

    public int getM() {
        return m;
    }

    public int getN() {
        return n;
    }

    public int getValue() {
        return value;
    }

    public String toString() {
        return "A(" + m + ", " + n + "): " + value;
    }

    protected static int A(int m, int n) {
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