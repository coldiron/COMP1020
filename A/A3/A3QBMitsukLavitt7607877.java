/**
 * A3QBMitsukLavitt7607877
 *
 * COMP 1020 SECTION D01
 * INSTRUCTOR    Safiur Mahdi
 * ASSIGNMENT    Assignment 3, Question B
 * @author       Richard Mitsuk Lavitt, 7607877
 * @version      2019-07-01
 *
 * PURPOSE: Plots points on scatter plots, calculates basic statistics,
 *          and prints plots complete with a least-squares regression line of
 *          best fit.
 */

// TODO - comment both assignment files

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

class A3QBMitsukLavitt7607877 {
    public static void main(String[] args) {
        int[] xRange = new int[] {0, 40};
        int[] yRange = new int[] {1, 20};

        ScatterPlot plotOne = new ScatterPlot(xRange, yRange);
        ScatterPlot plotTwo = new ScatterPlot(xRange, yRange);

        plotOne.processFile("a3plot1.txt");
        plotTwo.processFile("a3plot2.txt");

        System.out.println("\nPlot one:");
        System.out.println(plotOne);

        System.out.println("\nPlot two:");

        System.out.println(plotTwo);

        System.out.println("\nEnd of processing.");
    }
}

class ScatterPlot {
    private Point[][] points;

    private int[] xRange, yRange;
    private int n;

    // xBar and yBar are instance vars because they are basic 
    // statistics that potential additions to this program might
    // need access to.
    private float xBar, yBar;



    public ScatterPlot(int[] xRange, int[] yRange) {
        this.xRange = xRange;
        this.yRange = yRange;

        this.n = 0;

        points = new Point[(xRange[1] + 1)][(yRange[1] + 1)];

        for(int x = xRange[0]; x <= xRange[1]; x++) {
            for(int y = yRange[0]; y <= yRange[1]; y++) {
                points[x][y] = new Point();
            }
        }
    }

    public void processFile(String filename) {
        BufferedReader input;

        try {
            input = new BufferedReader(new FileReader(filename));
            String currentLine = input.readLine();

            while(currentLine != null) {
                processLine(currentLine);
                currentLine = input.readLine();
            }

            input.close();

            computeMeans();
            addRegressionLine();
        }
        catch (IOException ioe) {
            System.err.println(ioe.getMessage());
            ioe.printStackTrace();
        }
    }

    private void processLine(String currentLine) {
        try {
            String[] line = currentLine.split(" ", 2);
            int x = Integer.parseInt(line[0]);
            int y = Integer.parseInt(line[1]);
            
            addPoint(x, y);
        }
        catch(NumberFormatException e) {
        }
    }

    private void addPoint(int x, int y) {
        if(inRange(x, y)) {
            points[x][y].add();
            n++;
        }
    }

    private boolean inRange(int x, int y) {
        return xRange[0] <= x && x <= xRange[1] &&
               yRange[0] <= y && y <= yRange[1];
/*         return x >= xRange[0] && 
               x <= xRange[1] && 
               y >= yRange[0] && 
               y <= yRange[1]; */
    }

    private void addRegressionLine() {
        int     yHat;
        float   slope = computeSlope();

        for(int x = 0; x <= xRange[1]; x++) {
            /* I initially used this method to calculate yHat:

               yHat = Math.round(yBar + (slope * (x - xBar)));

               While the above method provides a more accurate 
               regression line, directly casting to int appears
               to better match the example in the assignment 
               instructions. */
            yHat = (int) (yBar + (slope * (x - xBar)));

            if(inRange(x, yHat)) {
                points[x][yHat].addToRegression();
            }
        }
    }

    private float computeSlope() {
        float topSum    = 0;
        float bottomSum = 0;


        for(int x = xRange[0]; x <= xRange[1]; x++) {
            for(int y = yRange[0]; y <= yRange[1]; y++) {
                if(points[x][y].hasData()) {
                    topSum += x * y;
                    bottomSum += x * x;
                }
            }
        }

        return (topSum - (n * xBar * yBar)) 
               / 
               (bottomSum - (n * xBar * xBar));

    }

    private void computeMeans() {
        int   xSum = 0,
              ySum = 0;

        for(int x = xRange[0]; x <= xRange[1]; x++) {
            for(int y = yRange[0]; y <= yRange[1]; y++) {
                if(points[x][y].hasData()) {
                    xSum += x;
                    ySum += y;
                }
            }
        }

        xBar = (float) xSum / n;
        yBar = (float) ySum / n;
    }


    public String toString() {
        String string = new String();

        for(int y = yRange[1]; y >= yRange[0]; y--) {

            // Add y-axis
            string += "\n|";

            for(int x = xRange[0];x <= xRange[1]; x++) {
                string += points[x][y];
            }
        }

        // Add x-axis
        string += "\n+";
        for(int x = xRange[0]; x <= xRange[1]; x++) {
            string +="-";
        }

        return string;
    }
}

class Point {
    private int type;

    public Point() {
        type = 0;
    }

    public void add() {
        type = (type > 1) ? 3 : 1;
    }

    public void addToRegression() {
        type = (hasData()) ? 3 : 2;
    }

    // If type is odd, the point contains data
    public boolean hasData() {
        return (type & 1) == 1;
    }

    public String toString() {
        String point = new String();

        switch(type) {
            case 0:
                point = " ";
                break;
            case 1:
                point = "X";
                break;
            case 2: 
                point = "-";
                break;
            case 3:
                point = "*";
        }

        return point;
    }
}