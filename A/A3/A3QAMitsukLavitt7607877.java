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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ListIterator;

class A3QAMitsukLavitt7607877 {
    public static void main(String[] args) {
        ShoppingList shoppingList = new ShoppingList();
        ShoppingList purchasedList = new ShoppingList();

        processListFile("a3a.txt", shoppingList, purchasedList);

        System.out.println("End of processing.");
    }

    private static void processListFile(String filename, ShoppingList shoppingList, ShoppingList purchasedList) {
        BufferedReader input;

        try {
            input = new BufferedReader(new FileReader(filename));
            String currentLine = input.readLine();

            while(currentLine != null) {
                processLine(currentLine, shoppingList, purchasedList);
                currentLine = input.readLine();
            }

            input.close();
        }
        catch (IOException ioe) {
            System.err.println(ioe.getMessage());
            ioe.printStackTrace();
        }
    }

    private static void processLine(String currentLine, ShoppingList shoppingList, ShoppingList purchasedList) {
        String[] line     = currentLine.split(",", 3);

        String   command = line[0];
        int      quantity;
        String   name;

        if(line.length > 1) {
            quantity = Integer.parseInt(line[1]);
            name     = line[2];
            switch(command) {
            case "buy":
                purchasedList.buy(quantity, name, shoppingList);
                break;
            case "add":
                shoppingList.add(quantity,name);
                break;
            }
        }
        else if(command.equals("list")) {
            printLists(shoppingList, purchasedList);
        }
        else {
            System.err.println("Invalid command.");
        }
    }

    private static void printLists(ShoppingList shoppingList, ShoppingList purchasedList) {
        System.out.println("Shopping List:");
        shoppingList.print();
        System.out.println();
        System.out.println("Purchased List:");
        purchasedList.print();
        System.out.println();
    }
}

class ShoppingList {
    private ArrayList<Item> list;

    public ShoppingList() {
        list = new ArrayList<Item>();
    }

    public void print() {
        for(Item i : list) {
            System.out.println(i.toString());
        }
    }

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

    private int find(String name) {
        Item currentItem;
        int index = -1;

        ListIterator<Item> items = list.listIterator();
        while(items.hasNext() && index < 0) {
            currentItem = items.next();
            if(currentItem.nameIs(name)) {
                index = items.previousIndex();
            }
        }

        return index;
    }
}
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

    public boolean notNeeded() {
        return quantity < 1;
    }
    public String toString() {
        return quantity + " - " + name;
    }
}