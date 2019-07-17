/**
 * A5QAMitsukLavitt7607877
 *
 * COMP 1020 SECTION D01
 * INSTRUCTOR    Safiur Mahdi
 * ASSIGNMENT    Assignment 5, Question A
 * @author       Richard Mitsuk Lavitt, 7607877
 * @version      2019-07-17
 *
 * PURPOSE: Prints a triangle from text using recursion 
 */

import java.util.Scanner;

class A5QAMitsukLavitt7607877 {
    public static void main(String[] args) {
        Scanner keyboard = new Scanner(System.in);

        getInput(keyboard);

        keyboard.close();

        System.out.println("\nEnd of processing.");
    }

    private static void getInput(Scanner keyboard) {
        System.out.println("\nEnter a string, 'quit' to exit");
        String input = keyboard.nextLine();

        if(!input.equals("quit")) {
            System.out.println();
            trianglePrint(input);
            getInput(keyboard);
        }
    }

    private static void trianglePrint(String s) {
        int first = 0;
        int last = s.length() - 1;
        String beginningOfLine = "" + s.charAt(first);
        String endOfLine = "";
        
        if(s.length() % 2 == 0) {
            endOfLine += s.charAt(last);
            last--;
        }

        centeredPrintln(beginningOfLine + endOfLine, s.length());

        if((beginningOfLine + endOfLine).length() < s.length()) {
            trianglePrintRecursive(s, beginningOfLine, endOfLine, first + 1, last);
        }
    }

    private static void trianglePrintRecursive(String s, String beginningOfLine, String endOfLine, int first, int last) {
        beginningOfLine += s.charAt(first);
        endOfLine = s.charAt(last) + endOfLine;

        centeredPrintln(beginningOfLine + endOfLine, s.length());

        if((beginningOfLine + endOfLine).length() < s.length()) {
            trianglePrintRecursive(s, beginningOfLine, endOfLine, first + 1, last - 1);
        }
    }

    private static void centeredPrintln(String toPrint, int totalLength) {
        int lengthRemaining = toPrint.length() + 
                              ((totalLength - toPrint.length()) / 2);

        System.out.println(String.format("%-" + totalLength + "s",
                           String.format("%" + lengthRemaining + "s",
                           toPrint)));
    }
}
