public class A1QBMitsukLavitt7607877 {
    private static final String[] EVENT_NAMES = new String[] {
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

    public static void main(String[] args) {
        Event[] september = new Event[30];
        Event[] october   = new Event[31];

        int firstOfSeptember = (int)(Math.random() * 7);
        int firstOfOctober   = (firstOfSeptember + 30) % 7;

        initializeEvents(september);
        initializeEvents(october);

        // set 10 random events per month
        fillRandomEvents(september);
        fillRandomEvents(october);


        // Output
        System.out.println();
        System.out.println("September");
        System.out.println("_________________________________");
        printCalendar(september, firstOfSeptember);
        System.out.println();
        System.out.println("Events:");
        printList(september);
        System.out.println();
        System.out.println("High-priority events:");
        printPriorityList(september);

        System.out.println();
        System.out.println("October");
        System.out.println("_________________________________");
        printCalendar(october, firstOfOctober);
        System.out.println();
        System.out.println("Events:");
        printList(october);
        System.out.println();
        System.out.println("High-priority events:");
        printPriorityList(october);

        System.out.println();

/*         for(int i = 0; i < september.length; i++) {
            if(!september[i].isEmpty()){
                System.out.println(september[i].toString());
            }
        } */

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

        for(int i = 0; i < firstOfMonth; i++) {
            System.out.print("     ");
        }

        for(int i = 0; i < month.length; i++) {
            // We use both an integer and string version of the day of the month
            // because we need to manipulate the string while still having access 
            // to the integer value.
            int day = i + 1;
            String printedDay = Integer.toString(day);

            if(!month[i].isEmpty()) {
                printedDay += "*";
            }

            System.out.format("%-5s", printedDay);
            if(((day + firstOfMonth) % 7 == 0) || i == month.length) { 
                System.out.println();
            }
        }
        System.out.println();
    }

    /*private static String[] getCalendarRow(Event[] month, int startDay, int firstOfMonth) {
        String[] row = new String[7];
        for(int i = 0; i < 7 && i < month.length; i++) {
            row[i] = Integer.toString(startDay + 1 + i);
            if(!month[startDay + i].isEmpty()) {
                row[i] += "*";
            }
        }
        return row;
    }*/

    private static void initializeEvents(Event[] month) {
        for(int i = 0; i < month.length; i++) {
            month[i] = new Event();
        }
    }

    private static void fillRandomEvents(Event[] month) {
        int filled = 0;

        while(filled < 10) {
            // Pick a random day
            int pos = (int)(Math.random() * (month.length));
            // Fill it with an event if it's empty
            if (month[pos].isEmpty()) {
                String name = EVENT_NAMES[filled];
                int priority = (int)(Math.random() * 3) + 1;
                month[pos].update(randomTime(), name, priority);
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