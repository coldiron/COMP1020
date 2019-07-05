/**
 * A4QBMitsukLavitt7607877
 *
 * COMP 1020 SECTION D01
 * INSTRUCTOR    Safiur Mahdi
 * ASSIGNMENT    Assignment 4, Question B
 * @author       Richard Mitsuk Lavitt, 7607877
 * @version      2019-07-01
 *
 * PURPOSE: Parses and stores sentences from an input file.
 *          Calculates readability index and outputs sentence statistics.
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

class A4QBMitsukLavitt7607877 {
    public static void main(String[] args) {
        OrderList orders = new OrderList();

        System.out.println("Order up:\n");

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

    public String totalsToString() {
        return String.format("%73s", "Grand total: $") + String.format("%.2f", grandTotal()) + "\n" +
               String.format("%4s", count(Donut.class))    + " total donuts\n" +
               String.format("%4s", count(Sandwich.class)) + " total sandwiches\n" +
               String.format("%4s", count(Pop.class))      + " total pops\n" +
               String.format("%4s", count(Coffee.class))   + " total coffees\n" +
               String.format("%4s", count(Order.class))    + " total items\n"; 
    }

    private void processLine(String currentLine) {
        String[] order = currentLine.split(",", 5);

        switch(order[0]) {
            case "Coffee":
                orders.add(new Coffee(Integer.parseInt(order[1]), order[2]));
                break;
            case "Pop":
                orders.add(new Pop(Integer.parseInt(order[1]), order[2], order[3]));
                break;
            case "Donut":
                orders.add(new Donut(Integer.parseInt(order[1]), Double.parseDouble(order[2]), order[3]));
                break;
            case "Sandwich":
                orders.add(new Sandwich(Integer.parseInt(order[1]), Double.parseDouble(order[2]), order[3], order[4]));
                break;
        }
    }

    public void sort() {
        Order temp;
        int minSorted;

        for(int i = 0; i < orders.size(); i++) {
            minSorted = i;
            for(int j = i + 1; j < orders.size(); j++) {
                if(orders.get(j).cheaperThan(orders.get(minSorted))) {
                    minSorted = j;
                }
            }
            temp = orders.get(i);
            orders.set(i, orders.get(minSorted));
            orders.set(minSorted, temp);
        }
    }


    public double grandTotal() {
        double total = 0.0;

        for(Order o: orders) {
            total += o.totalPrice();
        }

        return total;
    }

    public int count(Class<?> c) {
        int count = 0;

        for(Order o: orders) {
            if(c.isInstance(o)) {
                count += o.getQuantity();
            }
        }

        return count;
    }

    public static double roundCents(double price) {
        return Math.round(price * 100.0) / 100.0;
    }

    public String toString() {
        String string = new String();
        for(Order o: orders) {
            string += o.toString() + ", total: " +
            String.format("%7s", "$" + String.format("%.2f", o.totalPrice())) + "\n";
        }
        return string;
    }
}

abstract class Order {
    protected int quantity;
    protected double price;

    protected static final String[] COLUMN_FORMATS = { "%4s", "%52s", "%7s" };
    protected static final double TAX_RATE = 0.07;

    public Order(){
    }

    public Order(int quantity) {
        this.quantity = quantity;
    }
    public Order(int quantity, double price) {
        this.quantity = quantity;
        this.price = price;
    }

    public abstract String toString();

    public boolean cheaperThan(Order o) {
        return totalPrice() < o.totalPrice();
    }

    public double totalPrice() {
        return (price * quantity) + getTax();
    }

    public int getQuantity() {
        return quantity;
    }


    public double getTax() {
        double tax = 0.0;
        
        if(this instanceof Sandwich || (this instanceof Donut && quantity < 6)) {
            tax = calcTax();
        }

        return tax;
    }

    private double calcTax() {
        return quantity * price * TAX_RATE;
    }
}

abstract class Food extends Order {

    public Food(int quantity, double price) {
        super(quantity, price);
    }


}

abstract class Drink extends Order {
    protected String size;


    public Drink(int quantity, String size) {
        super(quantity);
        this.size = size;
    }

    protected void setPrice(double[] prices) {
        switch(size) {
            case "small":
                price = prices[0];
            case "medium":
                price = prices[1];
            case "large":
                price = prices[2];
        }
    }
}

class Donut extends Food {
    private String flavour;

    public Donut(int quantity, double price, String flavour) {
        super(quantity, price);
        this.flavour = flavour;
    }

    public String toString() {
        return String.format(COLUMN_FORMATS[0], quantity) +
               String.format(COLUMN_FORMATS[1]," " + flavour + " donut(s) @" ) + 
               String.format(COLUMN_FORMATS[2], "$" + String.format("%.2f", price));
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

    public String toString() {
        return String.format(COLUMN_FORMATS[0], quantity) + 
               String.format(COLUMN_FORMATS[1], filling + " sandwich(es) on " + bread + " @") +
               String.format(COLUMN_FORMATS[2], "$" + String.format("%.2f", price));
    }
}

class Pop extends Drink {
    private String brand;

    private static final double[] POP_PRICES = { 1.79, 2.09, 2.49 };

    public Pop(int quantity, String size, String brand) {
        super(quantity, size);
        this.brand = brand;
        setPrice();
    }

    private void setPrice() {
        super.setPrice(POP_PRICES);
    }

    public String toString() {
        return String.format(COLUMN_FORMATS[0], quantity) + 
               String.format(COLUMN_FORMATS[1], size + " " + brand + " drink(s)" + " @") +
               String.format(COLUMN_FORMATS[2], "$" + String.format("%.2f", price));
    }
}

class Coffee extends Drink {
    private static final double[] COFFEE_PRICES = { 1.39, 1.69, 1.99 };

    public Coffee(int quantity, String size) {
        super(quantity, size);
        setPrice();
    }

    private void setPrice() {
        super.setPrice(COFFEE_PRICES);
    }

    public String toString() {
        return String.format(COLUMN_FORMATS[0], quantity) +
               String.format(COLUMN_FORMATS[1], " " + size + " coffee(s) @") +
               String.format(COLUMN_FORMATS[2], "$" + String.format("%.2f", price));
    }
}