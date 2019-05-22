/**
 * A1QAMitsukLavitt7607877 
 *
 * COMP 1020 SECTION D01
 * INSTRUCTOR    Safiur Mahdi
 * ASSIGNMENT    Assignment 1, question A
 * @author       Richard Mitsuk Lavitt, 7607877
 * @version      2019-05-21 
 *
 * PURPOSE: Processes passenger lists and fits them into
 *          an airplane seating chart.
 */
public class A1QAMitsukLavitt7607877 {
    // Provide a standard label for easier identification of empty seats
    private static final String EMPTY_SEAT_LABEL = "-empty-";

    public static void main(String[] args) {
        String[] firstBooking = {
            "8",
            "2",
            "Nobbly, Greg",
            "Nobbly, Jo-Anne",
            "1",
            "Lee, Sook",
            "3",
            "Lukas, Stephie",
            "Lukas, Cambridge",
            "Lukas, Ogden"
        };
        String[] secondBooking = {
            "12",
            "2",
            "Picard, Jean-Luc",
            "LaForge, Geordi",
            "2",
            "Riker, William",
            "Troi, Deanna",
            "2",
            "Crusher, Wesley",
            "Crusher, Beverly",
            "3",
            "O'Brien, Miles",
            "O'Brien, Keiko",
            "O'Brien, Molly",
            "1",
            "Data",
            "2",
            "Worf",
            "Rozshenko, Alexander"
        };

        // Add a blank line to make any errors print more neatly
        System.out.println(); 

        // Seat the bookings.
        String[] firstSeats  = seatBooking(firstBooking);
        String[] secondSeats = seatBooking(secondBooking);
        String[] thirdSeats = seatBooking(Bookings.getBookings());

        // Add a line after any errors.
        System.out.println();

        // Display our seating plans.
        System.out.println("Seated bookings:\n");

        System.out.println("First booking:");
        printSeats(firstSeats);
        System.out.println();
        
        System.out.println("Second booking:");
        printSeats(secondSeats);
        System.out.println();

        System.out.println("Third booking:");
        printSeats(thirdSeats);
        System.out.println();
    }

    private static void printSeats(String[] seats) {
        for(int i = 0; i < seats.length; i++) {
            System.out.println("Seat " + (i + 1) + ": " + seats[i]);
        }
    }

    /* PURPOSE: Seats a given array of passengers. Returns a new, appropriately sized
     *          array of airline seats assigned to passengers.
     */
    private static String[] seatBooking(String[] bookings) {
        // Get the size of the flight by parsing the first element of the 
        // bookings[] array.
        String[] seats = new String[Integer.parseInt(bookings[0])];

        // Initialize the seats as empty
        for(int i = 0; i < seats.length; i++) {
            seats[i] = EMPTY_SEAT_LABEL; 
        }

        // We use a while loop here and declare the counter externally
        // because we will use a value from the bookings[] array to increment
        // the counter in each iteration.
        int groupSizePosition = 1;
        while (groupSizePosition < bookings.length) {
            int groupSize = getGroupSize(bookings, groupSizePosition);

            // See if there enough seats remaining on the flight for the group
            if(groupSize > emptySeats(seats)) {
                System.err.println("Not enough seats remaining for group of " + groupSize + " with members:");
                printGroupMembers(bookings, groupSizePosition);
                System.out.println();
                // Traverse the array to the next group
                groupSizePosition += groupSize + 1;
            } 
            // Go through the seats array to determine if a block of empty seats
            // large enough to fit the entire group is available.
            else if(checkSeatBlocks(seats, groupSize)) {
                seats = seatGroupInBlock(seats, bookings, groupSizePosition);
                groupSizePosition += groupSize + 1;
            }
            // Seat the rest randomly.
            else {
                seatRandomly(seats, bookings, groupSizePosition);
                groupSizePosition += groupSize + 1;
            }
        }
        return seats;
    }

    
    private static void printGroupMembers(String[] bookings, int groupSizePosition) {
       int start = groupSizePosition + 1;
       int groupSize = getGroupSize(bookings, groupSizePosition);
        for(int i = start; i < start + groupSize; i++) {
            System.out.println(bookings[i]);
        } 
    }

    /**
     * Seats passengers in a random manner.
     * Selects random element in array of seats and checks if it is empty. If so,
     * a passenger will be seated in that seat.
     */
    private static String[] seatRandomly(String[] seats, String[] bookings, int groupSizePosition) {
        int passengersSeated = 0;
        int groupSize = getGroupSize(bookings, groupSizePosition);

        while(passengersSeated < groupSize) {
            int randomSeat = (int) (Math.random() * seats.length);

            if(seats[randomSeat] == EMPTY_SEAT_LABEL) {
                seats[randomSeat] = bookings[groupSizePosition + 1 + passengersSeated];
                passengersSeated++;
            }
        }
        return seats;
    }

    private static int getGroupSize(String[] bookings, int groupSizePosition) {
        return Integer.parseInt(bookings[groupSizePosition]);
    }

    private static String[] seatGroupInBlock(String[] seats, String[] bookings, int groupSizePosition) {
        int groupSize = getGroupSize(bookings, groupSizePosition);
        boolean seated = false;

        while(seated == false) {
            // Look for a random block starting anywhere from the beginning of the seats up to the
            // last possible place the seating will fit, which is seats.length - groupSize.
            int randomBlockStart = (int) (Math.random() * (seats.length - groupSize + 1));
            int endOfBlock = groupSize + randomBlockStart;
            int emptyConsecutiveSeats = seatsInBlock(seats, groupSize, randomBlockStart, endOfBlock);

            if(emptyConsecutiveSeats >= groupSize) {
                // Traverse our group and seat them
                for(int i = 0; i < groupSize; i++) {
                    seats[randomBlockStart + i] = bookings[groupSizePosition + i + 1];
                }
                seated = true;
            }
        }
        return seats;
    }

    /**
     * Checks the *entire* flight to see if *any* blocks that will fit a group of
     * passengers is available.
     * 
     * @param  seats         Array of all seats on flight
     * @param  groupSize     Size of the group we are trying to seat
     * 
     * @return               whether or not there is an available block
     */
    private static boolean checkSeatBlocks(String[] seats, int groupSize) {
        boolean seatBlockAvailable = false;

        if (seatsInBlock(seats, groupSize, 0, seats.length) >= groupSize) {
            seatBlockAvailable = true;
        }
        return seatBlockAvailable;
    }

    // Checks if a given seat is empty
    private static boolean seatEmpty(String seat) {
        return seat == EMPTY_SEAT_LABEL;
    }

    /**
     * Counts empty seats in a given block of seats
     * 
     * @param  seats         Array of all seats on flight
     * @param  groupSize     Size of the group we are trying to seat
     * @param  position      Starting position of block we are checking
     * @param  endOfBlock    End of block we are checking
     * 
     * @return               number of seats remaining
     */
    private static int seatsInBlock(String[] seats, int groupSize, int position, int endOfBlock) {
        int seatsInBlock = 0;

        for(; position < endOfBlock && seatsInBlock < groupSize; position++) {
            // Increment seats in block if we encounter an empty seat.
            if(seatEmpty(seats[position])) {
                seatsInBlock++;
            }
            // Reset our counter if we encounter a non-empty seat.
            else {
                seatsInBlock = 0;
            }
        }
        return seatsInBlock;
    }

    // Counts empty seats remaining on flight.
    private static int emptySeats(String[] seats) {
        int emptySeats = 0;

        for(String seat: seats) {
            if(seatEmpty(seat)) {
                emptySeats++;
            }
        }
        return emptySeats;
    }
}
