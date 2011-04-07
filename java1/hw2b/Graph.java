class Graph 
{
    public static void main(String args[]) {
        for (String arg: args) {
            try {
                Integer.parseInt(arg);
            } catch (NumberFormatException e) {
                usage();
            }
        }

        for (String arg: args) {
            StringBuilder builder = new StringBuilder();
            int length = Integer.parseInt(arg);
            for (int i=0; i<length; i++) {
                builder.append('*');
            }
            System.out.println(builder.toString());
        }
    }

    private static void usage() {
        System.out.println("usage: DigitName [val1 [val2 ...]]");
        System.out.println("       val1, val2, etc. must be integers");
        System.exit(1);
    }
}
