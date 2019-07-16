/**
 * A4MitsukLavitt7607877
 *
 * COMP 1020 SECTION D01
 * INSTRUCTOR    Safiur Mahdi
 * ASSIGNMENT    Assignment 4, Question A and B
 * @author       Richard Mitsuk Lavitt, 7607877
 * @version      2019-07-16
 *
 * PURPOSE: Takes orders for a cafe and calculates prices, etc.
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

class A4MitsukLavitt7607877 {
    public static void main(String[] args) {
        OrderList orders = new OrderList();

        orders.processFile("a4b.txt");

        System.out.println("\nComplete list:\n");
        System.out.println(orders);

        System.out.println("\nSorted list:\n");
        orders.sort();
        System.out.print(orders);

        System.out.println(orders.totals());

        System.out.println("\nEnd of processing.");
    }
}

/**
 * Collection class to keep track of orders in a cafe. Provides helper methods
 * to compute total price, etc.
 */
class OrderList {
    private ArrayList<Order> orders;

    public OrderList() {
        orders = new ArrayList<Order>();
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

    /** 
     * Input is in the format:
     * 
     * Coffee,3,medium
     * Donut,7,0.89,chocolate
     * Pop,5,large,Splat! Cola
     * Sandwich,1,3.89,mystery meat,37-grain whole wheat
     * 
     * This method parses the input line-by-line and creates the appropriate
     * object for each. We assume there are no errors in the input.
     * 
     * The assignment instructions indicated we may need to add methods to
     * ensure that only objects of our Order subclasses may be added to the
     * ArrayList. I've tested adding other classes, and it appears that since
     * the list was defined as ArrayList<Order>, no other classes may be
     * added without throwing an exception.
     */
    private void processLine(String currentLine) {
        String[] order = currentLine.split(",", 5);

        switch(order[0]) {
            case "Coffee":
                orders.add(new Coffee(Integer.parseInt(order[1]), order[2]));
                break;
            case "Pop":
                orders.add(new Pop(Integer.parseInt(order[1]), 
                                   order[2],
                                   order[3]));
                break;
            case "Donut":
                orders.add(new Donut(Integer.parseInt(order[1]), 
                                     Double.parseDouble(order[2]),
                                     order[3]));
                break;
            case "Sandwich":
                orders.add(new Sandwich(Integer.parseInt(order[1]),
                                        Double.parseDouble(order[2]),
                                        order[3],
                                        order[4]));
                break;
        }
    }

    /**
     * Ascending price insertion sort.
     */
    public void sort() {
        for(int i = 0; i < orders.size(); i++) {
            Order order = orders.get(i);
            int j = i - 1;
            while(j >= 0 && order.cheaperThan(orders.get(j))) {
                orders.set(j + 1, orders.get(j));
                j--;
            }
            orders.set(j + 1, order);
        }
    }

    public String toString() {
        String string = listHeader() + "\n";

        for(Order order: orders) {
            string += order + "\n";
        }
        
        string += divider() + "\n";

        return string;
    }


    // totals of various categories plus a grand total price at the end
    public String totals() {
        return totalsRow(Sandwich.class, "sandwich(es)") +
               totalsRow(Donut.class,    "donut(s)") +
               totalsRow(Food.class,     "food(s)") +
               divider() + "\n" +
               totalsRow(Coffee.class,   "coffee(s)") +
               totalsRow(Pop.class,      "pop(s)") +
               totalsRow(Drink.class,    "drink(s)") +
               divider() + "\n" +
               totalsRow(Order.class,    "item(s)") +
               divider() + "\n"; 
    }

    /**
     * Counts given class of objects in orders list
     */
    private int count(Class clazz) {
        int count = 0;

        for(Order order: orders) {
            if(clazz.isInstance(order)) {
                count += order.getQuantity();
            }
        }

        return count;
    }

    // Helper to properly format rows of total item counts
    private String totalsRow(Class clazz, String label) {
        return Order.formatRow(Integer.toString(count(clazz)), 
                               label, 
                               "", 
                               "   total: ", 
                               "$" + orderTypeTotal(clazz)) + "\n";
    }

    /**
     * Calculates the grand total of all items of given class after tax
     */
    private String orderTypeTotal(Class clazz) {
        double total = 0.0;

        for(Order order: orders) {
            if(clazz.isInstance(order)) {
                total += order.totalPrice();
            }
        }

        return Order.formatCents(total);
    }

    // Generates a header row for the output table
    private static String listHeader() {
        return Order.formatRow("Qty", "Item", " @ Price", "", "Total") +
               "\n" + divider();
    }

    // Generates dividers for the output table
    private static String divider() {
        return "-".repeat(Order.rowLength() + 1);
    }
}

abstract class Order {
    private int quantity;
    private double price;

