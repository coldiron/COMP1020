/**
 * A4QBMitsukLavitt7607877
 *
 * COMP 1020 SECTION D01
 * INSTRUCTOR    Safiur Mahdi
 * ASSIGNMENT    Assignment 4, Question B
 * @author       Richard Mitsuk Lavitt, 7607877
 * @version      2019-07-05
 *
 * PURPOSE: Takes orders for a cafe and calculates prices, etc.
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

class A4QBMitsukLavitt7607877 {
    public static void main(String[] args) {
        CafeOrderList orders = new CafeOrderList();

        System.out.println("\nOrder up!\n");

        orders.processFile("a4b.txt");

        System.out.println("\nComplete list:\n");
        System.out.println(orders);

        System.out.println("\nSorted list:\n");
        orders.sort();
        System.out.println(orders);

        System.out.println(orders.totalsToString());

        System.out.println("\nEnd of processing.");
    }
}

/**
 * Collection class to keep track of orders in a cafe. Provides helper methods
 * to compute total price, etc.
 */
class CafeOrderList {
    private ArrayList<CafeOrder> orders;

    public CafeOrderList() {
        orders = new ArrayList<CafeOrder>();
    }

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
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }

        System.out.println("Done.");
    }

    /** Input is in the format:
     * 
     * Coffee,3,medium
     * Donut,7,0.89,chocolate
     * Pop,5,large,Splat! Cola
     * Sandwich,1,3.89,mystery meat,37-grain whole wheat
     * 
     * This method parses the input line-by-line and creates the appropriate
     * object for each. We assume there are no errors in the input.
     */
    private void processLine(String currentLine) {
        String[] order = currentLine.split(",", 5);

        switch(order[0]) {
            case "Coffee":
                orders.add(
                    new Coffee(
                        Integer.parseInt(order[1]), 
                        order[2]
                    )
                );
                break;
            case "Pop":
                orders.add(
                    new Pop(
                        Integer.parseInt(order[1]), 
                        order[2],
                        order[3]
                    )
                );
                break;
            case "Donut":
                orders.add(
                    new Donut(
                        Integer.parseInt(order[1]), 
                        Double.parseDouble(order[2]),
                        order[3]
                    )
                );
                break;
            case "Sandwich":
                orders.add(
                    new Sandwich(
                        Integer.parseInt(order[1]),
                        Double.parseDouble(order[2]),
                        order[3],
                        order[4]
                    )
                );
                break;
        }
    }

    /**
     * Ascending price selection sort.
     */
    public void sort() {
        int minSorted;

        for(int i = 0; i < orders.size(); i++) {
            minSorted = i;

            for(int j = i + 1; j < orders.size(); j++) {
                if(orders.get(j).cheaperThan(orders.get(minSorted))) {
                    minSorted = j;
                }
            }

            swap(i, minSorted);
        }
    }

    private void swap(int i, int j) {
        CafeOrder temp = orders.get(i);

        orders.set(i, orders.get(j));
        orders.set(j, temp);
    }

    public String toString() {
        String string = new String();
        string += listHeader() + "\n";
        for(CafeOrder o: orders) {
            string += o + "\n";
        }
        return string;
    }

    private static String listHeader() {
        return String.format(CafeOrder.columnFormats(0), "Qty") +
               String.format(CafeOrder.columnFormats(1), "Item @") +
               String.format(CafeOrder.columnFormats(2), "Price") +
               String.format(CafeOrder.columnFormats(3), "") +
               String.format(CafeOrder.columnFormats(4), "Total"); 
    }

    public String totalsToString() {
        return String.format("%72s", "Grand total: $") + 
               CafeOrder.formatCents(grandTotal()) + "\n" + 

               String.format("%4s", count(Donut.class)) +
               " total donuts\n" +

               String.format("%4s", count(Sandwich.class)) +
               " total sandwiches\n" +

               String.format("%4s", count(Pop.class)) +
               " total pops\n" +

               String.format("%4s", count(Coffee.class)) +
               " total coffees\n" +

               String.format("%4s", count(CafeOrder.class)) +
               " total items\n"; 
    }

    /**
     * Counts given class of objects in orders list
     * @param c class, in format Klass.class
     * @return integer count of objects found in list
     */
    private int count(Class<?> c) {
        int count = 0;

        for(CafeOrder o: orders) {
            if(c.isInstance(o)) {
                count += o.getQuantity();
            }
        }

        return count;
    }

    /**
     * Calculates the grand total of all items in the list after tax
     */
    private double grandTotal() {
        double total = 0.0;

        for(CafeOrder o: orders) {
            total += o.totalPrice();
        }

        return total;
    }
}

