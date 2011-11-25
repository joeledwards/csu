import java.awt.Color;
import java.awt.Graphics;
        
public class Rectangle
    implements Comparable<Rectangle>
{
    public static final int MIN_WIDTH = 1;
    public static final int MAX_WIDTH = 2048;

    public static final int MIN_HEIGHT = 1;
    public static final int MAX_HEIGHT = 2048;

    public static final int DEFAULT_WIDTH  = 10;
    public static final int DEFAULT_HEIGHT = 10;
    public static final Color DEFAULT_COLOR = Color.BLACK;

    private static int lastID = 1;
    public final int ID = lastID++;

    private int width = DEFAULT_WIDTH;
    private int height = DEFAULT_HEIGHT;
    private Color color = DEFAULT_COLOR;

    public Rectangle() {;}

    public Rectangle(int width) {
        setWidth(width);
        setHeight(width);
    }

    public Rectangle(int width, int height) {
        setWidth(width);
        setHeight(height);
    }

    public Rectangle(int width, int height, Color color) {
        setWidth(width);
        setHeight(height);
        this.color = color;
    }

    public Rectangle clone() {
        return new Rectangle(this.width, this.height, this.color);
    }

    public void setWidth(int width) {
        if (width < MIN_WIDTH) {
            this.width = MIN_WIDTH;
        } 
        else if (width > MAX_WIDTH) {
            this.width = MAX_WIDTH;
        } 
        else {
            this.width = width;
        }
    }

    public void setHeight(int height) {
        if (height < MIN_HEIGHT) {
            this.height = MIN_HEIGHT;
        } 
        else if (height > MAX_HEIGHT) {
            this.height = MAX_HEIGHT;
        } 
        else {
            this.height = height;
        }
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void drawAt(Graphics g, int x, int y) {
        Color lastColor = g.getColor();
        g.setColor(color);
        g.fillRect(x, y, width, height);
        g.setColor(lastColor);
    }

    public boolean equals(Object obj) {
        Rectangle other = (Rectangle)obj;
        if (width != other.width)
            return false;
        if (height != other.height)
            return false;
        if (!(color.equals(other.color)))
            return false;
        return true;
    }

    public int compareTo(Rectangle other) {
        if (height > other.height)
            return 1;
        if (height < other.height)
            return -1;
        return 0;
    }

    public int computeArea() {
        return width * height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Color getColor() {
        return color;
    }
}

