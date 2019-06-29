/**
 * A3QAMitsukLavitt7607877
 *
 * COMP 1020 SECTION D01
 * INSTRUCTOR    Safiur Mahdi
 * ASSIGNMENT    Assignment 2, Question B
 * @author       Richard Mitsuk Lavitt, 7607877
 * @version      2019-06-28
 *
 * PURPOSE: Parses and stores sentences from an input file.
 *          Calculates readability index and outputs sentence statistics.
 */

// TODO - get rid of 21 in y
// TODO - comment both assignment files

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

class A3QBMitsukLavitt7607877 {
    public static void main(String[] args) {
        ScatterPlot plotOne = new ScatterPlot();
        ScatterPlot plotTwo = new ScatterPlot();

        plotOne = processPlotFile("a3plot1.txt", plotOne);
        plotTwo = processPlotFile("a3plot2.txt", plotTwo);

        plotOne.addRegressionLine();
        plotTwo.addRegressionLine();

        System.out.println("Plot one: \n");
        plotOne.print();

        System.out.println("\nPlot two: \n");
        plotTwo.print();

        System.out.println("End of processing.");
    }

    private static ScatterPlot processPlotFile(String filename, ScatterPlot plot) {
        BufferedReader input;

        try {
            input = new BufferedReader(new FileReader(filename));
            String currentLine = input.readLine();

            while(currentLine != null) {
                processLine(currentLine, plot);
                currentLine = input.readLine();
            }

            input.close();
        }
        catch (IOException ioe) {
            System.err.println(ioe.getMessage());
            ioe.printStackTrace();
        }

        return plot;
    }

    private static void processLine(String currentLine, ScatterPlot plot) {
        try {
            String[] line     = currentLine.split(" ", 2);
            int x = Integer.parseInt(line[0]);
            int y = Integer.parseInt(line[1]);
            
            plot.addPoint(x, y);
        }
        catch(NumberFormatException e) {
        }
    }
}

class ScatterPlot {
    private Point[][] points = new Point[41][21];

    private float xBar;
    private float yBar;

    private int    n;


    public ScatterPlot() {
        this.xBar = 0;
        this.yBar = 0;

        for(int x = 0; x < 41; x++) {
            for(int y = 0; y < 21; y++) {
                points[x][y] = new Point();
            }
        }
    }

    public void addPoint(int x, int y) {
        if(isValid(x, y)) {
            points[x][y].add();
            n++;
        }
    }

    private static boolean isValid(int x, int y) {
        return x >= 0 && x <= 40 && y >= 1 && y <= 20;
    }

    public void addRegressionLine() {
        int yHat;
        float slope;

        slope = computeSlope();

        for(int x = 0; x < 41; x++) {
            yHat = Math.round(yBar + (slope * (x - xBar)));
            if(yHat > 0 && yHat <= 20) {
                points[x][yHat].addToRegression();
            }
        }
    }
    private float computeSlope() {
        float topSum    = 0;
        float bottomSum = 0;
        float slope;

        computeMeans();

        for(int x = 0; x < 41; x++) {
            for(int y = 0; y < 21; y++) {
                if(points[x][y].hasData()) {
                    topSum += topInstanceValue(x, y);
                    bottomSum += bottomInstanceValue(x);
                }
            }
        }

        return topSum / bottomSum;

                /*
        slope = topSum - (n * xBar * yBar)) 
                /
                (bottomSum - (n * (xBar * xBar)));
                */
    }

    private float topInstanceValue(int x, int y) {
        //float topIValue = (x * y);
        float topIValue = (x * y)  - (n * xBar * yBar);

        return topIValue;
    }

    private float bottomInstanceValue(int x) {
        //float bottomIValue = (x * x);
        float bottomIValue = (x * x) - (n * (xBar * xBar));
        
        return bottomIValue;
    }

    private void computeMeans() {
        int      xSum  = 0,
                 ySum  = 0;

        for(int x = 0; x < 41; x++) {
            for(int y = 0; y < 21; y++) {
                if(points[x][y].hasData()) {
                    xSum += x;
                    ySum += y;
                }
            }
        }

        this.xBar = (float) xSum / n;
        this.yBar = (float) ySum / n;

    }

    public void print() {
        for(int y = 20; y >= 0; y--) {
            System.out.print("\n|");
            for(int x = 0;x < 41; x++) {
                System.out.print(points[x][y].toString());
            }
        }
        System.out.println("\n+-----------------------------------------");
    }
}

class Point {
    boolean hasData = false;
    boolean inRegressionLine = false;

    public Point() {
    }

    public void add() {
        hasData = true;
    }

    public void addToRegression() {
        inRegressionLine = true;
    }

    public boolean hasData() {
        return hasData;
    }

    public String toString() {
        String point;

        if(hasData && inRegressionLine) {
            point = "*";
        }
        else if(inRegressionLine) {
            point = "-";
        }
        else if(hasData) {
            point = "X";
        }
        else {
            point = " ";
        }

        return point;
    }
}