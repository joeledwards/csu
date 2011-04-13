import java.lang.Math;

public class Circle extends Shape {
    private static int count = 0;

    private double radius;

    public Circle(double radius) {
        super();
        count++;
        this.radius = radius;
    }

    public static int numCircles() {
        return count;
    }

    public double area() {
        return Math.PI * radius * radius;
    }
}
