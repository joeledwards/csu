class BubbleSort
{
    public static void main(String args[]) {
        double[] values = new double[args.length];

        for (int i = 0; i < args.length; i++) {
            try {
                values[i] = Double.parseDouble(args[i]);
            } catch (NumberFormatException e) {
                usage();
            }
        }

        sort(values);

        for (double value: values) {
            System.out.print(value+ " ");
        }
        System.out.println("");
    }

    private static void sort(double[] values) {
        boolean swap_occurred = true;
        int count = values.length;
        double temp = 0;
        while (swap_occurred) {
            swap_occurred = false;
            for (int i = 0; i < (count - 1); i++) {
                if (values[i] > values[i+1]) {
                    temp = values[i];
                    values[i] = values[i+1];
                    values[i+1] = temp;
                    swap_occurred = true;
                }
            }
            count--;
        }
    }

    private static void usage() {
        System.out.println("usage: DigitName [val1 [val2 ...]]");
        System.out.println("       val1, val2, etc. must be integers");
        System.exit(1);
    }
}

