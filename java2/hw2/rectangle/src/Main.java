import java.util.Random;
import java.util.HashSet;
import java.util.TreeSet;
import java.util.ArrayList;

public class Main
{
    public static void main(String argv[])
    {
        HashSet<Rectangle>   hash = new HashSet<Rectangle>();
        TreeSet<Rectangle>   tree = new TreeSet<Rectangle>();
        ArrayList<Rectangle> list = new ArrayList<Rectangle>();

        Random r = new Random();
        for (int i=0; i < 1000; i++) {
            hash.add(new Rectangle(11+r.nextInt(10), 11+r.nextInt(10)));
            tree.add(new Rectangle(11+r.nextInt(10), 11+r.nextInt(10)));
            list.add(new Rectangle(11+r.nextInt(10), 11+r.nextInt(10)));
        }

        System.out.printf("HashSet elements   = %d\n", hash.size());
        System.out.printf("TreeSet elements   = %d\n", tree.size());
        System.out.printf("ArrayList elements = %d\n", list.size());

        int idx=0;
        System.out.println("");
        for (Rectangle a: hash) {
            System.out.printf("HashSet Item %d Area:%d h=%d w=%d\n", idx, a.area(), a.getHeight(), a.getWidth());
            idx++;
        }

        idx=0;
        System.out.println("");
        for (Rectangle b: tree) {
            System.out.printf("HashSet Item %d Area:%d h=%d w=%d\n", idx, b.area(), b.getHeight(), b.getWidth());
            idx++;
        }

        idx=0;
        System.out.println("");
        for (Rectangle c: list) {
            System.out.printf("HashSet Item %d Area:%d h=%d w=%d\n", idx, c.area(), c.getHeight(), c.getWidth());
            idx++;
        }
    }
}

