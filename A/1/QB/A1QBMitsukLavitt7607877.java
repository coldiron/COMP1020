/**
 * A1QBMitsukLavitt7607877
 *
 * COMP 1020 SECTION D01
 * INSTRUCTOR    Safiur Mahdi
 * ASSIGNMENT    Assignment 1, Question B
 * @author       Richard Mitsuk Lavitt, 7607877
 * @version      2019-05-22
 *
 * PURPOSE: Simple date book.
 */
public class A1QBMitsukLavitt7607877 {

    public static void main(String[] args) {
        Event[] september = new Event[30];
        Event[] october   = new Event[31];

        // Calculate the first day of each month
        int firstOfSeptember = (int)(Math.random() * 7);
        int firstOfOctober   = (firstOfSeptember + 30) % 7;

        initializeEvents(september);
        initializeEvents(october);

        // Set 10 random events per month
        fillRandomEvents(september);
        fillRandomEvents(october);

        printAllViews(september, firstOfSeptember, "September");
        printAllViews(october, firstOfOctober, "October");

        System.out.println();
    }

    private static void printAllViews(Event[] month, int firstOfMonth, String monthName) {
        System.out.println();

        System.out.println(monthName);
        System.out.println("_________________________________");
        printCalendar(month, firstOfMonth);

        System.out.println();

        System.out.println("Events:");
        printList(month);

        System.out.println();

        System.out.println("High-priority events:");
        printPriorityList(month);

        System.out.println();
    }

    private static void printPriorityList(Event[] month) {
        for(int i = 0; i < month.length; i++) {
            if(month[i].getPriority() == 3) {
                int day = i + 1;
                System.out.println(day + ": " + month[i].getName());
            }
        }
    }

    private static void printList(Event[] month) {
        for(int i = 0; i < month.length; i++) {
            if(!month[i].isEmpty()) {
                System.out.println((i + 1) + ": " + month[i].toString());
            }
        }
    }
    private static void printCalendar(Event[] month, int firstOfMonth) {
        System.out.println("Sun  Mon  Tue  Wed  Thu  Fri  Sat");

        // Add space to fill the week until the first of the month.
        for(int i = 0; i < firstOfMonth; i++) {
            System.out.print("     ");
        }

        for(int i = 0; i < month.length; i++) {
            // We use both an integer and string version of the day of the month
            // because we need to manipulate the string while still having access
            // to the integer value.
            int day = i + 1;
            String printedDay = Integer.toString(day);

            // Asterisk the day if there is an event that day
            if(!month[i].isEmpty()) {
                printedDay += "*";
            }

            // Print the days in 5-character blocks including whitespace
            System.out.format("%-5s", printedDay);

            // Move to the next line at the end of the week
            if((day + firstOfMonth) % 7 == 0) {
                System.out.println();
            }
        }
        System.out.println();
    }

    private static void initializeEvents(Event[] month) {
        for(int i = 0; i < month.length; i++) {
            month[i] = new Event();
        }
    }

    private static void fillRandomEvents(Event[] month) {
        final String[] EVENT_NAMES = new String[] {
                "Battle at Wolf 359",
                "Dinner with Brian Kernighan",
                "Get Infinity Gauntlet",
                "Tune up Millenium Falcon",
                "Groundhog Day",
                "Take the Iron Throne",
                "Find Holy Grail",
                "Trip to Mordor",
                "Meeting with boss",
                "Buy new shoes"
        };
        int filled = 0;

        // While loop to fill the days
        while(filled < 10) {
            // Pick a random day
            int pos = (int)(Math.random() * (month.length));

            // Fill it with an event if it's empty
            if (month[pos].isEmpty()) {
                month[pos].update(randomTime(),
                                  EVENT_NAMES[filled],
                                  ((int)(Math.random() * 3) + 1));
                filled++;
            }
        }
    }

    // Generate a random time in the format of 12:59 PM
    private static String randomTime() {
        String time;

        time = ((int)(Math.random() * 12) + 1) + ":" +
               ((int)(Math.random() * 6)) +
               ((int)(Math.random() * 10));

        if(Math.random() < 0.5) {
            time += " PM";
        }
        else {
            time += " AM";
        }

        return time;
    }
}

/** PURPOSE: Stores an event, which has a String name and start-time, and an int
 *           priority. Initializes startTime and name with default values of "",
 *           and the priority with 0.
 */
class Event {
    private String startTime = "";
    private String name      = "";
    private int    priority  = 0;

    public Event() {
    }

    public Event(String startTime, String name, int priority) {
        this.startTime = startTime;
        this.name      = name;
        this.priority  = priority;
    }

    public void update(String startTime, String name, int priority) {
        this.startTime = startTime;
        this.name      = name;
        this.priority  = priority;
    }

    public String toString() {
        return startTime + ": " + name + " (priority " + priority + ")";
    }

    public String getStartTime() {
        return startTime;
    }

    public String getName() {
        return name;
    }

    public int getPriority() {
        return priority;
    }

    public boolean isEmpty() {
        return name.isEmpty() &&
               startTime.isEmpty() &&
               priority == 0;
    }
}