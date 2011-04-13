public class GraphicCircle extends Circle implements Drawable {
    private int color = 0;

    public GraphicCircle(double radius, int color) {
        super(radius);
        this.color = color;
    }

    public void draw() {
        System.out.println(" circle drawn using color: " + color);
    }
}
