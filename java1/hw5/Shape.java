public abstract class Shape {
    private static int count = 0;

    public Shape() {
        count++;
    }

    public static int numShapes() {
        return count;
    }

    public abstract double area();
}
