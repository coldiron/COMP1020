/**
 * A2QAMitsukLavitt7607877
 *
 * COMP 1020 SECTION D01
 * INSTRUCTOR    Safiur Mahdi
 * ASSIGNMENT    Assignment 2, Question A
 * @author       Richard Mitsuk Lavitt, 7607877
 * @version      2019-06-07
 *
 * PURPOSE: Tallies  and outputs Olympic medals categorized by event and
 *          by country.
 */

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

class A2QAMitsukLavitt7607877 {
    public static void main(String args[]) {
        ArrayList<Medal> medals = new ArrayList<>();

        medals = loadMedals("a2a.txt");

        outputMedals(medals, "a2q1out.txt");

        System.out.println("End of processing.");
    }

    private static void outputMedals(ArrayList<Medal> medals, String filename) {
        try {
            PrintWriter fileOut = new PrintWriter(new FileWriter(filename));

            outputMedalCounter(medals, fileOut, "Event Type");
            outputMedalCounter(medals, fileOut, "Country");

            fileOut.close();
        }
        catch(IOException ioe) {
            System.err.println(ioe.getMessage());
            ioe.printStackTrace();
        }
    }

    /**
     * PURPOSE: Outputs a string to both a file and stdout with a newline
     * 
     * @param output String to be output
     * @param fileOut file to be output to
     */
    private static void dualOutputln(String output, PrintWriter fileOut) {
        System.out.println(output);
        fileOut.println(output);
    }


    private static void outputMedalCounter(ArrayList<Medal> medals, PrintWriter fileOut, String outputType) {
        ArrayList<MedalCounter> medalCounters = new ArrayList<>();

        for(int i = 0; i < medals.size(); i++) {
            String name;

            if(outputType.equals("Event Type")) {
                name = medals.get(i).getEventType();
            }
            else {
                name = medals.get(i).getCountry();
            }

            int medalCounterIndex = -1;
            for(int c = 0; c < medalCounters.size(); c++) {
                if(medalCounters.get(c).getName().equals(name)) {
                    medalCounterIndex = c;
                }
            }

            if(medalCounterIndex == -1) {
                medalCounters.add(new MedalCounter(name));
                medalCounters.get(medalCounters.size() - 1).addMedal();
            }
            else {
                medalCounters.get(medalCounterIndex).addMedal();
            }
        }

        for(MedalCounter medalCounter : medalCounters) {
            dualOutputln(outputType + ": " + medalCounter.getName(), fileOut);
            dualOutputln("Medals: " + medalCounter.getMedals(), fileOut);
        }
    }


    private static ArrayList<Medal> loadMedals(String filename) {
        BufferedReader input;
        String lineContents;
        int lineNumber = 0;
        int medalNumber = 0;
        ArrayList<Medal> medalList = new ArrayList<>();


        medalList.add(new Medal());
        try {
            input = new BufferedReader(new FileReader(filename));

            lineContents = input.readLine();
            while(lineContents != null) {

                if(medalList.size() < medalNumber + 1) {
                    medalList.add(new Medal());
                }

                updateMedal(medalList.get(medalNumber), lineContents, lineNumber);

                if(lineNumber == 2) {
                    lineNumber = 0;
                    medalNumber++;
                }
                else {
                    lineNumber++;
                }

            lineContents = input.readLine();
            }
            input.close();
        }
        catch (IOException ioe) {
            System.err.println(ioe.getMessage());
            ioe.printStackTrace();
        }

        return medalList;
    }

    private static void updateMedal(Medal medal, String lineContents, int lineNumber) {
        switch(lineNumber) {
            case 0:
                medal.setCountry(lineContents);
            case 1:
                medal.setEventType(lineContents);
            case 2:
                medal.setEventName(lineContents);
        }
    }
}

class MedalCounter { 
    private String name = "";
    private int medals = 0;

    public MedalCounter() {
    }

    public MedalCounter(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addMedal() {
        medals++;
    }

    public int getMedals() {
        return medals;
    }

    public String getName() {
        return name;
    }
}

class Medal {
    private String country   = "";
    private String eventName = "";
    private String eventType = "";

    public Medal() {
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getCountry() {
        return country;
    }

    public String getEventName() {
        return eventName;
    }

    public String getEventType() {
        return eventType;
    }
}