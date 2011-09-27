import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

public class Main
{
    public static void main(String argv[])
    {
        HashMap<Integer,Integer> count_map = new HashMap<Integer,Integer>(256);
        HashMap<Integer,Rectangle> map = new HashMap<Integer,Rectangle>(256);
        HashSet<Rectangle>   hash = new HashSet<Rectangle>(256, (float)0.75);
        TreeSet<Rectangle>   tree = new TreeSet<Rectangle>();
        ArrayList<Rectangle> list = new ArrayList<Rectangle>(1000);

        int temp = 0;
        int key = 0;
        Random r = new Random();
        Rectangle rect = null;
        for (int i=0; i < 1000; i++) {
            rect = new Rectangle(r.nextInt(30), r.nextInt(30));
            temp = 0;
            key = rect.hashCode();
            if (map.containsKey(key)) {
                temp = count_map.remove(key);
            }
            temp++;
            count_map.put(rect.hashCode(), temp);
            map.put(rect.hashCode(), rect);
            hash.add(rect);
            tree.add(rect);
            list.add(rect);
        }

        System.out.printf("HashMap elements   = %d\n", map.size());
        System.out.printf("HashSet elements   = %d\n", hash.size());
        System.out.printf("TreeSet elements   = %d\n", tree.size());
        System.out.printf("ArrayList elements = %d\n", list.size());

        int idx=0;
        System.out.println("");
        for (Rectangle d: hash) {
            System.out.printf("HashMap Item %d Area:%d h=%d w=%d (hash: %08x)\n", idx, d.area(), d.getHeight(), d.getWidth(), d.hashCode());
            idx++;
        }

        idx=0;
        System.out.println("");
        for (Rectangle a: hash) {
            System.out.printf("HashSet Item %d Area:%d h=%d w=%d (hash: %08x)\n", idx, a.area(), a.getHeight(), a.getWidth(), a.hashCode());
            idx++;
        }

        idx=0;
        System.out.println("");
        for (Rectangle b: tree) {
            System.out.printf("TreeSet Item %d Area:%d h=%d w=%d\n", idx, b.area(), b.getHeight(), b.getWidth());
            idx++;
        }

        idx=0;
        System.out.println("");
        for (Rectangle c: list) {
            System.out.printf("ArrayList Item %d Area:%d h=%d w=%d\n", idx, c.area(), c.getHeight(), c.getWidth());
            idx++;
        }

        idx=0;
        Set<Integer> keys = count_map.keySet();
        System.out.println("");
        for (Integer k: keys) {
            System.out.printf("Key=%08x Count=%d\n", k, count_map.get(k));
            idx++;
        }
    }
}