    private static final double TAX_RATE = 0.07;

    public Order(){
    }

    public Order(int quantity) {
        this.quantity = quantity;
    }

    public Order(int quantity, double price) {
        this.quantity = quantity;
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    private double getPrice() {
        return price;
    }

    protected void setPrice(double price) {
        this.price = price;
    }

    public String toString() {
        return formatRow(Integer.toString(quantity), 
                         this.secondColumnString(), 
                         " @ $" + formatCents(price), 
                         ", total: ",
                         "$" + formatCents(totalPrice()));
    }

    /**
     * Items will differ in how they're described; pops have a brand, 
     * sandwiches have bread and filling, etc.
     */
    protected abstract String secondColumnString();

    public boolean cheaperThan(Order order) {
        return totalPrice() < order.totalPrice();
    }

    public double totalPrice() {
        return (price * quantity) + getTax();
    }
    
    public double getTax() {
        return isTaxable() ? calcTax() : 0.0;
    }

    private double calcTax() {
        return getQuantity() * getPrice() * TAX_RATE;
    }

    private boolean isTaxable() {
        return getQuantity() < minimumTaxFreeQuantity();
    }

    // Allows adjustment of whether items are taxed per subclass.
    protected int minimumTaxFreeQuantity() {
        return 0;
    }

    /** 
     * Provides an easy way to format prices to two decimal places.
     */
    public static String formatCents(double price) {
        return String.format("%.2f", price);
    }

    /**
     * Calculates the length of a full row of Order.toString() for use in 
     * table formatting.
     */
    public static int rowLength() {
        int rowLength = 0;

        for(int i = 0; i <= 4; i++) {
            rowLength += String.format(columnFormats(i), "").length();
        }

        return rowLength;
    }

    /** 
     * Adjustable column widths for tabular output.
     */
    private static String columnFormats(int column) {
        String[] formats = { "%4s", "%52s", "%7s", "%9s", "%7s" };

        return formats[column];
    }

    public static String formatRow(String first, String second, String third,
                                   String fourth, String fifth) {

        return String.format(columnFormats(0), first) + 
               String.format(columnFormats(1), second) +
               String.format(columnFormats(2), third) +
               String.format(columnFormats(3), fourth) +
               String.format(columnFormats(4), fifth);
    }
}

// Food has its own abstract class so we can get a count of food items at the
// end.
abstract class Food extends Order {
    public Food(int quantity, double price) {
        super(quantity, price);
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

    @Override
    protected int minimumTaxFreeQuantity() {
        // This should be good enough because an order of over 
        // 2 billion sandwiches is improbable.
        return Integer.MAX_VALUE;
    }

    @Override
    protected String secondColumnString() {
        return filling + " sandwich(es) on " + bread;
    }
}

class Donut extends Food {
    private String flavour;

    public Donut(int quantity, double price, String flavour) {
        super(quantity, price);
        this.flavour = flavour;
    }

    @Override
    protected String secondColumnString() {
        return flavour + " donut(s)";
    }

    @Override
    protected int minimumTaxFreeQuantity() {
        // Donuts are only taxable if there are fewer than 6
        return 6;
    }
}

abstract class Drink extends Order {
    private String size;

    public Drink(int quantity, String size) {
        super(quantity);
        this.size = size;
        setPrice();
    }

    /** 
     * There are 3 sizes each of coffee and pop, but prices differ between the
     * subclasses.
     */
    protected abstract double[] getPrices();

    protected String getSize() {
        return size;
    }

    private void setPrice() {
        double[] prices = getPrices();

        switch(size) {
            case "small":
                setPrice(prices[0]);
                break;
            case "medium":
                setPrice(prices[1]);
                break;
            case "large":
                setPrice(prices[2]);
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

    @Override
    protected double[] getPrices() {
        return new double[] { 1.79, 2.09, 2.49 };
    } 

    @Override
    protected String secondColumnString() {
        return getSize() + " " + brand + " pop(s)";
    }
}

class Coffee extends Drink {
    public Coffee(int quantity, String size) {
        super(quantity, size);
    }

    @Override
    protected double[] getPrices() {
        return new double[] { 1.39, 1.69, 1.99 };
    } 

    @Override
    protected String secondColumnString() {
        return getSize() + " coffee(s)";
    }
}