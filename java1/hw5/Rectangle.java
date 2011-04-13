public class Rectangle extends Shape {
    private static int count = 0;

    private double width;
    private double length;

    public Rectangle(double width, double length) {
        super();
        count++;
        this.width = width;
        this.length = length;
    }

    public static int numRectangles() {
        return count;
    }

    public double area() {
        return width * length;
    }
}