abstract class CafeOrder {
    protected int quantity;
    protected double price;


    public CafeOrder(){
    }

    public CafeOrder(int quantity) {
        this.quantity = quantity;
    }

    public CafeOrder(int quantity, double price) {
        this.quantity = quantity;
        this.price = price;
    }

    public String toString() {
        return String.format(columnFormats(0), quantity) + 
               String.format(columnFormats(1), this.secondColumnString()) +
               String.format(columnFormats(2), "$" + formatCents(price)) +
               String.format(columnFormats(3), ", total: ") +
               String.format(columnFormats(4), "$" + formatCents(totalPrice()));

    }

    /**
     * Delegates the second column in our output to subclasses. Items will
     * differ in how they're described; pops have a brand, sandwiches have
     * bread and filling, etc.
     */
    protected abstract String secondColumnString();

    public boolean cheaperThan(CafeOrder o) {
        return totalPrice() < o.totalPrice();
    }

    public double totalPrice() {
        return (price * quantity) + getTax();
    }

    public int getQuantity() {
        return quantity;
    }

    protected double getTax() {
        return 0.0;
    }

    /** Provides an easy way to format prices to two decimal places */
    public static String formatCents(Object price) {
        return String.format("%.2f", price);
    }

    /** Adjustable column widths for tabular output. This is public because 
     * it's also used in our CafeOrderList class.
     */
    public static String columnFormats(int column) {
        String[] formats = { "%4s", "%52s", "%7s", "%9s", "%6s" };

        return formats[column];
    }
}

abstract class Food extends CafeOrder {
    protected static final double TAX_RATE = 0.07;

    public Food(int quantity, double price) {
        super(quantity, price);
    }

    protected double calcTax() {
        return quantity * price * TAX_RATE;
    }
}

class Sandwich extends Food {
    private String filling;
    private String bread;

    public Sandwich(int quantity, double price, String filling, String bread) {
        super(quantity, price);
        this.filling = filling;
        this.bread = bread;
    }

    protected String secondColumnString() {
        return filling + " sandwich(es) on " + bread + " @";
    }

    public double getTax() {
        return calcTax();
    }
}

class Donut extends Food {
    private String flavour;

    public Donut(int quantity, double price, String flavour) {
        super(quantity, price);
        this.flavour = flavour;
    }

    protected String secondColumnString() {
        return flavour + " donut(s) @";
    }

    public double getTax() {
        double tax = super.getTax();

        // Donuts are only taxable if there are fewer than 6
        if(quantity < 6) {
            tax = calcTax();
        }

        return tax;
    }
}

abstract class Drink extends CafeOrder {
    protected String size;

    public Drink(int quantity, String size) {
        super(quantity);
        this.size = size;
        setPrice();
    }

    /** There are 3 sizes each of coffee and pop, but prices differ.
     *  getPrices() is defined per-subclass.
     */
    protected abstract double[] getPrices();

    private void setPrice() {
        double[] prices = this.getPrices();

        switch(size) {
            case "small":
                price = prices[0];
                break;
            case "medium":
                price = prices[1];
                break;
            case "large":
                price = prices[2];
                break;
        }
    }
}

class Pop extends Drink {
    private String brand;

    public Pop(int quantity, String size, String brand) {
        super(quantity, size);
        this.brand = brand;
    }

    protected double[] getPrices() {
        return new double[] { 1.79, 2.09, 2.49 };
    } 

    protected String secondColumnString() {
        return size + " " + brand + " pop(s)" + " @";
    }
}

class Coffee extends Drink {
    public Coffee(int quantity, String size) {
        super(quantity, size);
    }

    protected double[] getPrices() {
        return new double[] { 1.39, 1.69, 1.99 };
    } 

    protected String secondColumnString() {
        return size + " coffee(s) @";
    }
}