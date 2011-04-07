import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;

class Compare_abc 
{
    public static final int MIN_ARGS = 3;
    public static final int MAX_ARGS = 3;

    public static void main(String args[]) {
        if ((MIN_ARGS == MAX_ARGS) && (args.length != MAX_ARGS)) {
            System.out.println("Compare_abc: " +MAX_ARGS+ " arguments required");
            System.exit(1);
        }
        if (args.length < MIN_ARGS) {
            System.out.println("Compare_abc: at least " +MIN_ARGS+ " arguments required");
            System.exit(1);
        }
        if (args.length > MAX_ARGS) {
            System.out.println("Compare_abc: at most " +MAX_ARGS+ " arguments allowed");
            System.exit(1);
        }

        TreeSet<Pair> tree = new TreeSet<Pair>(new ComparePair());
        try {
            for (int i = 0; i < MAX_ARGS; i++) {
                tree.add(new Pair(new String((char)((65 | 32) + i) + ""), Integer.parseInt(args[i])));
            }
        } catch (NumberFormatException e) {
            System.out.println("Compare_abc: arguments must be integers");
            System.exit(1);
        }

        Iterator iter = tree.iterator();

        Pair last = (Pair)iter.next();
        System.out.print(last.name);
        while (iter.hasNext()) {
            String separator = "<";
            Pair item = (Pair)iter.next();
            if (item.value == last.value) {
                separator = "=";
            }
            System.out.print(" " +separator+ " " +item.name);
            last = item;
        }
        System.out.println("");
    }
}

class ComparePair
    implements Comparator<Pair>
{
    public int compare(Pair a, Pair b) {
        if (a.value > b.value) {
            return 1;
        } else if (a.value < b.value) {
            return -1;
        } else {
            return a.name.compareTo(b.name);
        }
    }
}

class Pair
{
    public String  name  = "";
    public Integer value = 0;

    public Pair(String name, Integer value) {
        this.name  = name;
        this.value = value;
    }
}
