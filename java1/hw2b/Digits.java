class Digits 
{
    public static void main(String args[]) {
        if (args.length != 1) {
            usage();
        }

        int digit = 0;
        try {
            digit = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            usage();
        }

        if ((digit < 0) || (digit > 9)) {
            usage();
        }

        String text = "";
        
        switch (digit) {
            case 0:  text = "zero";   break;
            case 1:  text = "one";    break;
            case 2:  text = "two";    break;
            case 3:  text = "three";  break;
            case 4:  text = "four";   break;
            case 5:  text = "five";   break;
            case 6:  text = "six";    break;
            case 7:  text = "seven";  break;
            case 8:  text = "eight";  break;
            case 9:  text = "nine";   break;
            //default: text = "uknown"; break;
        }

        System.out.println(text);
    }

    private static void usage() {
        System.out.println("usage: DigitName <digit>");
        System.out.println("       digit must be an integer digit between 0 and 9");
        System.exit(1);
    }
}
