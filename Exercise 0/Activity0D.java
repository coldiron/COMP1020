public class Activity0D {
   public static void main(String[] args) {
      String[] phoneNumbers = new String[100];
      int[] callDurations = new int[phoneNumbers.length];
      int size = 0;

      size = addCall(phoneNumbers, callDurations, size, "555-555-1234", 10);
      size = addCall(phoneNumbers, callDurations, size, "555-555-4321", 20);
      size = addCall(phoneNumbers, callDurations, size, "555-555-1234", 30);
      size = addCall(phoneNumbers, callDurations, size, "555-555-1234", 30);
      size = addCall(phoneNumbers, callDurations, size, "555-555-1234", 30);
      size = addCall(phoneNumbers, callDurations, size, "555-555-1111", 10);
      size = addCall(phoneNumbers, callDurations, size, "555-555-1666", 30);
      size = addCall(phoneNumbers, callDurations, size, "555-555-1666", 37);

      System.out.println("Phone numbers (initially):");
      printList(phoneNumbers, callDurations, size);

      System.out.println("\nPhone numbers (after):");
      printList(phoneNumbers, callDurations, size);

      System.out.println("\nTotal durations:");
      totalDurations(phoneNumbers, callDurations, size);

      System.out.println("\nEnd of processing.");
   }


   public static int addCall(String[] phoneNumbers, int[] callDurations, int size, String newNumber, int newDuration) {
      if (size >= phoneNumbers.length) {
         System.out.println("Error adding " + newNumber + ": array capacity exceeded.");
      } else {
         phoneNumbers[size] = newNumber;
         callDurations[size] = newDuration;
         size++;
      }

      return size;
   }


   public static void printList(String[] phoneNumbers, int[] callDurations, int size) {
      for (int i = 0; i < size; i++) {
         System.out.println(phoneNumbers[i] + " duration: " + callDurations[i] + "s");
      }
   }

   public static int find(String[] list, int size, int start, String target) {
      int pos = start;

      while (pos < size && !target.equals(list[pos])) {
         pos++;
      }

      if (pos == size)
         pos = -1;

      return pos;
   }

   /**
    * PURPOSE: Given a list of phone numbers called and call durations, this 
    *          method trims combines duplicate phone numbers while summing 
    *          the durations, and then prints the newly trimmed and summed list.
    */
   public static void totalDurations(String[] phoneNumbers, int callDurations[], int size) {
      String[] trimmedPhoneNumbers = new String[size];
      int[] combinedCallDurations = new int[size];
      int newSize = 0;
      int pos;

      for (int i = 0; i < size; i++) {
         /**
          * Check if the phone number that called exists in our new array. If not,
          * add it. If it exists, add its duration to the existing entry.
          */
         pos = find(trimmedPhoneNumbers, size, 0, phoneNumbers[i]);

         if (pos == -1) {
            trimmedPhoneNumbers[newSize] = phoneNumbers[i];
            combinedCallDurations[newSize] = callDurations[i];
            newSize++;
         } else {
            combinedCallDurations[pos] += callDurations[i];
         }
      }

      printList(trimmedPhoneNumbers, combinedCallDurations, newSize);
   }

   public static void findAllCalls(String[] phoneNumbers, int[] callDurations, int size, String targetNumber) {
      int matchPos;

      System.out.println("Calls from " + targetNumber + ":");
      matchPos = find(phoneNumbers, size, 0, targetNumber);
      while (matchPos >= 0) {
         System.out.println(phoneNumbers[matchPos] + " duration: " + callDurations[matchPos] + "s");

         // Find the next match, starting after the last one
         matchPos = find(phoneNumbers, size, matchPos + 1, targetNumber);
      }
   }

   public static int removeCall(String[] phoneNumbers, int[] callDurations, int size, int posToRemove) {
      for (int i = posToRemove + 1; i < size; i++) {
         phoneNumbers[i - 1] = phoneNumbers[i];
         callDurations[i - 1] = callDurations[i];
      }
      size--;

      return size;
   }

   
}