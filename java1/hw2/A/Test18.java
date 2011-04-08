class Test18
{
    public static void main(String args[]) {
        int totalSeconds = 370;
        int minutes = totalSeconds / 60;   // whole minutes
        int seconds = totalSeconds % 60;   // remaining seconds
        System.out.println("Total Seconds: " + totalSeconds);
        System.out.println("------------------------");
        System.out.println("Minutes:       " + minutes);
        System.out.println("Seconds:       " + seconds);
    }

}
