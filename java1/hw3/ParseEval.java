import java.util.ArrayList;
import java.util.StringTokenizer;

class ParseEval
{
    public static final int NONE = 0;
    public static final int ADD = 1;
    public static final int SUBTRACT = 2;

    private static void error(String message) {
        System.out.println("E: " + message);
        System.exit(1);
    }

    public static void main(String[] args) {
        StringTokenizer tokenizer;
        ArrayList<String> tokens = new ArrayList<String>(args.length);
        
        // Look for tokens within each token returned from the command-line
        for (String arg: args) {
            tokenizer = new StringTokenizer(arg, "-+", true);
            while (tokenizer.hasMoreTokens()) {
                tokens.add(tokenizer.nextToken());
            }
        }

        boolean last_was_value = false;
        int     last_delimiter = ADD;
        double  value = 0;
        double  total = 0;
        int     token_index = 1;
        for (String token: tokens) {
            if ("+".compareTo(token) == 0) {
                if (!last_was_value) {
                    error("Adjacent operators are not allowed");
                }
                last_delimiter = ADD;
                last_was_value = false;
            } else if ("-".compareTo(token) == 0) {
                if (!last_was_value) {
                    error("Adjacent operators are not allowed");
                }
                last_delimiter = SUBTRACT;
                last_was_value = false;
            } else {
                try {
                    value = Double.parseDouble(token);
                    if (last_was_value) {
                        error("Adjacent values are not allowed");
                    }
                    last_was_value = true;
                    if (last_delimiter == SUBTRACT) {
                        total -= value;
                    } else if (last_delimiter == ADD) {
                        total += value;
                    } else {
                        // This should never occur
                        error("Unsupported arithmetic operation");
                    }
                } catch (NumberFormatException e) {
                    error("Unsupported input value '" +token+ "'");
                }
            }
            token_index++;
        }

        System.out.println("" + total);
    }
}
