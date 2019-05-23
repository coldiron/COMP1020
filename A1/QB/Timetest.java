public class Timetest {
    public static void main(String args[]) {

System.out.println(randomEventTime());
}
    private static String randomEventTime() {
        String time;
        time = ((int)(Math.random() * 13)) + ":" +
               ((int)(Math.random() * 6)) + 
               ((int)(Math.random() * 10));
        return time;
    }
}
