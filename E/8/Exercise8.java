public class Exercise8 {
    public static void main(String[] args) {
        System.out.println("Ackermann's Function");
        System.out.println("--------------------");
        System.out.println("A(m, n):");
        System.out.println("If m == 0,             => return n + 1");
        System.out.println("If m  > 0, and n == 0, => return A(m - 1, 1)");
        System.out.println("If m  > 0, and n >  0, => return A(m - 1, A(m, n - 1))\n");


        System.out.print(Ackermann.table(3, 4));
        System.out.println("\nEnd of processing.");
    }
}

class Ackermann {

    public static String table(int mMax, int nMax) {
        return recursiveTable("%5s|", mMax, nMax, 0, 0);
    }

    private static String recursiveTable(String format, int mMax, int nMax, int m, int n) {
        String output = new String();
        
        if(m == 0) {
            output += "Table format (rows m, columns n):\n";
            output += tableDivider(format, nMax);
            output += tableHeader(format, nMax);
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
            output += String.format(format, m);
        }

        output += String.format(format, ackermannFunction(m, n));

        if(n < nMax) {
            output+= tableRow(format, nMax, m, n + 1);
        }

        return output;
    }

    private static String tableHeader(String format, int nMax) {
        return recursiveTableHeader(format, nMax, 0);
    }

    private static String recursiveTableHeader(String format, int nMax, int n) {
        String output = new String();


        if(n == 0) {
            output += String.format(format, "M\\N");
        }

        output += String.format(format, n);

        if(n < nMax) {
            output += recursiveTableHeader(format, nMax, n + 1);
        }
        else {
            output += tableDivider(format, nMax);
        }

        return output;
    } 

    private static String tableDivider(String format, int nMax) {
        int length = String.format(format, "1").length();
        length = (nMax + 2) * length;

        return "\n" + recursiveTableDivider("", length, 0);
    }

    private static String recursiveTableDivider(String divider, int length, int pos) {
        divider = "-";

        if(pos < length) {
            divider += recursiveTableDivider(divider, length, pos + 1);
        }
        else {
            divider += "\n";
        }
        return divider;
    }

    private static int ackermannFunction(int m, int n) {
        int functionReturn = 0;
        if(m >= 0 && n >= 0) {
            if(m == 0) {
                functionReturn = n+1;
            }
            else if(m > 0 && n == 0) {
                functionReturn = ackermannFunction(m - 1, 1);
            }
            else if(m > 0 && n > 0) {
                functionReturn = ackermannFunction(m - 1, ackermannFunction(m, n - 1));
            }
        }
        else {
            System.err.println("Ackermann's function requires nonnegative input.");
        }
        return functionReturn;
    }

}