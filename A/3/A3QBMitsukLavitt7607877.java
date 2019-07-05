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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

class A3QBMitsukLavitt7607877 {
    public static void main(String[] args) {
        int[] xRange = new int[] { 0 , 40 };
        int[] yRange = new int[] { 1 , 20 };

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

/**
 * Represents a scatterplot grid of Point objects
 */
class ScatterPlot {
    // grid of points with axes [x][y]
    private Point[][] points;

    // Axis ranges in the format { min, max }
    private int[] xRange, yRange;

    // Number of points in the dataset. Named "n" rather than something
    // more descriptive, "n" is generally the term used in the wider world
    private int n;

    // xBar and yBar are instance vars because they are basic 
    // statistics describing the data on the scatterplot
    // that potential additions to this program might
    // eventually need access to.
    private float xBar, yBar;

    public ScatterPlot() {
    }

    /**
     * Constructs a ScatterPlot of a given size
     * @param xRange { xMin, xMax }
     * @param yRange { yMin, yMax }
     */
    public ScatterPlot(int[] xRange, int[] yRange) {
        this.xRange = xRange;
        this.yRange = yRange;

        this.n = 0;

        // The array is one cell larger in each direction because it's easier
        // to work with; we're able to reference points[xMax][yMax] without
        // going out of bounds
        points = new Point[(xRange[1] + 1)][(yRange[1] + 1)];

        initialize();
    }

    private void initialize() {
        for(int x = xRange[0]; x <= xRange[1]; x++) {
            for(int y = yRange[0]; y <= yRange[1]; y++) {
                points[x][y] = new Point();
            }
        }
    }

    /**
     * Processes a list of coordinates into points on the plot,
     * and adds basic statistics.
     */
    public void processFile(String filename) {
        BufferedReader input;

        System.out.println("Processing " + filename + "...");
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
        System.out.println("Done.");
    }

    private void processLine(String currentLine) {
        try {
            String[] line = currentLine.split(" ", 2);
            int x = Integer.parseInt(line[0]);
            int y = Integer.parseInt(line[1]);
            
            addPoint(x, y);
        }
        catch(NumberFormatException e) {
            System.err.println("Invalid input: \"" + currentLine + "\"");
        }
    }

    /**
     * Marks point at given coords as containing data.
     * @param x
     * @param y
     */
    private void addPoint(int x, int y) {
        if(inRange(x, y)) {
            points[x][y].add();
            n++;
        }
        else {
            System.err.println("Out of range: (" + x + ", " + y + ")");
        }
    }

    private boolean inRange(int x, int y) {
        // Comparators ordered in this manner for readability
        return xRange[0] <= x && x <= xRange[1] &&
               yRange[0] <= y && y <= yRange[1];
    }

    /**
     * Adds least-squares regression line of best fit to plot using formula.
     */
    private void addRegressionLine() {
        int   yHat;
        float slope = computeSlope();

        for(int x = 0; x <= xRange[1]; x++) {
            /* I initially used this method to calculate yHat:

               yHat = Math.round(yBar + (slope * (x - xBar)));

               While the above method provides a more accurate 
               regression line, directly casting to int appears
               to match the example plot in the assignment 
               instructions. */
            yHat = (int) (yBar + (slope * (x - xBar)));

            if(inRange(x, yHat)) {
                points[x][yHat].addLine();
            }
        }
    }

    // Computes the slope of the regression line
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

    // Computes the average of values on both axes.
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

        // Because the plot is drawn line-by-line top-down,
        // we must begin iterating from the highest y-value.
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


/**
 * Represents an individual point on a scatter plot.
 * Wraps a char and provides associated helper methods. */
class Point {
    char point;

    private static final char EMPTY = ' ';
    private static final char DATA  = 'X';
    private static final char LINE = '-';
    private static final char DATA_AND_LINE = '*';


    public Point() {
        point = EMPTY;
    }

    /**
     * Sets the point as containing a data point.
     * If there is already a regression line segment in the point,
     * it marks it as containing both.
     */
    public void add() {
        point = hasLine() ? DATA_AND_LINE : DATA;
    }

    /** 
     * Sets the point as containing a regression line segment.
     * If there is also data in the point, it marks it as containing both.
     */
    public void addLine() {
        point = hasData() ? DATA_AND_LINE : LINE;
    }

    /**
     * Checks if the point contains data.
     */
    public boolean hasData() {
        return point == DATA || point == DATA_AND_LINE; 
    }

    /**
     * Checks if the point belongs to regression line.
     */
    private boolean hasLine() {
        return point == LINE || point == DATA_AND_LINE;
    }

    /**
     * Boxes char in a string to for faster/easier concat when
     * building scatter plot
     * Blank:        " "
     * Data point:   "X"
     * Line segment: "-"
     * Both:         "*"
     */
    public String toString() {
        return String.valueOf(point);
    }
}