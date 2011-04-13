public class GraphicRectangle extends Rectangle implements Drawable {
    private int color = 0;

    public GraphicRectangle(double width, double length, int color) {
        super(width, length);
        this.color = color;
    }

    public void draw() {
        System.out.println(" rectangle drawn using color: " + color);
    }
}
