/**
 * A4QAMitsukLavitt7607877
 *
 * COMP 1020 SECTION D01
 * INSTRUCTOR    Safiur Mahdi
 * ASSIGNMENT    Assignment 4, Question A
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

class A4QAMitsukLavitt7607877 {
    public static void main(String[] args) {
        OrderList orders = new OrderList();

        System.out.println("Order up:\n");

        orders.processFile("a4a.txt");

        System.out.println("\nComplete list:\n");

        System.out.println(orders);
        
        System.out.println("Total quantity of donuts: " + orders.count(Donut.class));
        System.out.println("Total quantity of coffees: " + orders.count(Coffee.class));
        System.out.println("Grand total for all orders: $" + OrderList.roundCents(orders.grandTotal()));
        
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

    private void processLine(String currentLine) {
        String[] order = currentLine.split(",", 4);

        if(order[0].equals("Coffee")) {
            orders.add(new Coffee(Integer.parseInt(order[1]), order[2]));
        }
        else if(order[0].equals("Donut")) {
            orders.add(new Donut(Integer.parseInt(order[1]), Double.parseDouble(order[2]), order[3]));
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
                count++;
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
            string += o.toString() + ", total: $" + roundCents(o.totalPrice()) + "\n";
        }
        return string;
    }
}

abstract class Order {
    protected int quantity;
    protected double price;

    protected static final double TAX_RATE = 0.07;

    public Order(int quantity) {
        this.quantity = quantity;
    }
    public Order(int quantity, double price) {
        this.quantity = quantity;
        this.price = price;
    }

    public abstract String toString();

    public double totalPrice() {
        return (price * quantity) + getTax();
    }

    public double getTax() {
        return 0.0;
    }
}

class Coffee extends Order {
    private String size;

    public Coffee(int quantity, String size) {
        super(quantity);
        this.size = size;
        setPrice();
    }

    private void setPrice() {
        switch(size) {
            case "small":
                price = 1.39;
            case "medium":
                price = 1.69;
            case "large":
                price = 1.99;
        }
    }

    public String toString() {
        return quantity + " " + size + " coffee @ " + price;
    }
}

class Donut extends Order {
    private String flavour;

    public Donut(int quantity, double price, String flavour) {
        super(quantity, price);
        this.flavour = flavour;
    }

    public String toString() {
        return quantity + " " + flavour + " donut @ " + price;
    }

    public double getTax() {
        if(quantity < 6) {
            return quantity * price * TAX_RATE;
        }
        else {
            return super.getTax();
        }
    }
}