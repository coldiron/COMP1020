/**
 * A2QAMitsukLavitt7607877
 *
 * COMP 1020 SECTION D01
 * INSTRUCTOR    Safiur Mahdi
 * ASSIGNMENT    Assignment 2, Question A
 * @author       Richard Mitsuk Lavitt, 7607877
 * @version      2019-05-21
 *
 * PURPOSE: Tallies  and outputs Olympic medals categorized by event and
 *          by country.
 */

import java.io.*;
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

            outputMedalsByEventType(medals, fileOut);
            outputMedalsByCountry(medals, fileOut);

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

    private static void outputMedalsByEventType(ArrayList<Medal> medals, PrintWriter fileOut) {
        ArrayList<EventType> events = new ArrayList<>();

        for(int i = 0; i < medals.size(); i++) {

            int eventTypeIndex = -1;
            for(int ei = 0; ei < events.size(); ei++) {
                // Broken up for readability
                if(events.get(ei).getName().equals(
                medals.get(i).getEventType())) {
                    eventTypeIndex = ei;
                }
            }

            if(eventTypeIndex == -1) {
                events.add(new EventType(medals.get(i).getEventType()));
                events.get(events.size() - 1).addMedal();
            }
            else {
                events.get(eventTypeIndex).addMedal();
            }
        }

        for(EventType event : events) {
            dualOutputln("Event: " + event.getName(), fileOut);
            dualOutputln("Medals: " + event.getMedals(), fileOut);
        }
    }

    private static void outputMedalsByCountry(ArrayList<Medal> medals, PrintWriter fileOut) {
        ArrayList<Country> countries = new ArrayList<>();

        for(int i = 0; i < medals.size(); i++) {

            int countryIndex = -1;
            for(int c = 0; c < countries.size(); c++) {
                // Broken up for readability
                if(countries.get(c).getName()
                .equals(
                medals.get(i).getCountry())) {
                    countryIndex = c;
                }
            }

            if(countryIndex == -1) {
                countries.add(new Country(medals.get(i).getCountry()));
                countries.get(countries.size() - 1).addMedal();
            }
            else {
                countries.get(countryIndex).addMedal();
            }
        }

        for(Country country : countries) {
            dualOutputln("Country: " + country.getName(), fileOut);
            dualOutputln("Medals: " + country.getMedals(), fileOut);
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

    public void setName(String name) {
        this.name = name;
    }

    public void addMedal() {
        this.medals++;
    }

    public int getMedals() {
        return medals;
    }

    public String getName() {
        return name;
    }
}

class EventType extends MedalCounter {
    public EventType() {
    }

    public EventType(String name) {
        this.setName(name);
    }
}

class Country extends MedalCounter {
    public Country() {
    }

    public Country(String name) {
        this.setName(name);
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