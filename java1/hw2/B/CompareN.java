import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;

class CompareN
{
    public static final int MIN_ARGS = 2;
    public static final int MAX_ARGS = 26;

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

        Comparator<ArrayList<Object>> comp = new Comparator<ArrayList<Object>>() {
            public int compare(ArrayList<Object> a, ArrayList<Object> b) {
                Integer a_val = (Integer)a.get(1);
                Integer b_val = (Integer)b.get(1);
                String a_name = (String)a.get(0);
                String b_name = (String)b.get(0);
                if (a_val > b_val) {
                    return 1;
                } else if (a_val < b_val) {
                    return -1;
                } else {
                    return a_name.compareTo(b_name);
                }
            }
        };
        TreeSet<ArrayList<Object>> tree = new TreeSet<ArrayList<Object>>(comp);

        try {
            for (int i = 0; i < args.length; i++) {
                ArrayList<Object> p = new ArrayList<Object>();
                p.add((char)((65 | 32) + i) + "");
                p.add(Integer.parseInt(args[i]));
                tree.add(p);
            }
        } catch (NumberFormatException e) {
            System.out.println("Compare_abc: arguments must be integers");
            System.exit(1);
        }

        Iterator iter = tree.iterator();

        ArrayList<Object> last = (ArrayList<Object>)(iter.next());
        String name = (String)last.get(0);
        System.out.print(name);
        while (iter.hasNext()) {
            String separator = "<";
            ArrayList<Object> item = (ArrayList<Object>)(iter.next());
            Integer val = (Integer)item.get(1);
            Integer last_val = (Integer)last.get(1);
            if (val == last_val) {
                separator = "=";
            }
            name = (String)item.get(0);
            System.out.print(" " +separator+ " " +name);
            last = item;
        }
        System.out.println("");
    }
}

