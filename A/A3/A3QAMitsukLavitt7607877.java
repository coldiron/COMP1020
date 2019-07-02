/**
 * A3QAMitsukLavitt7607877
 *
 * COMP 1020 SECTION D01
 * INSTRUCTOR    Safiur Mahdi
 * ASSIGNMENT    Assignment 3, Question A
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

class A3QAMitsukLavitt7607877 {
    public static void main(String[] args) {
        ListKeeper lists = new ListKeeper();

        lists.processFile("a3a.txt");

        System.out.println("End of processing.");
    }
}

/**
 * Helper class to keep track of two ShoppingLists: one for needed items,
 * one for purchased items.
 */
class ListKeeper {
    private ShoppingList shoppingList;
    private ShoppingList purchaseList;

    public ListKeeper() {
        shoppingList = new ShoppingList();
        purchaseList = new ShoppingList();
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

    // Processes commands from file: List, add, or buy.
    // Buy adds to 
    private void processLine(String currentLine) {
        String[] line     = currentLine.split(",", 3);

        String   command = line[0];

        if(line.length > 1) {
            try {
                int quantity = Integer.parseInt(line[1]);
                String name  = line[2];

                switch(command) {
                case "buy":
                    purchaseList.buy(quantity, name, shoppingList);
                    break;
                case "add":
                    shoppingList.add(quantity, name);
                    break;
                default:
                    System.err.println("Invalid command: " + command);
                }
            }
            catch(NumberFormatException e) {
                System.err.println(e.getMessage());
                e.printStackTrace();
            }
        }
        else if(command.equals("list")) {
            System.out.println(this);
        }
        else {
            System.err.println("Invalid command: " + command);
        }
    }

    public String toString() {
        return "\n" 
             + "Shopping List:"
             + shoppingList
             + "\nPurchase List:"
             + purchaseList;
    }
}

class ShoppingList {
    private ArrayList<Item> list;

    public ShoppingList() {
        list = new ArrayList<Item>();
    }

    public String toString() {
        String string = "\n";

        for(Item i : list) {
            string += i + "\n";
        }
        
        return string;
    }

    /**
     * Adds an item to the list of purchases. Checks if our list of needed items
     * contains the same item, and adjusts it accordingly.
     * @param quantity
     * @param name
     * @param shoppingList List of needed items
     */
    public void buy(int quantity, String name, ShoppingList shoppingList) {
        int index;
        
        index = this.find(name);
        if(index >= 0) {
            list.get(index).addQuantity(quantity);
        }
        else {
            list.add(new Item(quantity, name));
        }

        index = shoppingList.find(name);
        if(index >= 0) {
            Item item = shoppingList.list.get(index);

            item.removeQuantity(quantity);

            if(item.notNeeded()) {
                shoppingList.list.remove(item);
            }
        }
    }

    public void add(int quantity, String name) {
        int index = this.find(name);
        if(index >= 0) {
            list.get(index).addQuantity(quantity);
        }
        else {
            list.add(new Item(quantity, name));
        }
    }

    /**
     * @param name
     * @return The index at which a given item is located, or -1 if it isn't found
     */
    private int find(String name) {
        int index = -1;

        int i = 0;
        while(i < list.size() && index < 0) {
            if(list.get(i).nameIs(name)) {
                index = i;
            }
            i++;
        }

        return index;
    }
}

/** 
 * Represents an individual item on a shopping list.
 */
class Item {
    private String name;
    private int quantity;

    public Item() {
    }

    public Item(int quantity, String name) {
        this.name = name;
        this.quantity = quantity;
    }

    public boolean nameIs(String name) {
        return this.name.equals(name);
    }

    public void addQuantity(int quantity) {
        this.quantity += quantity;
    }
    
    public void removeQuantity(int quantity) {
        this.quantity -= quantity;
    }

    // If the number of items on a list are 0 or less, 
    // it can be removed.
    public boolean notNeeded() {
        return quantity < 1;
    }
    public String toString() {
        return quantity + " - " + name;
    }
}